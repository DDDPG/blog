package com.ican.service.impl;

import com.ican.entity.Visitor;
import com.ican.manager.AsyncManager;
import com.ican.manager.factory.AsyncFactory;
import com.ican.model.dto.LoginUser;
import com.ican.service.RedisService;
import com.ican.service.TokenService;
import com.ican.utils.IpUtils;
import com.ican.utils.UserAgentUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.ican.constant.RedisConstant.LOGIN_KEY;
import static com.ican.constant.RedisConstant.TWENTY_MINUTES;
import static com.ican.enums.ZoneEnum.SHANGHAI;

/**
 * Token业务接口实现类
 *
 * @author ican
 */
@Service
public class TokenServiceImpl implements TokenService {

    @Value("${token.header}")
    private String header;

    @Value("${token.secret}")
    private String secret;

    @Value("${token.expireTime}")
    private Integer expireTime;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private RedisService redisService;

    /**
     * 返回唯一标识
     *
     * @return uuid
     */
    public String getUuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 创建令牌
     *
     * @param loginUser 用户信息
     * @return 令牌
     */
    @Override
    public String createToken(LoginUser loginUser) {
        // 设置用户登录时间
        LocalDateTime currentTime = LocalDateTime.now(ZoneId.of(SHANGHAI.getZone()));
        loginUser.setLoginTime(currentTime);
        setUserAgent(loginUser);
        refreshToken(currentTime, loginUser);
        recordVisitor(loginUser);
        String userId = loginUser.getId().toString();
        return createToken(userId);
    }

    /**
     * 生成token
     *
     * @param subject subject参数
     * @return token
     */
    @Override
    public String createToken(String subject) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        SecretKey secretKey = generalKey();
        return Jwts.builder().setId(getUuid()).setSubject(subject)
                // 签发者
                .setIssuer("ican")
                //使用HS256对称加密算法签名, 第二个参数为秘钥
                .signWith(signatureAlgorithm, secretKey).compact();
    }

    /**
     * 解析token
     *
     * @param token token对象
     * @return 返回
     */
    @Override
    public Claims parseToken(String token) {
        SecretKey secretKey = generalKey();
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }

    /**
     * 生成加密后的秘钥 secretKey
     *
     * @return key
     */
    public SecretKey generalKey() {
        byte[] encodedKey = Base64.getDecoder().decode(secret);
        return new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
    }

    /**
     * 设置用户代理信息
     *
     * @param loginUser 登录信息
     */
    @Override
    public void setUserAgent(LoginUser loginUser) {
        Map<String, String> userAgentMap = UserAgentUtils.parseOsAndBrowser(request.getHeader("User-Agent"));
        String ip = IpUtils.getIpAddress(request);
        loginUser.setIpAddress(ip);
        loginUser.setIpSource(IpUtils.getCityInfo(ip));
        loginUser.setBrowser(userAgentMap.get("browser"));
        loginUser.setOs(userAgentMap.get("os"));
        loginUser.setLoginTime(LocalDateTime.now(ZoneId.of(SHANGHAI.getZone())));
    }

    /**
     * 获取用户身份信息
     *
     * @param request request
     * @return 用户信息
     */
    @Override
    public LoginUser getLoginUser(HttpServletRequest request) {
        String token = request.getHeader(header);
        if (StringUtils.hasText(token)) {
            try {
                Claims claims = parseToken(token);
                String userId = claims.getSubject();
                return redisService.getObject(LOGIN_KEY + userId);
            } catch (Exception ignored) {
            }
        }
        return null;
    }

    /**
     * 验证令牌有效期，相差不足20分钟，自动刷新缓存
     *
     * @param loginUser 登录信息
     */
    @Override
    public void verifyToken(LoginUser loginUser) {
        LocalDateTime expireTime = loginUser.getExpireTime();
        LocalDateTime currentTime = LocalDateTime.now(ZoneId.of(SHANGHAI.getZone()));
        if (Duration.between(currentTime, expireTime).toMinutes() <= TWENTY_MINUTES) {
            refreshToken(currentTime, loginUser);
        }
    }

    /**
     * 刷新token有效期
     *
     * @param loginUser 登录信息
     */
    @Override
    public void refreshToken(LocalDateTime currentTime, LoginUser loginUser) {
        // 设置有效期
        loginUser.setExpireTime(currentTime.plusMinutes(expireTime));
        String userId = loginUser.getId().toString();
        redisService.setObject(LOGIN_KEY + userId, loginUser, expireTime, TimeUnit.MINUTES);
    }

    /**
     * 记录用户登录信息
     *
     * @param loginUser 登录信息
     */
    @Override
    public void recordVisitor(LoginUser loginUser) {
        Visitor visitor = Visitor.builder()
                .id(loginUser.getVisitorId())
                .ipAddress(loginUser.getIpAddress())
                .ipSource(loginUser.getIpSource())
                .os(loginUser.getOs())
                .browser(loginUser.getBrowser())
                .loginTime(loginUser.getLoginTime())
                .build();
        AsyncManager.me().execute(AsyncFactory.recordVisitor(visitor));
    }

    /**
     * 删除用户身份信息
     *
     * @param userId 用户id
     */
    @Override
    public void delLoginUser(Integer userId) {
        redisService.deleteObject(LOGIN_KEY + userId);
    }
}
