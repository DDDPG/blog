package com.ican.quartz.task;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ican.entity.UniqueView;
import com.ican.entity.Visitor;
import com.ican.mapper.UniqueViewMapper;
import com.ican.mapper.VisitorMapper;
import com.ican.model.vo.UserZoneVO;
import com.ican.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.ican.constant.CommonConstant.PROVINCE;
import static com.ican.constant.CommonConstant.UNKNOWN;
import static com.ican.constant.RedisConstant.*;
import static com.ican.enums.ZoneEnum.SHANGHAI;

/**
 * 执行定时任务
 *
 * @author ican
 */
@SuppressWarnings(value = "all")
@Component("timedTask")
public class TimedTask {

    @Autowired
    private VisitorMapper visitorMapper;

    @Autowired
    private RedisService redisService;

    @Autowired
    private UniqueViewMapper uniqueViewMapper;

    /**
     * 统计用户地区
     */
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

    /**
     * 统计网站访问量
     */
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
     * 清除redis游客记录和游客区域统计
     */
    public void clear() {
        // 清空redis访客记录
        redisService.deleteObject(UNIQUE_VISITOR);
        // 清空redis游客区域统计
        redisService.deleteObject(VISITOR_ZONE);
    }
}