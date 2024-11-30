package com.Bibibi.mappers;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description:用户视频序列归档Mapper
 * @date:2024-11-29
 * @author: liujun
 */
public interface UserVideoSeriesMappers<T, P> extends BaseMapper {

	/**
	 * 根据SeriesId查询
	 */
	T selectBySeriesId(@Param("seriesId") Integer seriesId);

	/**
	 * 根据SeriesId更新
	 */
	Integer updateBySeriesId(@Param("bean") T t, @Param("seriesId") Integer seriesId);

	/**
	 * 根据SeriesId删除
	 */
	Integer deleteBySeriesId(@Param("seriesId") Integer seriesId);

	List<T> selectUserAllSeries(@Param("userId") String userId);

	Integer selectMaxSort(@Param("userId") String userId);

	void changeSort(@Param("videoSeriesList") List<T> videoSeriesList);

	List<T> selectListWithVideoList(@Param("query") P p);

}