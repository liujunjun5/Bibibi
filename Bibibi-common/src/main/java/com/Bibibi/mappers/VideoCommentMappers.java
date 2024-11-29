package com.Bibibi.mappers;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description:评论Mapper
 * @date:2024-11-20
 * @author: liujun
 */
public interface VideoCommentMappers<T, P> extends BaseMapper {

    /**
     * 根据CommentId查询
     */
    T selectByCommentId(@Param("commentId") Integer commentId);

    /**
     * 根据CommentId更新
     */
    Integer updateByCommentId(@Param("bean") T t, @Param("commentId") Integer commentId);

    /**
     * 根据CommentId删除
     */
    Integer deleteByCommentId(@Param("commentId") Integer commentId);

    List<T> selectListWithChildren(@Param("query") P p);

    void updateCountInfo(@Param("commentId") Integer commentId,
                         @Param("field") String field, @Param("changeCount") Integer changeCount,
                         @Param("opposeField") String opposeField, @Param("opposeChangeCount") Integer opposeChangeCount);
}