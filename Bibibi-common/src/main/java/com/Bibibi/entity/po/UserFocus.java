package com.Bibibi.entity.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description:
 * @date:2024-11-29
 * @author: liujun
 */
public class UserFocus implements Serializable {
	/**
	 * 用户ID
	 */
	private String userId;

	/**
	 * 用户ID
	 */
	private String focusUserId;

	/**
	 * 
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date focusTime;


	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void setFocusUserId(String focusUserId) {
		this.focusUserId = focusUserId;
	}

	public void setFocusTime(Date focusTime) {
		this.focusTime = focusTime;
	}

	public String getUserId() {
		return this.userId;
	}

	public String getFocusUserId() {
		return this.focusUserId;
	}

	public Date getFocusTime() {
		return this.focusTime;
	}

	@Override
	public String toString() {
		return "用户ID:" + (userId == null ? "null" : userId) + ",用户ID:" + (focusUserId == null ? "null" : focusUserId) + ",:" + (focusTime == null ? "null" : focusTime);
	}
}