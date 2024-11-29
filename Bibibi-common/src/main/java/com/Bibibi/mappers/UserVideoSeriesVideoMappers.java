package com.Bibibi.mappers;

import org.apache.ibatis.annotations.Param;

/**
 * @Description:Mapper
 * @date:2024-11-29
 * @author: liujun
 */
public interface UserVideoSeriesVideoMappers<T, P> extends BaseMapper {

	/**
	 * 根据SeriesIdAndVideoId查询
	 */
	T selectBySeriesIdAndVideoId(@Param("seriesId") Integer seriesId, @Param("videoId") String videoId);

	/**
	 * 根据SeriesIdAndVideoId更新
	 */
	Integer updateBySeriesIdAndVideoId(@Param("bean") T t, @Param("seriesId") Integer seriesId, @Param("videoId") String videoId);

	/**
	 * 根据SeriesIdAndVideoId删除
	 */
	Integer deleteBySeriesIdAndVideoId(@Param("seriesId") Integer seriesId, @Param("videoId") String videoId);

}