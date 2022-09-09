package com.ican.utils;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.ican.constant.CommonConstant;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * QQ工具
 *
 * @author ican
 */
@Slf4j
public class QqUtils {

    private static final String API = "https://api.lixingyong.com/api/qq?id=";

    public static String nickname;

    public static String avatar;

    public static void getQqInfo(String email) {
        try {
            URL url = new URL(API + email.substring(0, email.length() - 7));
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openConnection().getInputStream(), StandardCharsets.UTF_8));
            String line;
            StringBuilder result = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            reader.close();
            nickname = JSON.parseObject(result.toString(), Map.class).get("nickname").toString();
            avatar = JSON.parseObject(result.toString(), Map.class).get("avatar").toString();
        } catch (Exception e) {
            log.error("QQ邮箱解析错误" + e);
        } finally {
            nickname = CommonConstant.DEFAULT_NICKNAME + IdWorker.getId();
            avatar = "";
        }
    }
}
