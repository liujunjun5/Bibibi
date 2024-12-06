package com.Bibibi.service;

import com.Bibibi.entity.po.StatisticsInfo;
import com.Bibibi.entity.query.StatisticsInfoQuery;
import com.Bibibi.entity.vo.PaginationResultVO;

import java.util.List;
/**
 * @Description:数据统计Service
 * @date:2024-12-04
 * @author: liujun
 */
public interface StatisticsInfoService { 

	/**
	 * 根据条件查询列表
	 */
	List<StatisticsInfo> findListByParam(StatisticsInfoQuery query);

	/**
	 * 根据条件查询数量
	 */
	Integer findCountByParam(StatisticsInfoQuery query);

	/**
	 * 分页查询
	 */
	PaginationResultVO<StatisticsInfo> findByPage(StatisticsInfoQuery query);

	/**
	 * 新增
	 */
	Integer add(StatisticsInfo bean);

	/**
	 * 批量新增
	 */
	Integer addBatch(List<StatisticsInfo> listBean);

	/**
	 * 批量新增或更新
	 */
	Integer addOrUpdateBatch(List<StatisticsInfo> listBean);


	/**
	 * 根据StatisticsDateAndUserIdAndDataType查询
	 */
	StatisticsInfo getByStatisticsDateAndUserIdAndDataType(String statisticsDate, String userId, Integer dataType);

	/**
	 * 根据StatisticsDateAndUserIdAndDataType更新
	 */
	Integer updateByStatisticsDateAndUserIdAndDataType(StatisticsInfo bean, String statisticsDate, String userId, Integer dataType);

	/**
	 * 根据StatisticsDateAndUserIdAndDataType删除
	 */
	Integer deleteByStatisticsDateAndUserIdAndDataType(String statisticsDate, String userId, Integer dataType);

}