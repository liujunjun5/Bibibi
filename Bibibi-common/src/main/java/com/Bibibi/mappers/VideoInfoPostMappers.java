package com.Bibibi.mappers;

import org.apache.ibatis.annotations.Param;

/**
 * @Description:视频信息Mapper
 * @date:2024-11-11
 * @author: liujun
 */
public interface VideoInfoPostMappers<T, P> extends BaseMapper {

	/**
	 * 根据VideoId查询
	 */
	T selectByVideoId(@Param("videoId") String videoId);

	/**
	 * 根据VideoId更新
	 */
	Integer updateByVideoId(@Param("bean") T t, @Param("videoId") String videoId);

	/**
	 * 根据VideoId删除
	 */
	Integer deleteByVideoId(@Param("videoId") String videoId);

}