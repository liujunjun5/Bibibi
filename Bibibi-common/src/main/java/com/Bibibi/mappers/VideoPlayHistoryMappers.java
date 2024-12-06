package com.Bibibi.mappers;

import org.apache.ibatis.annotations.Param;

/**
 * @Description:视频播放历史Mapper
 * @date:2024-12-04
 * @author: liujun
 */
public interface VideoPlayHistoryMappers<T, P> extends BaseMapper {

	/**
	 * 根据UserIdAndVideoId查询
	 */
	T selectByUserIdAndVideoId(@Param("userId") String userId, @Param("videoId") String videoId);

	/**
	 * 根据UserIdAndVideoId更新
	 */
	Integer updateByUserIdAndVideoId(@Param("bean") T t, @Param("userId") String userId, @Param("videoId") String videoId);

	/**
	 * 根据UserIdAndVideoId删除
	 */
	Integer deleteByUserIdAndVideoId(@Param("userId") String userId, @Param("videoId") String videoId);

}