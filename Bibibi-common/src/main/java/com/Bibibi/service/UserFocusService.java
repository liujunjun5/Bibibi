package com.Bibibi.service;

import com.Bibibi.entity.po.UserFocus;
import com.Bibibi.entity.query.UserFocusQuery;
import com.Bibibi.entity.vo.PaginationResultVO;
import com.Bibibi.exception.BusinessException;

import java.util.List;

/**
 * @Description:Service
 * @date:2024-11-29
 * @author: liujun
 */
public interface UserFocusService { 

	/**
	 * 根据条件查询列表
	 */
	List<UserFocus> findListByParam(UserFocusQuery query);

	/**
	 * 根据条件查询数量
	 */
	Integer findCountByParam(UserFocusQuery query);

	/**
	 * 分页查询
	 */
	PaginationResultVO<UserFocus> findByPage(UserFocusQuery query);

	/**
	 * 新增
	 */
	Integer add(UserFocus bean);

	/**
	 * 批量新增
	 */
	Integer addBatch(List<UserFocus> listBean);

	/**
	 * 批量新增或更新
	 */
	Integer addOrUpdateBatch(List<UserFocus> listBean);


	/**
	 * 根据UserIdAndFocusUserId查询
	 */
	UserFocus getByUserIdAndFocusUserId(String userId, String focusUserId);

	/**
	 * 根据UserIdAndFocusUserId更新
	 */
	Integer updateByUserIdAndFocusUserId(UserFocus bean, String userId, String focusUserId);

	/**
	 * 根据UserIdAndFocusUserId删除
	 */
	Integer deleteByUserIdAndFocusUserId(String userId, String focusUserId);

	void focusUser(String userId, String focusUserId) throws BusinessException;

	void cancelFocus(String userId, String focusUserId);
}