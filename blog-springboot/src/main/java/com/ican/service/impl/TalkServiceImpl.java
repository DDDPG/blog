package com.ican.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ican.entity.Talk;
import com.ican.exception.ServiceException;
import com.ican.mapper.CommentMapper;
import com.ican.mapper.TalkMapper;
import com.ican.model.dto.ConditionDTO;
import com.ican.model.dto.TalkDTO;
import com.ican.model.vo.CommentCountVO;
import com.ican.model.vo.PageResult;
import com.ican.model.vo.TalkBackVO;
import com.ican.model.vo.TalkVO;
import com.ican.service.RedisService;
import com.ican.service.TalkService;
import com.ican.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.ican.constant.CommonConstant.FALSE;
import static com.ican.constant.CommonConstant.TRUE;
import static com.ican.constant.RedisConstant.*;
import static com.ican.enums.ArticleStatusEnum.PUBLIC;
import static com.ican.enums.CommentTypeEnum.TALK;
import static com.ican.enums.ZoneEnum.SHANGHAI;

/**
 * 说说业务接口实现类
 *
 * @author ican
 */
@Service
public class TalkServiceImpl extends ServiceImpl<TalkMapper, Talk> implements TalkService {

    @Autowired
    private TalkMapper talkMapper;

    @Autowired
    private RedisService redisService;

    @Autowired
    private CommentMapper commentMapper;

    @Override
    public void saveOrUpdateTalk(TalkDTO talk) {
        Talk newTalk = BeanCopyUtils.copyBean(talk, Talk.class);
        // 发布时间
        LocalDateTime createTime = null;
        if (talk.getIsTime().equals(TRUE)) {
            createTime = talk.getCreateTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            newTalk.setCreateTime(createTime);
        }
        newTalk.setUserId(SecurityUtils.getUserId());
        this.saveOrUpdate(newTalk);
        // 定时发布
        if (talk.getIsTime().equals(TRUE)) {
            // 当前时间
            LocalDateTime currentTime = LocalDateTime.now(ZoneId.of(SHANGHAI.getZone()));
            long expireTime = Duration.between(currentTime, createTime).toMillis();
            redisService.setObject(TALK_KEY + newTalk.getId(), createTime, expireTime, TimeUnit.MILLISECONDS);
        }
    }

    @Override
    public void deleteTalk(Integer talkId) {
        // 谨慎删除文章、评论、说说
        // 删除说说
        talkMapper.deleteById(talkId);
        // 删除说说的定时发布
        redisService.deleteObject(TALK_KEY + talkId);
        // 删除说说点赞量
        redisService.deleteHash(TALK_LIKE_COUNT, talkId.toString());
        // 所有说说点赞用户id
        List<Integer> userIdList = redisService.getKeys(USER_TALK_LIKE + "*").stream()
                .map(item -> {
                    String[] split = item.split(":");
                    return Integer.parseInt(split[1]);
                })
                .collect(Collectors.toList());
        // 删除说说用户点赞记录
        userIdList.forEach(userId -> redisService.deleteSet(USER_TALK_LIKE + userId, talkId));
        // 删除说说下的评论
        List<Integer> commentIdList = commentMapper.selectCommentId(TALK.getType(), talkId);
        if (CollectionUtils.isNotEmpty(commentIdList)) {
            // 删除子评论
            commentMapper.deleteBatchIds(commentIdList);
            // 删除子评论的点赞量
            commentIdList.forEach(id -> redisService.deleteHash(COMMENT_LIKE_COUNT, id.toString()));
            Integer[] idList = commentIdList.toArray(new Integer[0]);
            // 所有评论点赞用户id
            List<Integer> commentUserIdList = redisService.getKeys(USER_COMMENT_LIKE + "*").stream()
                    .map(item -> {
                        String[] split = item.split(":");
                        return Integer.parseInt(split[1]);
                    })
                    .collect(Collectors.toList());
            // 删除评论的用户点赞记录
            commentUserIdList.forEach(userId -> redisService.deleteSet(USER_COMMENT_LIKE + userId, idList));
        }

    }

