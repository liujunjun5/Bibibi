package com.Bibibi.service;

import com.Bibibi.entity.po.VideoInfoFile;
import com.Bibibi.entity.query.VideoInfoFileQuery;
import com.Bibibi.entity.vo.PaginationResultVO;

import java.util.List;
/**
 * @Description:视频文件信息Service
 * @date:2024-11-11
 * @author: liujun
 */
public interface VideoInfoFileService { 

	/**
	 * 根据条件查询列表
	 */
	List<VideoInfoFile> findListByParam(VideoInfoFileQuery query);

	/**
	 * 根据条件查询数量
	 */
	Integer findCountByParam(VideoInfoFileQuery query);

	/**
	 * 分页查询
	 */
	PaginationResultVO<VideoInfoFile> findByPage(VideoInfoFileQuery query);

	/**
	 * 新增
	 */
	Integer add(VideoInfoFile bean);

	/**
	 * 批量新增
	 */
	Integer addBatch(List<VideoInfoFile> listBean);

	/**
	 * 批量新增或更新
	 */
	Integer addOrUpdateBatch(List<VideoInfoFile> listBean);


	/**
	 * 根据FileId查询
	 */
	VideoInfoFile getByFileId(String fileId);

	/**
	 * 根据FileId更新
	 */
	Integer updateByFileId(VideoInfoFile bean, String fileId);

	/**
	 * 根据FileId删除
	 */
	Integer deleteByFileId(String fileId);

}