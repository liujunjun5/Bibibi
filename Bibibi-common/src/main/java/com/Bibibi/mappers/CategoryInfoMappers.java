package com.Bibibi.mappers;

import org.apache.ibatis.annotations.Param;

/**
 * @Description:分类信息Mapper
 * @date:2024-11-08
 * @author: liujun
 */
public interface CategoryInfoMappers<T, P> extends BaseMapper {

	/**
	 * 根据CategoryId查询
	 */
	T selectByCategoryId(@Param("categoryId") Integer categoryId);

	/**
	 * 根据CategoryId更新
	 */
	Integer updateByCategoryId(@Param("bean") T t, @Param("categoryId") Integer categoryId);

	/**
	 * 根据CategoryId删除
	 */
	Integer deleteByCategoryId(@Param("categoryId") Integer categoryId);

	/**
	 * 根据CategoryCode查询
	 */
	T selectByCategoryCode(@Param("categoryCode") String categoryCode);

	/**
	 * 根据CategoryCode更新
	 */
	Integer updateByCategoryCode(@Param("bean") T t, @Param("categoryCode") String categoryCode);

	/**
	 * 根据CategoryCode删除
	 */
	Integer deleteByCategoryCode(@Param("categoryCode") String categoryCode);

}