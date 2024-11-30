package com.Bibibi.entity.query;

import java.util.Date;

/**
 * @Description:查询对象
 * @date:2024-11-29
 * @author: liujun
 */
public class UserFocusQuery extends BaseQuery{
	/**
	 * 用户ID
	 */
	private String userId;

	private String userIdFuzzy;

	/**
	 * 用户ID
	 */
	private String focusUserId;

	private String focusUserIdFuzzy;

	/**
	 * 關注事件
	 */
	private Date focusTime;

	private String focusTimeStart;

	private String focusTimeEnd;

	private Integer queryType;

	public Integer getQueryType() {
		return queryType;
	}

	public void setQueryType(Integer queryType) {
		this.queryType = queryType;
	}

	public void setUserIdFuzzy(String userIdFuzzy) {
		this.userIdFuzzy = userIdFuzzy;
	}

	public String getUserIdFuzzy() {
		return this.userIdFuzzy;
	}

	public void setFocusUserIdFuzzy(String focusUserIdFuzzy) {
		this.focusUserIdFuzzy = focusUserIdFuzzy;
	}

	public String getFocusUserIdFuzzy() {
		return this.focusUserIdFuzzy;
	}

	public void setFocusTimeStart(String focusTimeStart) {
		this.focusTimeStart = focusTimeStart;
	}

	public String getFocusTimeStart() {
		return this.focusTimeStart;
	}

	public void setFocusTimeEnd(String focusTimeEnd) {
		this.focusTimeEnd = focusTimeEnd;
	}

	public String getFocusTimeEnd() {
		return this.focusTimeEnd;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserId() {
		return this.userId;
	}

	public void setFocusUserId(String focusUserId) {
		this.focusUserId = focusUserId;
	}

	public String getFocusUserId() {
		return this.focusUserId;
	}

	public void setFocusTime(Date focusTime) {
		this.focusTime = focusTime;
	}

	public Date getFocusTime() {
		return this.focusTime;
	}

}