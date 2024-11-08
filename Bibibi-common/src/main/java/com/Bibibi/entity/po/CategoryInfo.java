package com.Bibibi.entity.po;

import java.io.Serializable;

/**
 * @Description:分类信息
 * @date:2024-11-08
 * @author: liujun
 */
public class CategoryInfo implements Serializable {
	/**
	 * 自增分类ID
	 */
	private Integer categoryId;

	/**
	 * 分类编码
	 */
	private String categoryCode;

	/**
	 * 分类名称
	 */
	private String categoryName;

	/**
	 * 父级分类ID
	 */
	private Integer pCategoryId;

	/**
	 * 图标
	 */
	private String icon;

	/**
	 * 背景图
	 */
	private String background;

	/**
	 * 排序号
	 */
	private Integer sort;


	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}

	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public void setPCategoryId(Integer pCategoryId) {
		this.pCategoryId = pCategoryId;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public void setBackground(String background) {
		this.background = background;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public Integer getCategoryId() {
		return this.categoryId;
	}

	public String getCategoryCode() {
		return this.categoryCode;
	}

	public String getCategoryName() {
		return this.categoryName;
	}

	public Integer getPCategoryId() {
		return this.pCategoryId;
	}

	public String getIcon() {
		return this.icon;
	}

	public String getBackground() {
		return this.background;
	}

	public Integer getSort() {
		return this.sort;
	}

	@Override
	public String toString() {
		return "自增分类ID:" + (categoryId == null ? "null" : categoryId) + ",分类编码:" + (categoryCode == null ? "null" : categoryCode) + ",分类名称:" + (categoryName == null ? "null" : categoryName) + ",父级分类ID:" + (pCategoryId == null ? "null" : pCategoryId) + ",图标:" + (icon == null ? "null" : icon) + ",背景图:" + (background == null ? "null" : background) + ",排序号:" + (sort == null ? "null" : sort);
	}
}