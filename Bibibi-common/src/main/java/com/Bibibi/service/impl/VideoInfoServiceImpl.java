package com.Bibibi.service.impl;

import com.Bibibi.service.VideoInfoService;
import com.Bibibi.entity.po.VideoInfo;
import com.Bibibi.entity.query.SimplePage;
import com.Bibibi.entity.query.VideoInfoQuery;
import com.Bibibi.entity.vo.PaginationResultVO;
import com.Bibibi.enums.PageSize;
import com.Bibibi.mappers.VideoInfoMappers;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description:视频信息Service
 * @date:2024-11-11
 * @author: liujun
 */
@Service("VideoInfoService")
public class VideoInfoServiceImpl implements VideoInfoService {

	@Resource
	private VideoInfoMappers<VideoInfo, VideoInfoQuery> videoInfoMappers;

	/**
	 * 根据条件查询列表
	 */
	public List<VideoInfo> findListByParam(VideoInfoQuery query) {
		return this.videoInfoMappers.selectList(query);
	}

	/**
	 * 根据条件查询数量
	 */
	public Integer findCountByParam(VideoInfoQuery query) {
		return this.videoInfoMappers.selectCount(query);
	}

	/**
	 * 分页查询
	 */
	public PaginationResultVO<VideoInfo> findByPage(VideoInfoQuery query) {
		Integer count = this.findCountByParam(query);
		Integer pageSize = query.getPageSize()==null?PageSize.SIZE15.getSize():query.getPageSize();
		SimplePage page = new SimplePage(query.getPageNo(), count, pageSize);
		query.setSimplePage(page);
		List<VideoInfo> list = this.findListByParam(query);
		PaginationResultVO<VideoInfo> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
		return result;
	}

	/**
	 * 新增
	 */
	public Integer add(VideoInfo bean) {
		return this.videoInfoMappers.insert(bean);
	}

	/**
	 * 批量新增
	 */
	public Integer addBatch(List<VideoInfo> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.videoInfoMappers.insertBatch(listBean);
	}

	/**
	 * 批量新增或更新
	 */
	public Integer addOrUpdateBatch(List<VideoInfo> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.videoInfoMappers.insertOrUpdateBatch(listBean);
	}


	/**
	 * 根据VideoId查询
	 */
	public VideoInfo getByVideoId(String videoId) {
		return this.videoInfoMappers.selectByVideoId(videoId);
	}

	/**
	 * 根据VideoId更新
	 */
	public Integer updateByVideoId(VideoInfo bean, String videoId) {
		return this.videoInfoMappers.updateByVideoId(bean, videoId);
	}

	/**
	 * 根据VideoId删除
	 */
	public Integer deleteByVideoId(String videoId) {
		return this.videoInfoMappers.deleteByVideoId(videoId);
	}

}