    @Override
    public PageResult<TalkBackVO> listTalkBackVO(ConditionDTO condition) {
        // 查询说说总量
        Integer count = talkMapper.selectCount(new LambdaQueryWrapper<Talk>()
                .eq(Objects.nonNull(condition.getStatus()), Talk::getStatus, condition.getStatus()));
        if (count == 0) {
            return new PageResult<>();
        }
        // 分页查询说说
        List<TalkBackVO> talkBackVOList = talkMapper.selectTalkBackList(PageUtils.getLimit(), PageUtils.getSize(), condition);
        talkBackVOList.forEach(item -> {
            // 转换图片格式
            if (Objects.nonNull(item.getImages())) {
                item.setImgList(CommonUtils.castList(JSON.parseObject(item.getImages(), List.class), String.class));
            }
        });
        return new PageResult<>(talkBackVOList, count);
    }

    @Override
    public TalkBackVO getBackTalkById(Integer talkId) {
        TalkBackVO talkBackVO = talkMapper.selectTalkBackById(talkId);
        // 转换图片格式
        if (Objects.nonNull(talkBackVO.getImages())) {
            talkBackVO.setImgList(CommonUtils.castList(JSON.parseObject(talkBackVO.getImages(), List.class), String.class));
        }
        return talkBackVO;

    }

    @Override
    public List<String> listTalkHome() {
        // 查询最新10条说说
        List<Talk> talkList = talkMapper.selectList(new LambdaQueryWrapper<Talk>()
                .eq(Talk::getStatus, PUBLIC.getStatus())
                .orderByDesc(Talk::getIsTop)
                .orderByDesc(Talk::getId)
                .last("limit 10"));
        return talkList.stream()
                .map(item -> item.getTalkContent().length() > 200
                        ? HTMLUtils.deleteHtmlTag(item.getTalkContent().substring(0, 200))
                        : HTMLUtils.deleteHtmlTag(item.getTalkContent()))
                .collect(Collectors.toList());
    }

    @Override
    public PageResult<TalkVO> listTalkVO() {
        // 查询说说总量
        Integer count = talkMapper.selectCount((new LambdaQueryWrapper<Talk>()
                .eq(Talk::getStatus, PUBLIC.getStatus()).eq(Talk::getIsTime, FALSE)));
        if (count == 0) {
            return new PageResult<>();
        }
        // 分页查询说说
        List<TalkVO> talkVOList = talkMapper.selectTalkList(PageUtils.getLimit(), PageUtils.getSize());
        // 查询说说评论量
        List<Integer> talkIdList = talkVOList.stream()
                .map(TalkVO::getId)
                .collect(Collectors.toList());
        List<CommentCountVO> commentCountVOList = commentMapper.selectCommentCountByTypeId(talkIdList, TALK.getType());
        Map<Integer, Integer> commentCountMap = commentCountVOList.stream()
                .collect(Collectors.toMap(CommentCountVO::getId, CommentCountVO::getCommentCount));
        // 查询说说点赞量
        Map<String, Integer> likeCountMap = redisService.getHashAll(TALK_LIKE_COUNT);
        // 封装说说
        talkVOList.forEach(item -> {
            item.setLikeCount(likeCountMap.get(item.getId().toString()));
            item.setCommentCount(commentCountMap.get(item.getId()));
            // 转换图片格式
            if (Objects.nonNull(item.getImages())) {
                item.setImgList(CommonUtils.castList(JSON.parseObject(item.getImages(), List.class), String.class));
            }
        });
        return new PageResult<>(talkVOList, count);
    }

    @Override
    public TalkVO getTalkById(Integer talkId) {
        // 查询说说信息
        TalkVO talkVO = talkMapper.selectTalkById(talkId);
        if (Objects.isNull(talkVO)) {
            throw new ServiceException("说说不存在");
        }
        // 查询说说点赞量
        Integer likeCount = redisService.getHash(TALK_LIKE_COUNT, talkId.toString());
        talkVO.setLikeCount(likeCount);
        // 转换图片格式
        if (Objects.nonNull(talkVO.getImages())) {
            talkVO.setImgList(CommonUtils.castList(JSON.parseObject(talkVO.getImages(), List.class), String.class));
        }
        return talkVO;
    }
}
