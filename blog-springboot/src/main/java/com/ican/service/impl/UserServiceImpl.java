package com.ican.service.impl;

import cn.hutool.captcha.generator.RandomGenerator;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ican.entity.User;
import com.ican.entity.Visitor;
import com.ican.enums.FilePathEnum;
import com.ican.enums.LoginTypeEnum;
import com.ican.exception.ServiceException;
import com.ican.mapper.UserMapper;
import com.ican.mapper.VisitorMapper;
import com.ican.model.dto.*;
import com.ican.model.vo.*;
import com.ican.service.RedisService;
import com.ican.service.SiteSettingService;
import com.ican.service.UserService;
import com.ican.strategy.context.UploadStrategyContext;
import com.ican.utils.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.ican.constant.CommonConstant.*;
import static com.ican.constant.MqConstant.EMAIL_EXCHANGE;
import static com.ican.constant.RedisConstant.*;
import static com.ican.enums.UserTypeEnum.*;
import static com.ican.utils.CommonUtils.checkEmail;

/**
 * 用户业务接口
 *
 * @author ican
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RedisService redisService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private VisitorMapper visitorMapper;

    @Autowired
    private UploadStrategyContext uploadStrategyContext;

    @Autowired
    private SiteSettingService siteSettingService;

    @Override
    public UserInfo getUserInfo() {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (Objects.isNull(loginUser)) {
            return null;
        }
        Integer userId = SecurityUtils.getUserId();
        Set<Object> articleLikeSet = redisService.getSet(USER_ARTICLE_LIKE + userId);
        Set<Object> commentLikeSet = redisService.getSet(USER_COMMENT_LIKE + userId);
        Set<Object> talkLikeSet = redisService.getSet(USER_TALK_LIKE + userId);
        return UserInfo
                .builder()
                .id(loginUser.getId())
                .avatar(loginUser.getAvatar())
                .nickname(loginUser.getNickname())
                .username(loginUser.getUsername())
                .email(loginUser.getEmail())
                .articleLikeSet(articleLikeSet)
                .commentLikeSet(commentLikeSet)
                .talkLikeSet(talkLikeSet)
                .loginType(loginUser.getLoginType())
                .loginTime(loginUser.getLoginTime())
                .build();
    }

    @Override
    public void sendCode(String username) {
        if (!checkEmail(username)) {
            throw new ServiceException("请输入正确的邮箱！");
        }
        RandomGenerator randomGenerator = new RandomGenerator("0123456789", 6);
        String code = randomGenerator.generate();
        MailDTO mailDTO = MailDTO.builder()
                .toEmail(username)
                .subject("验证码")
                .content("您的验证码为 " + code + " 有效期为" + CODE_EXPIRE_TIME + "分钟")
                .build();
        // 验证码存入消息队列
        rabbitTemplate.convertAndSend(EMAIL_EXCHANGE, "*", JSON.toJSONBytes(mailDTO));
        // 验证码存入redis
        redisService.setObject(CODE_KEY + username, code, CODE_EXPIRE_TIME, TimeUnit.MINUTES);
    }

    @Override
    public void updateAdminPassword(PasswordDTO password) {
        // 查询旧密码是否正确
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getId, SecurityUtils.getUserId()));
        // 正确则修改密码，错误则提示不正确
        if (Objects.nonNull(user) && BCrypt.checkpw(password.getOldPassword(), user.getPassword())) {
            User newUser = User.builder()
                    .id(SecurityUtils.getUserId())
                    .password(BCrypt.hashpw(password.getNewPassword(), BCrypt.gensalt()))
                    .build();
            userMapper.updateById(newUser);
        } else {
            throw new ServiceException("旧密码不正确");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void register(RegisterDTO register) {
        verifyCode(register.getUsername(), register.getCode());
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .select(User::getUsername)
                .eq(User::getUsername, register.getUsername()));
        if (Objects.nonNull(user)) {
            throw new ServiceException("邮箱已注册！");
        }
        // 登录信息
        Visitor newVisitor = new Visitor();
        visitorMapper.insert(newVisitor);
        QqUtils.getQqInfo(register.getUsername());
        String userAvatar = QqUtils.avatar;
        if (!StringUtils.hasText(userAvatar)) {
            userAvatar = siteSettingService.getSiteSetting().getUserAvatar();
        }
        // 新增用户
        User newUser = User.builder()
                .visitorId(newVisitor.getId())
                .avatar(userAvatar)
                .nickname(QqUtils.nickname)
                .username(register.getUsername())
                .password(BCrypt.hashpw(register.getPassword(), BCrypt.gensalt()))
                .email(register.getUsername())
                .loginType(LoginTypeEnum.EMAIL.getLoginType())
                .role(ROLE_VISITOR)
                .build();
        userMapper.insert(newUser);
    }

    @Override
    public PageResult<OnlineVO> listOnlineUser(ConditionDTO condition) {
        List<OnlineVO> onlineList = redisService.getKeys(LOGIN_KEY + "*")
                .stream()
                .map(item -> {
                    LoginUser loginUser = redisService.getObject(item);
                    OnlineVO onlineVO = BeanCopyUtils.copyBean(loginUser, OnlineVO.class);
                    onlineVO.setUserId(loginUser.getId());
                    onlineVO.setAvatar(loginUser.getAvatar());
                    onlineVO.setNickname(loginUser.getNickname());
                    return onlineVO;
                })
                .filter(item -> !StringUtils.hasText(condition.getKeyword()) || item.getNickname().contains(condition.getKeyword()))
                .sorted(Comparator.comparing(OnlineVO::getLoginTime).reversed())
                .collect(Collectors.toList());
        return new PageResult<>(onlineList, onlineList.size());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateUserInfo(UserInfoDTO userInfo) {
        User user = User.builder()
                .id(SecurityUtils.getUserId())
                .nickname(userInfo.getNickname())
                .build();
        userMapper.updateById(user);
    }

    @Override
    public void updatePassword(UserDTO user) {
        // 校验账号是否合法
        verifyCode(user.getUsername(), user.getCode());
        User existUser = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .select(User::getUsername)
                .eq(User::getUsername, user.getUsername()));
        if (Objects.isNull(existUser)) {

            throw new ServiceException("邮箱尚未注册！");
        }
        // 根据用户名修改密码
        userMapper.update(new User(), new LambdaUpdateWrapper<User>()
                .set(User::getPassword, BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()))
                .eq(User::getUsername, user.getUsername()));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String updateUserAvatar(MultipartFile file) {
        // 头像上传
        String avatar = uploadStrategyContext.executeUploadStrategy(file, FilePathEnum.AVATAR.getPath());
        // 更新用户头像
        User newUser = User.builder()
                .id(SecurityUtils.getUserId())
                .avatar(avatar)
                .build();
        userMapper.updateById(newUser);
        return avatar;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateUserEmail(EmailDTO email) {
        verifyCode(email.getEmail(), email.getCode());
        User newUser = User.builder()
                .id(SecurityUtils.getUserId())
                .email(email.getEmail())
                .build();
        userMapper.updateById(newUser);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateUserStatus(StatusDTO userStatus) {
        // 更新用户状态
        User user = User.builder()
                .id(userStatus.getId())
                .status(userStatus.getStatus())
                .build();
        userMapper.updateById(user);
    }

    @Override
    public PageResult<UserBackVO> listUserBackVO(ConditionDTO condition) {
        // 获取后台用户数量
        Integer count = userMapper.countUser(condition);
        if (count == 0) {
            return new PageResult<>();
        }
        // 获取后台用户列表
        List<UserBackVO> userBackVOList = userMapper.listUsers(PageUtils.getLimit(), PageUtils.getSize(), condition);
        return new PageResult<>(userBackVOList, count);
    }

    @Override
    public void updateAdminInfo(AdminInfoDTO adminInfo) {
        User newUser = User.builder()
                .id(SecurityUtils.getUserId())
                .username(adminInfo.getUsername())
                .nickname(adminInfo.getNickname())
                .email(adminInfo.getEmail())
                .build();
        userMapper.updateById(newUser);
    }

    @Override
    public List<UserZoneVO> listUserZone(ConditionDTO condition) {
        List<UserZoneVO> userZoneVOList = new ArrayList<>();
        if (Objects.requireNonNull(getUserType(condition.getUserType())) == USER) {
            // 查询注册用户区域分布
            Object userZone = redisService.getObject(USER_ZONE);
            if (Objects.nonNull(userZone)) {
                userZoneVOList = JSON.parseObject(userZone.toString(), List.class);
            }
            return userZoneVOList;
        }
        if (Objects.requireNonNull(getUserType(condition.getUserType())) == VISITOR) {
            // 查询游客区域分布
            Map<String, Integer> visitorZone = redisService.getHashAll(VISITOR_ZONE);
            if (Objects.nonNull(visitorZone)) {
                userZoneVOList = visitorZone.entrySet()
                        .stream()
                        .map(item -> UserZoneVO.builder()
                                .name(item.getKey())
                                .value(Long.valueOf(item.getValue().toString()))
                                .build())
                        .collect(Collectors.toList());
            }
            return userZoneVOList;
        }
        return userZoneVOList;
    }

    /**
     * 校验验证码
     *
     * @param username 用户名
     * @param code     验证码
     */
    public void verifyCode(String username, String code) {
        String sysCode = redisService.getObject(CODE_KEY + username);
        if (!StringUtils.hasText(sysCode)) {
            throw new ServiceException("验证码未发送或已过期！");
        }
        if (!code.equals(sysCode)) {
            throw new ServiceException("验证码错误，请重新输入！");
        }
    }

    /**
     * 每天凌晨零点执行
     * 统计用户地区
     */
    @Scheduled(cron = "0 0 * * * ?")
    public void statisticalUserZone() {
        // 查询用户登录记录
        List<Visitor> visitorList = visitorMapper.selectList(new LambdaQueryWrapper<Visitor>()
                .select(Visitor::getIpSource));
        // 统计用户区域分布
        Map<String, Long> userZoneMap = visitorList.stream()
                .map(item -> {
                    if (StringUtils.hasText(item.getIpSource())) {
                        return item.getIpSource().split("\\|")[1].replaceAll(PROVINCE, "");
                    }
                    return UNKNOWN;
                })
                .collect(Collectors.groupingBy(item -> item, Collectors.counting()));
        // 转换格式
        List<UserZoneVO> userZoneList = userZoneMap.entrySet().stream()
                .map(item -> UserZoneVO.builder()
                        .name(item.getKey())
                        .value(item.getValue())
                        .build())
                .collect(Collectors.toList());
        redisService.setObject(USER_ZONE, JSON.toJSONString(userZoneList));
    }

}
