package com.ican.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 错误信息处理类
 *
 * @author ican
 */
public class ExceptionUtils {

    /**
     * 获取exception的详细错误信息
     *
     * @param e 异常
     * @return 错误信息
     */
    public static String getExceptionMessage(Throwable e) {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw, true));
        return sw.toString();
    }

}
