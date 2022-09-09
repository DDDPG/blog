package com.ican.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ican.entity.Visitor;
import com.ican.mapper.VisitorMapper;
import com.ican.service.VisitorService;
import org.springframework.stereotype.Service;

/**
 * 登录信息接口实现类
 *
 * @author ican
 */
@Service
public class VisitorServiceImpl extends ServiceImpl<VisitorMapper, Visitor> implements VisitorService {
    @Override
    public void updateVisitor(Visitor visitor) {
        this.updateById(visitor);
    }
}
