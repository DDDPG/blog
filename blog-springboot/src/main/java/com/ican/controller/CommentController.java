package com.ican.controller;

import com.ican.annotation.AccessLimit;
import com.ican.annotation.OptLog;
import com.ican.enums.LikeTypeEnum;
import com.ican.model.dto.CheckDTO;
import com.ican.model.dto.CommentDTO;
import com.ican.model.dto.CommentDeleteDTO;
import com.ican.model.dto.ConditionDTO;
import com.ican.model.vo.*;
import com.ican.service.CommentService;
import com.ican.strategy.context.LikeStrategyContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.ican.constant.OptTypeConstant.DELETE;
import static com.ican.constant.OptTypeConstant.UPDATE;

/**
 * 评论控制器
 *
 * @author ican
 */
@Api(tags = "评论模块")
@RestController
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private LikeStrategyContext likeStrategyContext;

    /**
     * 添加评论
     *
     * @param comment 评论信息
     * @return {@link Result<>}
     */
    @ApiOperation(value = "添加评论")
    @PostMapping("/comments")
    public Result<?> saveComment(@Validated @RequestBody CommentDTO comment) {
        commentService.saveComment(comment);
        return Result.success();
    }

    /**
     * 审核评论
     *
     * @param check 审核信息
     * @return {@link Result<>}
     */
    @OptLog(optType = UPDATE)
    @ApiOperation(value = "审核评论")
    @PutMapping("/admin/comments/pass")
    public Result<?> updateCommentsCheck(@Validated @RequestBody CheckDTO check) {
        commentService.updateCommentsCheck(check);
        return Result.success();
    }

    /**
     * 点赞评论
     *
     * @param commentId 评论id
     * @return {@link Result<>}
     */
    @ApiOperation(value = "点赞评论")
    @AccessLimit(seconds = 60, maxCount = 3)
    @PostMapping("/comments/{commentId}/like")
    public Result<?> saveCommentLike(@PathVariable("commentId") Integer commentId) {
        likeStrategyContext.executeLikeStrategy(LikeTypeEnum.COMMENT, commentId);
        return Result.success();
    }

    /**
     * 查看后台评论列表
     *
     * @param condition 条件
     * @return {@link Result<CommentBackVO>} 后台评论
     */
    @ApiOperation(value = "查看后台评论")
    @GetMapping("/admin/comments")
    public Result<PageResult<CommentBackVO>> listCommentBackVO(ConditionDTO condition) {
        return Result.success(commentService.listCommentBackVO(condition));
    }

    /**
     * 删除评论
     *
     * @param commentDelete 评论
     * @return {@link Result<>}
     */
    @OptLog(optType = DELETE)
    @ApiOperation(value = "删除评论")
    @DeleteMapping("/admin/comments")
    public Result<?> deleteComment(@Validated @RequestBody CommentDeleteDTO commentDelete) {
        commentService.deleteCommentByIds(commentDelete);
        return Result.success();
    }

    /**
     * 查询评论总数量
     *
     * @param commentDTO 评论信息
     * @return {@link Result<Integer>} 评论总数量
     */
    @ApiOperation(value = "查询评论总数量")
    @GetMapping("/comments/count")
    public Result<Integer> listCommentCount(CommentDTO commentDTO) {
        return Result.success(commentService.listCommentCount(commentDTO));
    }

    /**
     * 查询最新评论
     *
     * @return {@link List<LastCommentVO>}
     */
    @ApiOperation(value = "查询评论总数量")
    @GetMapping("/recent/comments")
    public Result<List<LastCommentVO>> listLastCommentVO() {
        return Result.success(commentService.listLastCommentVO());
    }

    /**
     * 查询评论
     *
     * @param commentDTO 评论信息
     * @return {@link Result<CommentVO>}
     */
    @ApiOperation(value = "查询评论")
    @GetMapping("/comments")
    public Result<PageResult<CommentVO>> listCommentVO(CommentDTO commentDTO) {
        return Result.success(commentService.listCommentVO(commentDTO));
    }

    /**
     * 查询评论下的回复评论
     *
     * @param commentId 评论id
     * @return {@link Result<ReplyVO>} 回复评论列表
     */
    @ApiOperation(value = "查询评论下的回复评论")
    @GetMapping("/comments/{commentId}/replies")
    public Result<List<ReplyVO>> listRepliesByCommentId(@PathVariable("commentId") Integer commentId) {
        return Result.success(commentService.listRepliesByCommentId(commentId));
    }
}
