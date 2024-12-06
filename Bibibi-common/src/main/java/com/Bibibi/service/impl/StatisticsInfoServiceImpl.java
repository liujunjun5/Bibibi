package com.Bibibi.service.impl;

import com.Bibibi.entity.po.StatisticsInfo;
import com.Bibibi.entity.query.SimplePage;
import com.Bibibi.entity.query.StatisticsInfoQuery;
import com.Bibibi.entity.vo.PaginationResultVO;
import com.Bibibi.enums.PageSize;
import com.Bibibi.mappers.StatisticsInfoMappers;
import com.Bibibi.service.StatisticsInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description:数据统计Service
 * @date:2024-12-04
 * @author: liujun
 */
@Service("StatisticsInfoService")
public class StatisticsInfoServiceImpl implements StatisticsInfoService {

	@Resource
	private StatisticsInfoMappers<StatisticsInfo, StatisticsInfoQuery> statisticsInfoMappers;

	/**
	 * 根据条件查询列表
	 */
	public List<StatisticsInfo> findListByParam(StatisticsInfoQuery query) {
		return this.statisticsInfoMappers.selectList(query);
	}

	/**
	 * 根据条件查询数量
	 */
	public Integer findCountByParam(StatisticsInfoQuery query) {
		return this.statisticsInfoMappers.selectCount(query);
	}

	/**
	 * 分页查询
	 */
	public PaginationResultVO<StatisticsInfo> findByPage(StatisticsInfoQuery query) {
		Integer count = this.findCountByParam(query);
		Integer pageSize = query.getPageSize()==null?PageSize.SIZE15.getSize():query.getPageSize();
		SimplePage page = new SimplePage(query.getPageNo(), count, pageSize);
		query.setSimplePage(page);
		List<StatisticsInfo> list = this.findListByParam(query);
		PaginationResultVO<StatisticsInfo> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
		return result;
	}

	/**
	 * 新增
	 */
	public Integer add(StatisticsInfo bean) {
		return this.statisticsInfoMappers.insert(bean);
	}

	/**
	 * 批量新增
	 */
	public Integer addBatch(List<StatisticsInfo> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.statisticsInfoMappers.insertBatch(listBean);
	}

	/**
	 * 批量新增或更新
	 */
	public Integer addOrUpdateBatch(List<StatisticsInfo> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.statisticsInfoMappers.insertOrUpdateBatch(listBean);
	}


	/**
	 * 根据StatisticsDateAndUserIdAndDataType查询
	 */
	public StatisticsInfo getByStatisticsDateAndUserIdAndDataType(String statisticsDate, String userId, Integer dataType) {
		return this.statisticsInfoMappers.selectByStatisticsDateAndUserIdAndDataType(statisticsDate, userId, dataType);
	}

	/**
	 * 根据StatisticsDateAndUserIdAndDataType更新
	 */
	public Integer updateByStatisticsDateAndUserIdAndDataType(StatisticsInfo bean, String statisticsDate, String userId, Integer dataType) {
		return this.statisticsInfoMappers.updateByStatisticsDateAndUserIdAndDataType(bean, statisticsDate, userId, dataType);
	}

	/**
	 * 根据StatisticsDateAndUserIdAndDataType删除
	 */
	public Integer deleteByStatisticsDateAndUserIdAndDataType(String statisticsDate, String userId, Integer dataType) {
		return this.statisticsInfoMappers.deleteByStatisticsDateAndUserIdAndDataType(statisticsDate, userId, dataType);
	}

}