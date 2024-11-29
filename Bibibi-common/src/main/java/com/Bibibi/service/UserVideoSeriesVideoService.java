package com.Bibibi.service;

import com.Bibibi.entity.po.UserVideoSeriesVideo;
import com.Bibibi.entity.query.UserVideoSeriesVideoQuery;
import com.Bibibi.entity.vo.PaginationResultVO;

import java.util.List;
/**
 * @Description:Service
 * @date:2024-11-29
 * @author: liujun
 */
public interface UserVideoSeriesVideoService { 

	/**
	 * 根据条件查询列表
	 */
	List<UserVideoSeriesVideo> findListByParam(UserVideoSeriesVideoQuery query);

	/**
	 * 根据条件查询数量
	 */
	Integer findCountByParam(UserVideoSeriesVideoQuery query);

	/**
	 * 分页查询
	 */
	PaginationResultVO<UserVideoSeriesVideo> findByPage(UserVideoSeriesVideoQuery query);

	/**
	 * 新增
	 */
	Integer add(UserVideoSeriesVideo bean);

	/**
	 * 批量新增
	 */
	Integer addBatch(List<UserVideoSeriesVideo> listBean);

	/**
	 * 批量新增或更新
	 */
	Integer addOrUpdateBatch(List<UserVideoSeriesVideo> listBean);


	/**
	 * 根据SeriesIdAndVideoId查询
	 */
	UserVideoSeriesVideo getBySeriesIdAndVideoId(Integer seriesId, String videoId);

	/**
	 * 根据SeriesIdAndVideoId更新
	 */
	Integer updateBySeriesIdAndVideoId(UserVideoSeriesVideo bean, Integer seriesId, String videoId);

	/**
	 * 根据SeriesIdAndVideoId删除
	 */
	Integer deleteBySeriesIdAndVideoId(Integer seriesId, String videoId);

}