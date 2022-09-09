package com.ican.utils;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.codec.binary.Hex;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.security.MessageDigest;

/**
 * 文件md5工具类
 *
 * @author ican
 */
@Log4j2
public class FileUtils {

    /**
     * 获取文件md5值
     *
     * @param inputStream 文件输入流
     * @return {@link String} 文件md5值
     */
    public static String getMd5(InputStream inputStream) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("md5");
            byte[] buffer = new byte[8192];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                md5.update(buffer, 0, length);
            }
            return new String(Hex.encodeHex(md5.digest()));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 得到文件扩展名
     *
     * @param fileName 文件名称
     * @return {@link String} 文件后缀
     */
    public static String getExtName(String fileName) {
        if (StringUtils.isBlank(fileName)) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf("."));
    }

    /**
     * 转换file
     *
     * @param multipartFile 文件
     * @return {@link File} 临时文件
     */
    public static File multipartFileToFile(MultipartFile multipartFile) {
        File tempFile = null;
        try {
            // 获取文件md5值
            String md5 = getMd5(multipartFile.getInputStream());
            // 获取文件扩展名
            String extName = getExtName(multipartFile.getOriginalFilename());
            // 重新生成文件名
            String fileName = md5 + extName;
            // 创建临时文件
            tempFile = File.createTempFile(fileName, extName);
            multipartFile.transferTo(tempFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tempFile;
    }

}
