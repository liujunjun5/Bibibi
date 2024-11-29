package com.Bibibi.service;

import com.Bibibi.entity.po.VideoComment;
import com.Bibibi.entity.query.VideoCommentQuery;
import com.Bibibi.entity.vo.PaginationResultVO;
import com.Bibibi.exception.BusinessException;

import java.util.List;

/**
 * @Description:评论Service
 * @date:2024-11-20
 * @author: liujun
 */
public interface VideoCommentService {

    /**
     * 根据条件查询列表
     */
    List<VideoComment> findListByParam(VideoCommentQuery query);

    /**
     * 根据条件查询数量
     */
    Integer findCountByParam(VideoCommentQuery query);

    /**
     * 分页查询
     */
    PaginationResultVO<VideoComment> findByPage(VideoCommentQuery query);

    /**
     * 新增
     */
    Integer add(VideoComment bean);

    /**
     * 批量新增
     */
    Integer addBatch(List<VideoComment> listBean);

    /**
     * 批量新增或更新
     */
    Integer addOrUpdateBatch(List<VideoComment> listBean);


    /**
     * 根据CommentId查询
     */
    VideoComment getByCommentId(Integer commentId);

    /**
     * 根据CommentId更新
     */
    Integer updateByCommentId(VideoComment bean, Integer commentId);

    /**
     * 根据CommentId删除
     */
    Integer deleteByCommentId(Integer commentId);

    void postComment(VideoComment comment, Integer replyCommentId) throws BusinessException;

    void topComment(Integer commentId, String userId) throws BusinessException;

    void cancelTopComment(Integer commentId, String userId) throws BusinessException;

    void deleteComment(Integer commentId, String userId) throws BusinessException;
}