package com.ican.utils;

import org.apache.http.Consts;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * HttpClient工具类
 *
 * @author ican
 */
public class HttpClientUtils {

    private static final Logger logger = LoggerFactory.getLogger(HttpClientUtils.class);

    /**
     * 默认超时时间20秒
     */
    private static final int DEFAULT_TIMEOUT = 20 * 1000;

    /**
     * HttpClient上传文件
     *
     * @param url      请求接口
     * @param headers  请求参数
     * @param file     文件
     * @param fileName 文件名称
     * @return 响应结果
     */
    public static String uploadFileByHttpClient(String url, Map<String, String> headers, File file, String fileName) {
        String resultString = "";
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        try {
            // 创建http post请求
            HttpPost httpPost = new HttpPost(url);
            // 设置超时时间
            RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(DEFAULT_TIMEOUT)
                    .setConnectionRequestTimeout(DEFAULT_TIMEOUT).setSocketTimeout(DEFAULT_TIMEOUT).build();
            httpPost.setConfig(requestConfig);
            // 设置参数信息
            // HttpMultipartMode.RFC6532参数的设定是为避免文件名为中文时乱码
            MultipartEntityBuilder builder = MultipartEntityBuilder.create().setMode(HttpMultipartMode.RFC6532);
            builder.setCharset(StandardCharsets.UTF_8);
            // 设置文件
            builder.addBinaryBody("file_up", file, ContentType.MULTIPART_FORM_DATA, fileName);
            // 设置必要参数
            builder.addTextBody("category", "daily");
            httpPost.setEntity(builder.build());
            // 设置头信息
            if (headers != null) {
                for (String key : headers.keySet()) {
                    httpPost.setHeader(key, headers.get(key));
                }
            }
            // 执行http请求
            response = httpClient.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                resultString = EntityUtils.toString(response.getEntity(), Consts.UTF_8);
            }
        } catch (Exception e) {
            logger.error("上传文件失败：", e);
        } finally {
            try {
                if (httpClient != null) {
                    httpClient.close();
                }
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                logger.error("关闭异常" + e);
            }
        }
        return resultString;
    }

}