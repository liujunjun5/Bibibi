package com.Bibibi.service;

import com.Bibibi.entity.po.UserVideoSeries;
import com.Bibibi.entity.query.UserVideoSeriesQuery;
import com.Bibibi.entity.vo.PaginationResultVO;

import java.util.List;
/**
 * @Description:用户视频序列归档Service
 * @date:2024-11-29
 * @author: liujun
 */
public interface UserVideoSeriesService { 

	/**
	 * 根据条件查询列表
	 */
	List<UserVideoSeries> findListByParam(UserVideoSeriesQuery query);

	/**
	 * 根据条件查询数量
	 */
	Integer findCountByParam(UserVideoSeriesQuery query);

	/**
	 * 分页查询
	 */
	PaginationResultVO<UserVideoSeries> findByPage(UserVideoSeriesQuery query);

	/**
	 * 新增
	 */
	Integer add(UserVideoSeries bean);

	/**
	 * 批量新增
	 */
	Integer addBatch(List<UserVideoSeries> listBean);

	/**
	 * 批量新增或更新
	 */
	Integer addOrUpdateBatch(List<UserVideoSeries> listBean);


	/**
	 * 根据SeriesId查询
	 */
	UserVideoSeries getBySeriesId(Integer seriesId);

	/**
	 * 根据SeriesId更新
	 */
	Integer updateBySeriesId(UserVideoSeries bean, Integer seriesId);

	/**
	 * 根据SeriesId删除
	 */
	Integer deleteBySeriesId(Integer seriesId);

}