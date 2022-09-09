package com.ican.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ican.entity.Article;
import com.ican.entity.Comment;
import com.ican.entity.Talk;
import com.ican.entity.User;
import com.ican.exception.ServiceException;
import com.ican.mapper.ArticleMapper;
import com.ican.mapper.CommentMapper;
import com.ican.mapper.TalkMapper;
import com.ican.mapper.UserMapper;
import com.ican.model.dto.*;
import com.ican.model.vo.*;
import com.ican.service.CommentService;
import com.ican.service.RedisService;
import com.ican.service.SiteSettingService;
import com.ican.utils.HTMLUtils;
import com.ican.utils.PageUtils;
import com.ican.utils.SecurityUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static com.ican.constant.CommonConstant.*;
import static com.ican.constant.MqConstant.EMAIL_EXCHANGE;
import static com.ican.constant.RedisConstant.*;
import static com.ican.enums.CommentTypeEnum.*;
import static com.ican.enums.StatusCodeEnum.VALID_ERROR;

/**
 * 评论业务接口实现类
 *
 * @author ican
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Value("${blog.url}")
    private String websiteUrl;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private SiteSettingService siteSettingService;

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private TalkMapper talkMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RedisService redisService;

    @Override
    public PageResult<CommentBackVO> listCommentBackVO(ConditionDTO condition) {
        // 查询后台评论数量
        Integer count = commentMapper.countComment(condition);
        if (count == 0) {
            return new PageResult<>();
        }
        // 查询后台评论集合
        List<CommentBackVO> commentBackDTOList = commentMapper.listCommentBackVO(PageUtils.getLimit(), PageUtils.getSize(), condition);
        return new PageResult<>(commentBackDTOList, count);
    }

    @Override
    public PageResult<CommentVO> listCommentVO(CommentDTO commentDTO) {
        // 查询父评论数量
        Integer count = commentMapper.selectCount(new LambdaQueryWrapper<Comment>().eq(Objects.nonNull(commentDTO.getTypeId()), Comment::getTypeId, commentDTO.getTypeId())
                .eq(Comment::getCommentType, commentDTO.getCommentType())
                .eq(Comment::getIsCheck, TRUE)
                .isNull(Comment::getParentId));
        if (count == 0) {
            return new PageResult<>();
        }
        // 分页查询父评论
        List<CommentVO> commentVOList = commentMapper.selectParentComment(PageUtils.getLimit(), PageUtils.getSize(), commentDTO);
        if (CollectionUtils.isEmpty(commentVOList)) {
            return new PageResult<>();
        }
        // 评论点赞数据
        Map<String, Integer> likeCountMap = redisService.getHashAll(COMMENT_LIKE_COUNT);
        // 父评论id集合
        List<Integer> parentCommentIdList = commentVOList.stream().map(CommentVO::getId).collect(Collectors.toList());
        // 分组查询每组父评论下的子评论前三条
        List<ReplyVO> replyVOList = commentMapper.selectRepliesByParentIdList(parentCommentIdList);
        // 封装子评论点赞量
        replyVOList.forEach(item -> item.setLikeCount(likeCountMap.get(item.getId().toString())));
        // 根据父评论id生成对应子评论的Map
        Map<Integer, List<ReplyVO>> replyMap = replyVOList.stream().collect(Collectors.groupingBy(ReplyVO::getParentId));
        // 父评论的回复数量
        List<ReplyCountVO> replyCountList = commentMapper.selectReplyCountByParentId(parentCommentIdList);
        // 转换Map
        Map<Integer, Integer> replyCountMap = replyCountList.stream().collect(Collectors.toMap(ReplyCountVO::getCommentId, ReplyCountVO::getReplyCount));
        // 封装评论数据
        commentVOList.forEach(item -> {
            item.setLikeCount(likeCountMap.get(item.getId().toString()));
            item.setReplyVOList(replyMap.get(item.getId()));
            item.setReplyCount(replyCountMap.get(item.getId()));
        });
        return new PageResult<>(commentVOList, count);
    }

    @Override
    public void saveComment(CommentDTO comment) {
        // 验证评论参数
        judgeComment(comment);
        // 判断评论是否需要审核
        WebsiteDTO siteSetting = siteSettingService.getSiteSetting();
        // 获取评论审核
        Integer isCommentCheck = siteSetting.getIsCommentCheck();
        // 过滤标签
        comment.setCommentContent(HTMLUtils.filter(comment.getCommentContent()));
        // 父评论的
        Comment newComment = Comment.builder().fromUid(SecurityUtils.getUserId()).toUid(comment.getToUid()).typeId(comment.getTypeId()).commentType(comment.getCommentType()).parentId(comment.getParentId()).replyId(comment.getReplyId()).commentContent(comment.getCommentContent()).isCheck(isCommentCheck.equals(FALSE) ? TRUE : FALSE).build();
        // 保存评论
        commentMapper.insert(newComment);
        // 当前用户昵称
        String fromNickName = SecurityUtils.getNickName();
        // 判断是否开启邮箱通知,通知用户
        if (siteSetting.getIsEmailNotice().equals(TRUE)) {
            CompletableFuture.runAsync(() -> notice(newComment, fromNickName));
        }
    }

    @Override
    public void updateCommentsCheck(CheckDTO check) {
        // 修改评论审核状态
        List<Comment> commentList = check.getIdList().stream().map(id -> Comment.builder().id(id).isCheck(check.getIsCheck()).build()).collect(Collectors.toList());
        this.updateBatchById(commentList);
    }

    @Override
    public void deleteCommentByIds(CommentDeleteDTO commentDelete) {
        // 删除当前评论
        commentMapper.deleteById(commentDelete.getId());
        // 删除当前评论的点赞量
        redisService.deleteHash(COMMENT_LIKE_COUNT, commentDelete.getId().toString());
        // 所有评论点赞用户id
        List<Integer> userIdList = redisService.getKeys(USER_COMMENT_LIKE + "*").stream().map(item -> {
            String[] split = item.split(":");
            return Integer.parseInt(split[1]);
        }).collect(Collectors.toList());
        // 删除当前评论的用户点赞记录
        userIdList.forEach(userId -> redisService.deleteSet(USER_COMMENT_LIKE + userId, commentDelete.getId()));
        // 当前评论是父评论则删除子评论
        if (commentDelete.getIsParent().equals(TRUE)) {
            // 查询子评论id列表
            List<Integer> childrenIdList = commentMapper.selectIdByParentId(commentDelete.getId());
            // 子评论不为空
            if (CollectionUtils.isNotEmpty(childrenIdList)) {
                // 删除子评论
                commentMapper.deleteBatchIds(childrenIdList);
                // 删除子评论的点赞量
                childrenIdList.forEach(id -> redisService.deleteHash(COMMENT_LIKE_COUNT, id.toString()));
                // 删除子评论的用户点赞记录
                Integer[] idList = childrenIdList.toArray(new Integer[0]);
                userIdList.forEach(userId -> redisService.deleteSet(USER_COMMENT_LIKE + userId, idList));
            }
        }
    }

    @Override
    public Integer listCommentCount(CommentDTO commentDTO) {
        // 查询评论数量
        return commentMapper.selectCount(new LambdaQueryWrapper<Comment>().eq(Objects.nonNull(commentDTO.getTypeId()), Comment::getTypeId, commentDTO.getTypeId()).eq(Comment::getCommentType, commentDTO.getCommentType()).eq(Comment::getIsCheck, TRUE));
    }

    @Override
    public List<LastCommentVO> listLastCommentVO() {
        return commentMapper.selectRecentComment();
    }

    @Override
    public List<ReplyVO> listRepliesByCommentId(Integer commentId) {
        // 分页查询子评论
        List<ReplyVO> replyVOList = commentMapper.selectRepliesByParentId(PageUtils.getLimit(), PageUtils.getSize(), commentId);
        // 子评论点赞Map
        Map<String, Integer> likeCountMap = redisService.getHashAll(COMMENT_LIKE_COUNT);
        replyVOList.forEach(item -> item.setLikeCount(likeCountMap.get(item.getId().toString())));
        return replyVOList;
    }

    /**
     * 判断提交的评论参数是否合法
     *
     * @param comment 评论信息
     */
    private void judgeComment(CommentDTO comment) {
        // 类型为文章和说说时，类型id不能为空，且判断文章或说说是否存在
        if (Objects.requireNonNull(getCommentEnum(comment.getCommentType())) == ARTICLE || Objects.requireNonNull(getCommentEnum(comment.getCommentType())) == TALK) {
            // 类型id为空则报异常
            if (Objects.isNull(comment.getTypeId())) {
                throw new ServiceException(VALID_ERROR.getCode(), "评论类型ID不能为空");
            } else {
                // 类型id不为空判断文章是否存在
                if (Objects.requireNonNull(getCommentEnum(comment.getCommentType())) == ARTICLE) {
                    Article article = articleMapper.selectOne(new LambdaQueryWrapper<Article>().select(Article::getId, Article::getUserId).eq(Article::getId, comment.getTypeId()));
                    if (Objects.isNull(article)) {
                        throw new ServiceException("文章不存在");
                    }
                }
                // 类型id不为空判断说说是否存在
                if (Objects.requireNonNull(getCommentEnum(comment.getCommentType())) == TALK) {
                    Talk talk = talkMapper.selectOne(new LambdaQueryWrapper<Talk>().select(Talk::getId, Talk::getUserId).eq(Talk::getId, comment.getTypeId()));
                    if (Objects.isNull(talk)) {
                        throw new ServiceException("说说不存在");
                    }
                }
            }
        }
        // 类型为友链时，类型必须为空
        if (Objects.requireNonNull(getCommentEnum(comment.getCommentType())) == LINK) {
            if (Objects.nonNull(comment.getTypeId())) {
                throw new ServiceException(VALID_ERROR.getCode(), "评论类型必须为空");
            }
        }
        // 父评论时reply_id、to_uid必须都为空
        if (Objects.isNull(comment.getParentId())) {
            if (Objects.nonNull(comment.getReplyId()) && Objects.nonNull(comment.getToUid())) {
                throw new ServiceException(VALID_ERROR.getCode(), "reply_id、to_uid必须都为空");
            }
        }
        // 评论为子评论，评论的reply_id、to_uid字段都不能为空，判断回复的评论和用户是否存在
        if (Objects.nonNull(comment.getParentId())) {
            // 判断父评论是否存在
            Comment parentComment = commentMapper.selectOne(new LambdaQueryWrapper<Comment>().select(Comment::getId, Comment::getParentId, Comment::getCommentType).eq(Comment::getId, comment.getParentId()));
            if (Objects.isNull(parentComment)) {
                throw new ServiceException("父评论不存在");
            }
            if (Objects.nonNull(parentComment.getParentId())) {
                throw new ServiceException(VALID_ERROR.getCode(), "当前评论为子评论，不能作为父评论");
            }
            if (!comment.getCommentType().equals(parentComment.getCommentType())) {
                throw new ServiceException("只能以同类型的评论作为父评论");
            }
            if (Objects.isNull(comment.getReplyId()) || Objects.isNull(comment.getToUid())) {
                throw new ServiceException(VALID_ERROR.getCode(), "回复评论id和回复用户id不能为空");
            } else {
                // 判断回复的评论和用户是否存在
                Comment existComment = commentMapper.selectOne(new LambdaQueryWrapper<Comment>().select(Comment::getId, Comment::getParentId, Comment::getFromUid, Comment::getCommentType).eq(Comment::getId, comment.getReplyId()));
                User existUser = userMapper.selectOne(new LambdaQueryWrapper<User>().select(User::getId).eq(User::getId, comment.getToUid()));
                if (Objects.isNull(existComment) || Objects.isNull(existUser)) {
                    throw new ServiceException("回复的评论或用户不存在");
                }
                if (!comment.getCommentType().equals(existComment.getCommentType())) {
                    throw new ServiceException("只能回复同类型的下的评论");
                }
                if (!Objects.isNull(existComment.getParentId())) {
                    if (!existComment.getParentId().equals(comment.getParentId())) {
                        // 回复的是父级评论通过
                        throw new ServiceException(VALID_ERROR.getCode(), "提交的评论parentId与当前回复评论parentId不一致");
                    }
                }
                if (!existComment.getFromUid().equals(comment.getToUid())) {
                    throw new ServiceException(VALID_ERROR.getCode(), "提交的评论toUid与当前回复评论fromUid不一致");
                }
                // 只能回复当前父评论及其子评论
                List<Integer> replyIdList = commentMapper.selectIdByParentId(comment.getParentId());
                replyIdList.add(comment.getParentId());
                if (!replyIdList.contains(comment.getReplyId())) {
                    throw new ServiceException("当前父评论下不存在该子评论");
                }
            }
        }
    }

    /**
     * 邮箱通知
     *
     * @param comment      评论
     * @param fromNickName 当前用户昵称
     */
    private void notice(Comment comment, String fromNickName) {
        // 用户id
        Integer userId = BLOGGER_ID;
        // 作者id
        Integer authorId = BLOGGER_ID;
        //  自己回复自己不用提醒
        if (comment.getFromUid().equals(comment.getToUid())) {
            return;
        }
        // 博主提交父评论不用提醒
        if ((comment.getFromUid().equals(BLOGGER_ID) && Objects.isNull(comment.getParentId()))) {
            return;
        }
        // 评论类型id
        String id = Objects.nonNull(comment.getTypeId()) ? comment.getTypeId().toString() : "";
        // 标题
        String title = "友链";
        // 子评论
        if (Objects.nonNull(comment.getToUid())) {
            userId = comment.getToUid();
            if (Objects.requireNonNull(getCommentEnum(comment.getCommentType())) == ARTICLE) {
                Article article = articleMapper.selectOne(new LambdaQueryWrapper<Article>().select(Article::getUserId, Article::getArticleTitle).eq(Article::getId, comment.getTypeId()));
                title = article.getArticleTitle();
                authorId = article.getUserId();
            }
            if (Objects.requireNonNull(getCommentEnum(comment.getCommentType())) == TALK) {
                Talk talk = talkMapper.selectOne(new LambdaQueryWrapper<Talk>().select(Talk::getUserId).eq(Talk::getId, comment.getTypeId()));
                title = "说说";
                authorId = talk.getUserId();
            }
        } else {
            // 父评论
            if (Objects.requireNonNull(getCommentEnum(comment.getCommentType())) == ARTICLE) {
                Article article = articleMapper.selectOne(new LambdaQueryWrapper<Article>().select(Article::getUserId, Article::getArticleTitle).eq(Article::getId, comment.getTypeId()));
                authorId = article.getUserId();
                userId = article.getUserId();
                title = article.getArticleTitle();
            }
            if (Objects.requireNonNull(getCommentEnum(comment.getCommentType())) == TALK) {
                Talk talk = talkMapper.selectOne(new LambdaQueryWrapper<Talk>().select(Talk::getUserId).eq(Talk::getId, comment.getTypeId()));
                title = "说说";
                authorId = talk.getUserId();
                userId = talk.getUserId();
            }
        }
        // 查询回复用户邮箱号、昵称
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().select(User::getEmail, User::getNickname).eq(User::getId, userId));
        if (StringUtils.hasText(user.getEmail())) {
            MailDTO mailDTO = getMailDTO(comment, authorId, id, title, fromNickName, user);
            rabbitTemplate.convertAndSend(EMAIL_EXCHANGE, "*", JSON.toJSONBytes(mailDTO));
        }

    }

    /**
     * 获取MailDTO内容
     *
     * @param comment      评论
     * @param authorId     作者id
     * @param id           评论类型id
     * @param title        标题
     * @param fromNickName 评论用户昵称
     * @param user         用户
     * @return {@link MailDTO}
     */
    private MailDTO getMailDTO(Comment comment, Integer authorId, String id, String title, String fromNickName, User user) {
        MailDTO mailDTO = new MailDTO();
        if (comment.getIsCheck().equals(TRUE)) {
            Map<String, Object> map = new HashMap<>(16);
            // 评论链接
            String url = websiteUrl + getCommentPath(comment.getCommentType()) + id;
            // 回复的是作者或评论为父评论则提醒作者
            if (Objects.isNull(comment.getParentId()) || comment.getToUid().equals(authorId)) {
                mailDTO.setToEmail(user.getEmail());
                mailDTO.setSubject("评论提醒");
                mailDTO.setTemplate("author.html");
                String createTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").format(comment.getCreateTime());
                map.put("time", createTime);
                map.put("url", url);
                map.put("title", title);
                map.put("nickname", fromNickName);
                map.put("content", comment.getCommentContent());
                mailDTO.setCommentMap(map);
            } else {
                // 回复的是用户提醒该用户
                Comment parentComment = commentMapper.selectOne(new LambdaQueryWrapper<Comment>().select(Comment::getCommentContent, Comment::getCreateTime).eq(Comment::getId, comment.getReplyId()));
                mailDTO.setToEmail(user.getEmail());
                mailDTO.setSubject("评论提醒");
                mailDTO.setTemplate("user.html");
                map.put("url", url);
                map.put("title", title);
                String createTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").format(parentComment.getCreateTime());
                map.put("time", createTime);
                // 被回复用户昵称
                map.put("toUser", user.getNickname());
                // 评论用户昵称
                map.put("fromUser", fromNickName);
                // 被回复的评论内容
                map.put("parentComment", parentComment.getCommentContent());
                // 回复评论内容
                map.put("replyComment", comment.getCommentContent());
                mailDTO.setCommentMap(map);
            }
        } else {
            // 审核提醒
            String adminEmail = userMapper.selectOne(new LambdaQueryWrapper<User>().select(User::getEmail).eq(User::getId, authorId)).getEmail();
            mailDTO.setToEmail(adminEmail);
            mailDTO.setSubject("审核提醒");
            mailDTO.setContent("您收到一条新的回复，请前往后台管理页面审核");
        }
        return mailDTO;
    }
}
