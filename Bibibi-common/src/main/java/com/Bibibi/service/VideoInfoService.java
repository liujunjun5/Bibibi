package com.Bibibi.service;

import java.util.Date;

import com.Bibibi.exception.BusinessException;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import com.Bibibi.entity.po.VideoInfo;
import com.Bibibi.entity.query.VideoInfoQuery;
import com.Bibibi.entity.vo.PaginationResultVO;

import java.util.List;
/**
 * @Description:视频信息Service
 * @date:2024-11-11
 * @author: liujun
 */
public interface VideoInfoService { 

	/**
	 * 根据条件查询列表
	 */
	List<VideoInfo> findListByParam(VideoInfoQuery query);

	/**
	 * 根据条件查询数量
	 */
	Integer findCountByParam(VideoInfoQuery query);

	/**
	 * 分页查询
	 */
	PaginationResultVO<VideoInfo> findByPage(VideoInfoQuery query);

	/**
	 * 新增
	 */
	Integer add(VideoInfo bean);

	/**
	 * 批量新增
	 */
	Integer addBatch(List<VideoInfo> listBean);

	/**
	 * 批量新增或更新
	 */
	Integer addOrUpdateBatch(List<VideoInfo> listBean);


	/**
	 * 根据VideoId查询
	 */
	VideoInfo getByVideoId(String videoId);

	/**
	 * 根据VideoId更新
	 */
	Integer updateByVideoId(VideoInfo bean, String videoId);

	/**
	 * 根据VideoId删除
	 */
	Integer deleteByVideoId(String videoId);

    void changeInteraction(String videoId, String userId, String interaction);

	void deleteVideo(String videoId, String userId) throws BusinessException;
}