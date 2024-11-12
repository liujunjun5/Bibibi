package com.Bibibi.service.impl;

import com.Bibibi.service.VideoInfoFileService;
import com.Bibibi.entity.po.VideoInfoFile;
import com.Bibibi.entity.query.SimplePage;
import com.Bibibi.entity.query.VideoInfoFileQuery;
import com.Bibibi.entity.vo.PaginationResultVO;
import com.Bibibi.enums.PageSize;
import com.Bibibi.mappers.VideoInfoFileMappers;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description:视频文件信息Service
 * @date:2024-11-11
 * @author: liujun
 */
@Service("VideoInfoFileService")
public class VideoInfoFileServiceImpl implements VideoInfoFileService {

	@Resource
	private VideoInfoFileMappers<VideoInfoFile, VideoInfoFileQuery> videoInfoFileMappers;

	/**
	 * 根据条件查询列表
	 */
	public List<VideoInfoFile> findListByParam(VideoInfoFileQuery query) {
		return this.videoInfoFileMappers.selectList(query);
	}

	/**
	 * 根据条件查询数量
	 */
	public Integer findCountByParam(VideoInfoFileQuery query) {
		return this.videoInfoFileMappers.selectCount(query);
	}

	/**
	 * 分页查询
	 */
	public PaginationResultVO<VideoInfoFile> findByPage(VideoInfoFileQuery query) {
		Integer count = this.findCountByParam(query);
		Integer pageSize = query.getPageSize()==null?PageSize.SIZE15.getSize():query.getPageSize();
		SimplePage page = new SimplePage(query.getPageNo(), count, pageSize);
		query.setSimplePage(page);
		List<VideoInfoFile> list = this.findListByParam(query);
		PaginationResultVO<VideoInfoFile> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
		return result;
	}

	/**
	 * 新增
	 */
	public Integer add(VideoInfoFile bean) {
		return this.videoInfoFileMappers.insert(bean);
	}

	/**
	 * 批量新增
	 */
	public Integer addBatch(List<VideoInfoFile> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.videoInfoFileMappers.insertBatch(listBean);
	}

	/**
	 * 批量新增或更新
	 */
	public Integer addOrUpdateBatch(List<VideoInfoFile> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.videoInfoFileMappers.insertOrUpdateBatch(listBean);
	}


	/**
	 * 根据FileId查询
	 */
	public VideoInfoFile getByFileId(String fileId) {
		return this.videoInfoFileMappers.selectByFileId(fileId);
	}

	/**
	 * 根据FileId更新
	 */
	public Integer updateByFileId(VideoInfoFile bean, String fileId) {
		return this.videoInfoFileMappers.updateByFileId(bean, fileId);
	}

	/**
	 * 根据FileId删除
	 */
	public Integer deleteByFileId(String fileId) {
		return this.videoInfoFileMappers.deleteByFileId(fileId);
	}

}