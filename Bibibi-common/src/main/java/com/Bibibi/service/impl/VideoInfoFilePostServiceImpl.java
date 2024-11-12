package com.Bibibi.service.impl;

import com.Bibibi.service.VideoInfoFilePostService;
import com.Bibibi.entity.po.VideoInfoFilePost;
import com.Bibibi.entity.query.SimplePage;
import com.Bibibi.entity.query.VideoInfoFilePostQuery;
import com.Bibibi.entity.vo.PaginationResultVO;
import com.Bibibi.enums.PageSize;
import com.Bibibi.mappers.VideoInfoFilePostMappers;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description:视频文件信息Service
 * @date:2024-11-11
 * @author: liujun
 */
@Service("VideoInfoFilePostService")
public class VideoInfoFilePostServiceImpl implements VideoInfoFilePostService {

	@Resource
	private VideoInfoFilePostMappers<VideoInfoFilePost, VideoInfoFilePostQuery> videoInfoFilePostMappers;

	/**
	 * 根据条件查询列表
	 */
	public List<VideoInfoFilePost> findListByParam(VideoInfoFilePostQuery query) {
		return this.videoInfoFilePostMappers.selectList(query);
	}

	/**
	 * 根据条件查询数量
	 */
	public Integer findCountByParam(VideoInfoFilePostQuery query) {
		return this.videoInfoFilePostMappers.selectCount(query);
	}

	/**
	 * 分页查询
	 */
	public PaginationResultVO<VideoInfoFilePost> findByPage(VideoInfoFilePostQuery query) {
		Integer count = this.findCountByParam(query);
		Integer pageSize = query.getPageSize()==null?PageSize.SIZE15.getSize():query.getPageSize();
		SimplePage page = new SimplePage(query.getPageNo(), count, pageSize);
		query.setSimplePage(page);
		List<VideoInfoFilePost> list = this.findListByParam(query);
		PaginationResultVO<VideoInfoFilePost> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
		return result;
	}

	/**
	 * 新增
	 */
	public Integer add(VideoInfoFilePost bean) {
		return this.videoInfoFilePostMappers.insert(bean);
	}

	/**
	 * 批量新增
	 */
	public Integer addBatch(List<VideoInfoFilePost> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.videoInfoFilePostMappers.insertBatch(listBean);
	}

	/**
	 * 批量新增或更新
	 */
	public Integer addOrUpdateBatch(List<VideoInfoFilePost> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.videoInfoFilePostMappers.insertOrUpdateBatch(listBean);
	}


	/**
	 * 根据FileId查询
	 */
	public VideoInfoFilePost getByFileId(String fileId) {
		return this.videoInfoFilePostMappers.selectByFileId(fileId);
	}

	/**
	 * 根据FileId更新
	 */
	public Integer updateByFileId(VideoInfoFilePost bean, String fileId) {
		return this.videoInfoFilePostMappers.updateByFileId(bean, fileId);
	}

	/**
	 * 根据FileId删除
	 */
	public Integer deleteByFileId(String fileId) {
		return this.videoInfoFilePostMappers.deleteByFileId(fileId);
	}


	/**
	 * 根据UploadIdAndUserId查询
	 */
	public VideoInfoFilePost getByUploadIdAndUserId(String uploadId, String userId) {
		return this.videoInfoFilePostMappers.selectByUploadIdAndUserId(uploadId, userId);
	}

	/**
	 * 根据UploadIdAndUserId更新
	 */
	public Integer updateByUploadIdAndUserId(VideoInfoFilePost bean, String uploadId, String userId) {
		return this.videoInfoFilePostMappers.updateByUploadIdAndUserId(bean, uploadId, userId);
	}

	/**
	 * 根据UploadIdAndUserId删除
	 */
	public Integer deleteByUploadIdAndUserId(String uploadId, String userId) {
		return this.videoInfoFilePostMappers.deleteByUploadIdAndUserId(uploadId, userId);
	}

}