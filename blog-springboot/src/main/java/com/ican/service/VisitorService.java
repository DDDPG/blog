package com.ican.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ican.entity.Visitor;

/**
 * 登录信息接口
 *
 * @author ican
 */
public interface VisitorService extends IService<Visitor> {
    /**
     * 更新用户访问信息
     *
     * @param visitor 登录信息
     */
    void updateVisitor(Visitor visitor);
}
