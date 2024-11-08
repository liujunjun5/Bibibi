package com.Bibibi.service.impl;

import com.Bibibi.entity.po.CategoryInfo;
import com.Bibibi.entity.query.CategoryInfoQuery;
import com.Bibibi.entity.query.SimplePage;
import com.Bibibi.entity.vo.PaginationResultVO;
import com.Bibibi.enums.PageSize;
import com.Bibibi.mappers.CategoryInfoMappers;
import com.Bibibi.service.CategoryInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description:分类信息Service
 * @date:2024-11-08
 * @author: liujun
 */
@Service("CategoryInfoService")
public class CategoryInfoServiceImpl implements CategoryInfoService {

	@Resource
	private CategoryInfoMappers<CategoryInfo, CategoryInfoQuery> categoryInfoMappers;

	/**
	 * 根据条件查询列表
	 */
	public List<CategoryInfo> findListByParam(CategoryInfoQuery query) {
		return this.categoryInfoMappers.selectList(query);
	}

	/**
	 * 根据条件查询数量
	 */
	public Integer findCountByParam(CategoryInfoQuery query) {
		return this.categoryInfoMappers.selectCount(query);
	}

	/**
	 * 分页查询
	 */
	public PaginationResultVO<CategoryInfo> findByPage(CategoryInfoQuery query) {
		Integer count = this.findCountByParam(query);
		Integer pageSize = query.getPageSize()==null?PageSize.SIZE15.getSize():query.getPageSize();
		SimplePage page = new SimplePage(query.getPageNo(), count, pageSize);
		query.setSimplePage(page);
		List<CategoryInfo> list = this.findListByParam(query);
		PaginationResultVO<CategoryInfo> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
		return result;
	}

	/**
	 * 新增
	 */
	public Integer add(CategoryInfo bean) {
		return this.categoryInfoMappers.insert(bean);
	}

	/**
	 * 批量新增
	 */
	public Integer addBatch(List<CategoryInfo> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.categoryInfoMappers.insertBatch(listBean);
	}

	/**
	 * 批量新增或更新
	 */
	public Integer addOrUpdateBatch(List<CategoryInfo> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.categoryInfoMappers.insertOrUpdateBatch(listBean);
	}


	/**
	 * 根据CategoryId查询
	 */
	public CategoryInfo getByCategoryId(Integer categoryId) {
		return this.categoryInfoMappers.selectByCategoryId(categoryId);
	}

	/**
	 * 根据CategoryId更新
	 */
	public Integer updateByCategoryId(CategoryInfo bean, Integer categoryId) {
		return this.categoryInfoMappers.updateByCategoryId(bean, categoryId);
	}

	/**
	 * 根据CategoryId删除
	 */
	public Integer deleteByCategoryId(Integer categoryId) {
		return this.categoryInfoMappers.deleteByCategoryId(categoryId);
	}


	/**
	 * 根据CategoryCode查询
	 */
	public CategoryInfo getByCategoryCode(String categoryCode) {
		return this.categoryInfoMappers.selectByCategoryCode(categoryCode);
	}

	/**
	 * 根据CategoryCode更新
	 */
	public Integer updateByCategoryCode(CategoryInfo bean, String categoryCode) {
		return this.categoryInfoMappers.updateByCategoryCode(bean, categoryCode);
	}

	/**
	 * 根据CategoryCode删除
	 */
	public Integer deleteByCategoryCode(String categoryCode) {
		return this.categoryInfoMappers.deleteByCategoryCode(categoryCode);
	}

}