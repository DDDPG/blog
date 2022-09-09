package com.ican.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ican.entity.UniqueView;
import com.ican.mapper.UniqueViewMapper;
import com.ican.model.vo.UniqueViewVO;
import com.ican.service.RedisService;
import com.ican.service.UniqueViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.ican.constant.RedisConstant.UNIQUE_VISITOR;
import static com.ican.constant.RedisConstant.VISITOR_ZONE;
import static com.ican.enums.ZoneEnum.SHANGHAI;

/**
 * 用户量统计接口实现类
 *
 * @author ican
 */
@Service
public class UniqueViewServiceImpl extends ServiceImpl<UniqueViewMapper, UniqueView> implements UniqueViewService {

    @Autowired
    private UniqueViewMapper uniqueViewMapper;

    @Autowired
    private RedisService redisService;

    @Override
    public List<UniqueViewVO> listUniqueView() {
        DateTime startTime = DateUtil.beginOfDay(DateUtil.offsetDay(new Date(), -7));
        DateTime endTime = DateUtil.endOfDay(new Date());
        return uniqueViewMapper.selectUniqueView(startTime, endTime);
    }

    /**
     * 每天凌晨零点执行
     */
    @Scheduled(cron = " 0 0 0 * * ?", zone = "Asia/Shanghai")
    public void saveUniqueView() {
        // 获取每天用户量
        Long count = redisService.getSetSize(UNIQUE_VISITOR);
        // 获取昨天日期插入数据
        UniqueView uniqueView = UniqueView.builder()
                .createTime(LocalDateTimeUtil.offset(LocalDateTime.now(ZoneId.of(SHANGHAI.getZone())), -1, ChronoUnit.DAYS))
                .viewCount(Optional.of(count.intValue()).orElse(0))
                .build();
        uniqueViewMapper.insert(uniqueView);
    }

    /**
     * 每天凌晨零点1分执行
     */
    @Scheduled(cron = " 0 1 0 * * ?", zone = "Asia/Shanghai")
    public void clear() {
        // 清空redis访客记录
        redisService.deleteObject(UNIQUE_VISITOR);
        // 清空redis游客区域统计
        redisService.deleteObject(VISITOR_ZONE);
    }
}
