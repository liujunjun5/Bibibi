package com.Bibibi.service;

import com.Bibibi.entity.po.CategoryInfo;
import com.Bibibi.entity.query.CategoryInfoQuery;
import com.Bibibi.entity.vo.PaginationResultVO;
import com.Bibibi.exception.BusinessException;

import java.util.List;

/**
 * @Description:分类信息Service
 * @date:2024-11-08
 * @author: liujun
 */
public interface CategoryInfoService { 

	/**
	 * 根据条件查询列表
	 */
	List<CategoryInfo> findListByParam(CategoryInfoQuery query);

	/**
	 * 根据条件查询数量
	 */
	Integer findCountByParam(CategoryInfoQuery query);

	/**
	 * 分页查询
	 */
	PaginationResultVO<CategoryInfo> findByPage(CategoryInfoQuery query);

	/**
	 * 新增
	 */
	Integer add(CategoryInfo bean);

	/**
	 * 批量新增
	 */
	Integer addBatch(List<CategoryInfo> listBean);

	/**
	 * 批量新增或更新
	 */
	Integer addOrUpdateBatch(List<CategoryInfo> listBean);


	/**
	 * 根据CategoryId查询
	 */
	CategoryInfo getByCategoryId(Integer categoryId);

	/**
	 * 根据CategoryId更新
	 */
	Integer updateByCategoryId(CategoryInfo bean, Integer categoryId);

	/**
	 * 根据CategoryId删除
	 */
	Integer deleteByCategoryId(Integer categoryId);


	/**
	 * 根据CategoryCode查询
	 */
	CategoryInfo getByCategoryCode(String categoryCode);

	/**
	 * 根据CategoryCode更新
	 */
	Integer updateByCategoryCode(CategoryInfo bean, String categoryCode);

	/**
	 * 根据CategoryCode删除
	 */
	Integer deleteByCategoryCode(String categoryCode);

	void saveCategory(CategoryInfo categoryInfo) throws BusinessException;

	void delCategory(Integer categoryId);

	void changeSort(Integer pCategoryId, String categoryIds);
}