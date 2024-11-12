package com.Bibibi.service;

import com.Bibibi.entity.po.VideoInfoFilePost;
import com.Bibibi.entity.query.VideoInfoFilePostQuery;
import com.Bibibi.entity.vo.PaginationResultVO;

import java.util.List;
/**
 * @Description:视频文件信息Service
 * @date:2024-11-11
 * @author: liujun
 */
public interface VideoInfoFilePostService { 

	/**
	 * 根据条件查询列表
	 */
	List<VideoInfoFilePost> findListByParam(VideoInfoFilePostQuery query);

	/**
	 * 根据条件查询数量
	 */
	Integer findCountByParam(VideoInfoFilePostQuery query);

	/**
	 * 分页查询
	 */
	PaginationResultVO<VideoInfoFilePost> findByPage(VideoInfoFilePostQuery query);

	/**
	 * 新增
	 */
	Integer add(VideoInfoFilePost bean);

	/**
	 * 批量新增
	 */
	Integer addBatch(List<VideoInfoFilePost> listBean);

	/**
	 * 批量新增或更新
	 */
	Integer addOrUpdateBatch(List<VideoInfoFilePost> listBean);


	/**
	 * 根据FileId查询
	 */
	VideoInfoFilePost getByFileId(String fileId);

	/**
	 * 根据FileId更新
	 */
	Integer updateByFileId(VideoInfoFilePost bean, String fileId);

	/**
	 * 根据FileId删除
	 */
	Integer deleteByFileId(String fileId);


	/**
	 * 根据UploadIdAndUserId查询
	 */
	VideoInfoFilePost getByUploadIdAndUserId(String uploadId, String userId);

	/**
	 * 根据UploadIdAndUserId更新
	 */
	Integer updateByUploadIdAndUserId(VideoInfoFilePost bean, String uploadId, String userId);

	/**
	 * 根据UploadIdAndUserId删除
	 */
	Integer deleteByUploadIdAndUserId(String uploadId, String userId);

}