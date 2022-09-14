package com.ican.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ican.entity.Comment;
import com.ican.model.dto.CommentDTO;
import com.ican.model.dto.ConditionDTO;
import com.ican.model.vo.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 评论Mapper
 *
 * @author ican
 */
@Repository
public interface CommentMapper extends BaseMapper<Comment> {

    /**
     * 查询评论数量
     *
     * @param condition 条件
     * @return 评论数量
     */
    Integer countComment(@Param("condition") ConditionDTO condition);

    /**
     * 查询后台评论列表
     *
     * @param limit     分页
     * @param size      大小
     * @param condition 条件
     * @return 评论集合
     */
    List<CommentBackVO> listCommentBackVO(@Param("limit") Long limit, @Param("size") Long size, @Param("condition") ConditionDTO condition);

    /**
     * 根据父评论id查询子评论id
     *
     * @param parentId 父评论id
     * @return 子评论id列表
     */
    List<Integer> selectIdByParentId(@Param("parentId") Integer parentId);

    /**
     * 分页查询父评论
     *
     * @param limit      分页
     * @param size       大小
     * @param commentDTO 评论信息
     * @return 评论集合
     */
    List<CommentVO> selectParentComment(@Param("limit") Long limit, @Param("size") Long size, @Param("commentDTO") CommentDTO commentDTO);

    /**
     * 查询每条父评论下的子评论，取前三条
     *
     * @param parentCommentIdList 父评论id集合
     * @return 回复集合
     */
    List<ReplyVO> selectRepliesByParentIdList(@Param("parentCommentIdList") List<Integer> parentCommentIdList);

    /**
     * 根据父评论id查询回复数量
     *
     * @param parentCommentIdList 父评论id列表
     * @return 回复数量
     */
    List<ReplyCountVO> selectReplyCountByParentId(@Param("parentCommentIdList") List<Integer> parentCommentIdList);

    /**
     * 分页查询父评论下的子评论
     *
     * @param limit     页码
     * @param size      大小
     * @param commentId 父评论id
     * @return 回复评论集合
     */
    List<ReplyVO> selectRepliesByParentId(@Param("limit") Long limit, @Param("size") Long size, @Param("commentId") Integer commentId);

    /**
     * 查询评论id列表
     *
     * @param commentType 评论类型
     * @param typeId      类型id
     * @return 评论id列表
     */
    List<Integer> selectCommentId(@Param("commentType") Integer commentType, @Param("typeId") Integer typeId);

    /**
     * 根据评论类型id获取评论量
     *
     * @param typeIdList  类型id列表
     * @param commentType 评论类型
     * @return {@link List<CommentCountVO>}说说评论量
     */
    List<CommentCountVO> selectCommentCountByTypeId(@Param("typeIdList") List<Integer> typeIdList, @Param("commentType") Integer commentType);

    /**
     * 查询最新评论
     *
     * @return 最新评论列表
     */
    List<LastCommentVO> selectRecentComment();
}
