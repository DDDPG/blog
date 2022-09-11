package com.ican.quartz.task;

import org.springframework.stereotype.Component;

/**
 * @author 86132
 */
@Component("ryTask")
public class RyTask
{

    public void ryParams(String params)
    {
        System.out.println("执行有参方法：" + params);
    }

    public void ryNoParams()
    {
        System.out.println("执行无参方法");
    }
}
