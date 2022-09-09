package com.ican.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ican.entity.Comment;
import com.ican.model.dto.CheckDTO;
import com.ican.model.dto.CommentDTO;
import com.ican.model.dto.CommentDeleteDTO;
import com.ican.model.dto.ConditionDTO;
import com.ican.model.vo.*;

import java.util.List;


/**
 * 评论业务接口
 *
 * @author ican
 */
public interface CommentService extends IService<Comment> {

    /**
     * 查询后台评论列表
     *
     * @param condition 条件
     * @return 后台评论列表
     */
    PageResult<CommentBackVO> listCommentBackVO(ConditionDTO condition);

    /**
     * 查询评论
     *
     * @param comment 评论信息
     * @return 评论列表
     */
    PageResult<CommentVO> listCommentVO(CommentDTO comment);

    /**
     * 添加评论
     *
     * @param comment 评论信息
     */
    void saveComment(CommentDTO comment);

    /**
     * 审核评论
     *
     * @param check 审核信息
     */
    void updateCommentsCheck(CheckDTO check);

    /**
     * 删除评论
     *
     * @param commentDelete 评论
     */
    void deleteCommentByIds(CommentDeleteDTO commentDelete);

    /**
     * 查询评论总数量
     *
     * @param commentDTO 评论信息
     * @return 评论总数量
     */
    Integer listCommentCount(CommentDTO commentDTO);

    /**
     * 查询最新评论
     *
     * @return 最新评论列表
     */
    List<LastCommentVO> listLastCommentVO();

    /**
     * 查询评论下的回复评论
     *
     * @param commentId 评论id
     * @return 回复评论列表
     */
    List<ReplyVO> listRepliesByCommentId(Integer commentId);

}
