package com.Bibibi.service;

import com.Bibibi.entity.po.VideoInfoFilePost;
import com.Bibibi.entity.po.VideoInfoPost;
import com.Bibibi.entity.query.VideoInfoPostQuery;
import com.Bibibi.entity.vo.PaginationResultVO;
import com.Bibibi.exception.BusinessException;

import java.util.List;
/**
 * @Description:视频信息Service
 * @date:2024-11-11
 * @author: liujun
 */
public interface VideoInfoPostService { 

	/**
	 * 根据条件查询列表
	 */
	List<VideoInfoPost> findListByParam(VideoInfoPostQuery query);

	/**
	 * 根据条件查询数量
	 */
	Integer findCountByParam(VideoInfoPostQuery query);

	/**
	 * 分页查询
	 */
	PaginationResultVO<VideoInfoPost> findByPage(VideoInfoPostQuery query);

	/**
	 * 新增
	 */
	Integer add(VideoInfoPost bean);

	/**
	 * 批量新增
	 */
	Integer addBatch(List<VideoInfoPost> listBean);

	/**
	 * 批量新增或更新
	 */
	Integer addOrUpdateBatch(List<VideoInfoPost> listBean);


	/**
	 * 根据VideoId查询
	 */
	VideoInfoPost getByVideoId(String videoId);

	/**
	 * 根据VideoId更新
	 */
	Integer updateByVideoId(VideoInfoPost bean, String videoId);

	/**
	 * 根据VideoId删除
	 */
	Integer deleteByVideoId(String videoId);

	/**
	 * 保存视频信息
	 * @param videoInfo
	 * @param filePostList
	 * @throws BusinessException
	 */
    void saveVideoInfo(VideoInfoPost videoInfo, List<VideoInfoFilePost> filePostList) throws BusinessException;

	void transferVideoFile(VideoInfoFilePost videoInfoFilePost);

	/**
	 * 管理端审核视频
	 * @param videoId 视频id
	 * @param status  审核结果
	 * @param reason  未通过理由
	 */
	void auditVideo(String videoId, Integer status, String reason) throws BusinessException;

    void addReadCount(String videoId);

    void recommendVideo(String videoId) throws BusinessException;
}