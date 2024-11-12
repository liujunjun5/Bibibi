package com.Bibibi.service.impl;

import com.Bibibi.component.RedisComponent;
import com.Bibibi.entity.constants.Constants;
import com.Bibibi.entity.po.CategoryInfo;
import com.Bibibi.entity.query.CategoryInfoQuery;
import com.Bibibi.entity.query.SimplePage;
import com.Bibibi.entity.vo.PaginationResultVO;
import com.Bibibi.enums.PageSize;
import com.Bibibi.exception.BusinessException;
import com.Bibibi.mappers.CategoryInfoMappers;
import com.Bibibi.service.CategoryInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
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
	@Autowired
	private RedisComponent redisComponent;

	/**
	 * 根据条件查询列表
	 */
	public List<CategoryInfo> findListByParam(CategoryInfoQuery query) {
		List<CategoryInfo> list = this.categoryInfoMappers.selectList(query);
		if (query.getConvert2Tree() != null && query.getConvert2Tree()) {
			list = convertLine2Tree(list, Constants.ZERO);
		}
		return list;
	}

	private List<CategoryInfo> convertLine2Tree(List<CategoryInfo> dataList, Integer pid) {
		List<CategoryInfo> children = new ArrayList<>();
		for (CategoryInfo m : dataList) {
			if (m.getCategoryId() != null && m.getPCategoryId() != null && m.getPCategoryId().equals(pid)) {
				m.setChildren(convertLine2Tree(dataList, m.getCategoryId()));
				children.add(m);
			}
		}
		return children;
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

	@Override
	public void saveCategory(CategoryInfo bean) throws BusinessException {
		CategoryInfo dbBean = this.categoryInfoMappers.selectByCategoryCode(bean.getCategoryCode());
		if (bean.getCategoryId() == null && dbBean != null ||
				bean.getCategoryId() != null && dbBean != null && !bean.getCategoryId().equals(dbBean.getCategoryId())) {
			throw new BusinessException("分类编号已存在");
		}
		if (bean.getCategoryId() == null) {
			Integer maxSort = this.categoryInfoMappers.selectMaxSort(bean.getPCategoryId());
			bean.setSort(maxSort + 1);
			this.categoryInfoMappers.insert(bean);
		} else {
			this.categoryInfoMappers.updateByCategoryId(bean, bean.getCategoryId());
		}

		save2Redis();
	}

	@Override
	public void delCategory(Integer categoryId) {
		//TODO 查询分类下是否有视频
		CategoryInfoQuery categoryInfoQuery = new CategoryInfoQuery();
		categoryInfoQuery.setCategoryIdOrPCategoryId(categoryId);
		categoryInfoMappers.deleteByParam(categoryInfoQuery);

		save2Redis();
	}

	@Override
	public void changeSort(Integer pCategoryId, String categoryIds) {
		String[] categoryIdArray = categoryIds.split(",");
		ArrayList<CategoryInfo> categoryList = new ArrayList<>();
		Integer sort = 1;
		for (String categoryId : categoryIdArray) {
			CategoryInfo categoryInfo = new CategoryInfo();
			categoryInfo.setCategoryId(Integer.parseInt(categoryId));
			categoryInfo.setPCategoryId(pCategoryId);
			categoryInfo.setSort(sort++);
			categoryList.add(categoryInfo);
		}
		this.categoryInfoMappers.updateSortBatch(categoryList);

		save2Redis();
	}

	@Override
	public List<CategoryInfo> getAllCategoryList() {
		List<CategoryInfo> categoryInfoList = redisComponent.getCategoryList();
		if (categoryInfoList.isEmpty()) {
			save2Redis();
		}
		return redisComponent.getCategoryList();
	}

	/**
	 * 刷新缓存
	 */
	private void save2Redis() {
		CategoryInfoQuery query = new CategoryInfoQuery();
		query.setOrderBy("sort asc");
		query.setConvert2Tree(true);
		List<CategoryInfo> categoryInfoList = findListByParam(query);
		redisComponent.saveCategoryList(categoryInfoList);
	}
}