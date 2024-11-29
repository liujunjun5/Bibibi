package com.Bibibi.service.impl;

import com.Bibibi.entity.po.UserVideoSeriesVideo;
import com.Bibibi.entity.query.SimplePage;
import com.Bibibi.entity.query.UserVideoSeriesVideoQuery;
import com.Bibibi.entity.vo.PaginationResultVO;
import com.Bibibi.enums.PageSize;
import com.Bibibi.mappers.UserVideoSeriesVideoMappers;
import com.Bibibi.service.UserVideoSeriesVideoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description:Service
 * @date:2024-11-29
 * @author: liujun
 */
@Service("UserVideoSeriesVideoService")
public class UserVideoSeriesVideoServiceImpl implements UserVideoSeriesVideoService {

	@Resource
	private UserVideoSeriesVideoMappers<UserVideoSeriesVideo, UserVideoSeriesVideoQuery> userVideoSeriesVideoMappers;

	/**
	 * 根据条件查询列表
	 */
	public List<UserVideoSeriesVideo> findListByParam(UserVideoSeriesVideoQuery query) {
		return this.userVideoSeriesVideoMappers.selectList(query);
	}

	/**
	 * 根据条件查询数量
	 */
	public Integer findCountByParam(UserVideoSeriesVideoQuery query) {
		return this.userVideoSeriesVideoMappers.selectCount(query);
	}

	/**
	 * 分页查询
	 */
	public PaginationResultVO<UserVideoSeriesVideo> findByPage(UserVideoSeriesVideoQuery query) {
		Integer count = this.findCountByParam(query);
		Integer pageSize = query.getPageSize()==null?PageSize.SIZE15.getSize():query.getPageSize();
		SimplePage page = new SimplePage(query.getPageNo(), count, pageSize);
		query.setSimplePage(page);
		List<UserVideoSeriesVideo> list = this.findListByParam(query);
		PaginationResultVO<UserVideoSeriesVideo> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
		return result;
	}

	/**
	 * 新增
	 */
	public Integer add(UserVideoSeriesVideo bean) {
		return this.userVideoSeriesVideoMappers.insert(bean);
	}

	/**
	 * 批量新增
	 */
	public Integer addBatch(List<UserVideoSeriesVideo> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.userVideoSeriesVideoMappers.insertBatch(listBean);
	}

	/**
	 * 批量新增或更新
	 */
	public Integer addOrUpdateBatch(List<UserVideoSeriesVideo> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.userVideoSeriesVideoMappers.insertOrUpdateBatch(listBean);
	}


	/**
	 * 根据SeriesIdAndVideoId查询
	 */
	public UserVideoSeriesVideo getBySeriesIdAndVideoId(Integer seriesId, String videoId) {
		return this.userVideoSeriesVideoMappers.selectBySeriesIdAndVideoId(seriesId, videoId);
	}

	/**
	 * 根据SeriesIdAndVideoId更新
	 */
	public Integer updateBySeriesIdAndVideoId(UserVideoSeriesVideo bean, Integer seriesId, String videoId) {
		return this.userVideoSeriesVideoMappers.updateBySeriesIdAndVideoId(bean, seriesId, videoId);
	}

	/**
	 * 根据SeriesIdAndVideoId删除
	 */
	public Integer deleteBySeriesIdAndVideoId(Integer seriesId, String videoId) {
		return this.userVideoSeriesVideoMappers.deleteBySeriesIdAndVideoId(seriesId, videoId);
	}

}