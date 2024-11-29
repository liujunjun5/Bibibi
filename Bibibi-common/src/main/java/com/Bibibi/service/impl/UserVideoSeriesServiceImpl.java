package com.Bibibi.service.impl;

import com.Bibibi.entity.po.UserVideoSeries;
import com.Bibibi.entity.query.SimplePage;
import com.Bibibi.entity.query.UserVideoSeriesQuery;
import com.Bibibi.entity.vo.PaginationResultVO;
import com.Bibibi.enums.PageSize;
import com.Bibibi.mappers.UserVideoSeriesMappers;
import com.Bibibi.service.UserVideoSeriesService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description:用户视频序列归档Service
 * @date:2024-11-29
 * @author: liujun
 */
@Service("UserVideoSeriesService")
public class UserVideoSeriesServiceImpl implements UserVideoSeriesService {

	@Resource
	private UserVideoSeriesMappers<UserVideoSeries, UserVideoSeriesQuery> userVideoSeriesMappers;

	/**
	 * 根据条件查询列表
	 */
	public List<UserVideoSeries> findListByParam(UserVideoSeriesQuery query) {
		return this.userVideoSeriesMappers.selectList(query);
	}

	/**
	 * 根据条件查询数量
	 */
	public Integer findCountByParam(UserVideoSeriesQuery query) {
		return this.userVideoSeriesMappers.selectCount(query);
	}

	/**
	 * 分页查询
	 */
	public PaginationResultVO<UserVideoSeries> findByPage(UserVideoSeriesQuery query) {
		Integer count = this.findCountByParam(query);
		Integer pageSize = query.getPageSize()==null?PageSize.SIZE15.getSize():query.getPageSize();
		SimplePage page = new SimplePage(query.getPageNo(), count, pageSize);
		query.setSimplePage(page);
		List<UserVideoSeries> list = this.findListByParam(query);
		PaginationResultVO<UserVideoSeries> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
		return result;
	}

	/**
	 * 新增
	 */
	public Integer add(UserVideoSeries bean) {
		return this.userVideoSeriesMappers.insert(bean);
	}

	/**
	 * 批量新增
	 */
	public Integer addBatch(List<UserVideoSeries> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.userVideoSeriesMappers.insertBatch(listBean);
	}

	/**
	 * 批量新增或更新
	 */
	public Integer addOrUpdateBatch(List<UserVideoSeries> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.userVideoSeriesMappers.insertOrUpdateBatch(listBean);
	}


	/**
	 * 根据SeriesId查询
	 */
	public UserVideoSeries getBySeriesId(Integer seriesId) {
		return this.userVideoSeriesMappers.selectBySeriesId(seriesId);
	}

	/**
	 * 根据SeriesId更新
	 */
	public Integer updateBySeriesId(UserVideoSeries bean, Integer seriesId) {
		return this.userVideoSeriesMappers.updateBySeriesId(bean, seriesId);
	}

	/**
	 * 根据SeriesId删除
	 */
	public Integer deleteBySeriesId(Integer seriesId) {
		return this.userVideoSeriesMappers.deleteBySeriesId(seriesId);
	}

}