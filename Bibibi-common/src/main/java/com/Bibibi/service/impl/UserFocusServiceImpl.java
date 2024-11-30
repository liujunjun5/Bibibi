package com.Bibibi.service.impl;

import com.Bibibi.entity.po.UserFocus;
import com.Bibibi.entity.po.UserInfo;
import com.Bibibi.entity.query.SimplePage;
import com.Bibibi.entity.query.UserFocusQuery;
import com.Bibibi.entity.query.UserInfoQuery;
import com.Bibibi.entity.vo.PaginationResultVO;
import com.Bibibi.enums.PageSize;
import com.Bibibi.enums.ResponseCodeEnum;
import com.Bibibi.exception.BusinessException;
import com.Bibibi.mappers.UserFocusMappers;
import com.Bibibi.mappers.UserInfoMappers;
import com.Bibibi.service.UserFocusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @Description:Service
 * @date:2024-11-29
 * @author: liujun
 */
@Service("UserFocusService")
public class UserFocusServiceImpl implements UserFocusService {

	@Resource
	private UserFocusMappers<UserFocus, UserFocusQuery> userFocusMappers;

	@Autowired
    private UserInfoMappers<UserInfo, UserInfoQuery> userInfoMappers;

	/**
	 * 根据条件查询列表
	 */
	public List<UserFocus> findListByParam(UserFocusQuery query) {
		return this.userFocusMappers.selectList(query);
	}

	/**
	 * 根据条件查询数量
	 */
	public Integer findCountByParam(UserFocusQuery query) {
		return this.userFocusMappers.selectCount(query);
	}

	/**
	 * 分页查询
	 */
	public PaginationResultVO<UserFocus> findByPage(UserFocusQuery query) {
		Integer count = this.findCountByParam(query);
		Integer pageSize = query.getPageSize()==null?PageSize.SIZE15.getSize():query.getPageSize();
		SimplePage page = new SimplePage(query.getPageNo(), count, pageSize);
		query.setSimplePage(page);
		List<UserFocus> list = this.findListByParam(query);
		PaginationResultVO<UserFocus> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
		return result;
	}

	/**
	 * 新增
	 */
	public Integer add(UserFocus bean) {
		return this.userFocusMappers.insert(bean);
	}

	/**
	 * 批量新增
	 */
	public Integer addBatch(List<UserFocus> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.userFocusMappers.insertBatch(listBean);
	}

	/**
	 * 批量新增或更新
	 */
	public Integer addOrUpdateBatch(List<UserFocus> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.userFocusMappers.insertOrUpdateBatch(listBean);
	}


	/**
	 * 根据UserIdAndFocusUserId查询
	 */
	public UserFocus getByUserIdAndFocusUserId(String userId, String focusUserId) {
		return this.userFocusMappers.selectByUserIdAndFocusUserId(userId, focusUserId);
	}

	/**
	 * 根据UserIdAndFocusUserId更新
	 */
	public Integer updateByUserIdAndFocusUserId(UserFocus bean, String userId, String focusUserId) {
		return this.userFocusMappers.updateByUserIdAndFocusUserId(bean, userId, focusUserId);
	}

	/**
	 * 根据UserIdAndFocusUserId删除
	 */
	public Integer deleteByUserIdAndFocusUserId(String userId, String focusUserId) {
		return this.userFocusMappers.deleteByUserIdAndFocusUserId(userId, focusUserId);
	}

	/**
	 * @param userId
	 * @param focusUserId
	 */
	@Override
	public void focusUser(String userId, String focusUserId) throws BusinessException {
		if (userId.equals(focusUserId)) {
			throw new BusinessException("不能對自己做此操作");
		}
		UserFocus dbInfo = this.userFocusMappers.selectByUserIdAndFocusUserId(userId, focusUserId);
		if (dbInfo!=null) {
			return;
		}
		UserInfo userInfo = userInfoMappers.selectByUserId(focusUserId);
		if (userInfo==null) {
			throw new BusinessException(ResponseCodeEnum.CODE_600);
		}
		UserFocus userFocus = new UserFocus();
		userFocus.setFocusTime(new Date());
		userFocus.setUserId(userId);
		userFocus.setFocusUserId(focusUserId);
		add(userFocus);
	}

	/**
	 * @param userId
	 * @param focusUserId
	 */
	@Override
	public void cancelFocus(String userId, String focusUserId) {
		deleteByUserIdAndFocusUserId(userId, focusUserId);
	}
}