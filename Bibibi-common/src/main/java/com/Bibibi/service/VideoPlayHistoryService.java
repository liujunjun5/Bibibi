package com.Bibibi.service;

import com.Bibibi.entity.po.VideoPlayHistory;
import com.Bibibi.entity.query.VideoPlayHistoryQuery;
import com.Bibibi.entity.vo.PaginationResultVO;

import java.util.List;
/**
 * @Description:视频播放历史Service
 * @date:2024-12-04
 * @author: liujun
 */
public interface VideoPlayHistoryService { 

	/**
	 * 根据条件查询列表
	 */
	List<VideoPlayHistory> findListByParam(VideoPlayHistoryQuery query);

	/**
	 * 根据条件查询数量
	 */
	Integer findCountByParam(VideoPlayHistoryQuery query);

	/**
	 * 分页查询
	 */
	PaginationResultVO<VideoPlayHistory> findByPage(VideoPlayHistoryQuery query);

	/**
	 * 新增
	 */
	Integer add(VideoPlayHistory bean);

	/**
	 * 批量新增
	 */
	Integer addBatch(List<VideoPlayHistory> listBean);

	/**
	 * 批量新增或更新
	 */
	Integer addOrUpdateBatch(List<VideoPlayHistory> listBean);


	/**
	 * 根据UserIdAndVideoId查询
	 */
	VideoPlayHistory getByUserIdAndVideoId(String userId, String videoId);

	/**
	 * 根据UserIdAndVideoId更新
	 */
	Integer updateByUserIdAndVideoId(VideoPlayHistory bean, String userId, String videoId);

	/**
	 * 根据UserIdAndVideoId删除
	 */
	Integer deleteByUserIdAndVideoId(String userId, String videoId);

	/**
	 * 保存历史记录
	 */
	void saveHistory(String userId, String videoId, Integer fileIndex);

	void deleteByParam(VideoPlayHistoryQuery historyQuery);
}