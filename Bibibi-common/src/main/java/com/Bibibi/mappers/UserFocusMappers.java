package com.Bibibi.mappers;

import org.apache.ibatis.annotations.Param;

/**
 * @Description:Mapper
 * @date:2024-11-29
 * @author: liujun
 */
public interface UserFocusMappers<T, P> extends BaseMapper {

	/**
	 * 根据UserIdAndFocusUserId查询
	 */
	T selectByUserIdAndFocusUserId(@Param("userId") String userId, @Param("focusUserId") String focusUserId);

	/**
	 * 根据UserIdAndFocusUserId更新
	 */
	Integer updateByUserIdAndFocusUserId(@Param("bean") T t, @Param("userId") String userId, @Param("focusUserId") String focusUserId);

	/**
	 * 根据UserIdAndFocusUserId删除
	 */
	Integer deleteByUserIdAndFocusUserId(@Param("userId") String userId, @Param("focusUserId") String focusUserId);

}