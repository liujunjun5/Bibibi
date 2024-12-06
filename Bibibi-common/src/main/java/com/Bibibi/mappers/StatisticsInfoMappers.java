package com.Bibibi.mappers;

import org.apache.ibatis.annotations.Param;

/**
 * @Description:数据统计Mapper
 * @date:2024-12-04
 * @author: liujun
 */
public interface StatisticsInfoMappers<T, P> extends BaseMapper {

	/**
	 * 根据StatisticsDateAndUserIdAndDataType查询
	 */
	T selectByStatisticsDateAndUserIdAndDataType(@Param("statisticsDate") String statisticsDate, @Param("userId") String userId, @Param("dataType") Integer dataType);

	/**
	 * 根据StatisticsDateAndUserIdAndDataType更新
	 */
	Integer updateByStatisticsDateAndUserIdAndDataType(@Param("bean") T t, @Param("statisticsDate") String statisticsDate, @Param("userId") String userId, @Param("dataType") Integer dataType);

	/**
	 * 根据StatisticsDateAndUserIdAndDataType删除
	 */
	Integer deleteByStatisticsDateAndUserIdAndDataType(@Param("statisticsDate") String statisticsDate, @Param("userId") String userId, @Param("dataType") Integer dataType);

}