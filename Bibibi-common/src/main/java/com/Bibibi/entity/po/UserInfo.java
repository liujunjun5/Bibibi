package com.Bibibi.entity.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description:用户信息
 * @date:2024-11-18
 * @author: liujun
 */
public class UserInfo implements Serializable {
	/**
	 * 用户id
	 */
	private String userId;

	/**
	 * 昵称
	 */
	private String nickName;

	/**
	 * 邮箱
	 */
	private String email;

	/**
	 * 密码
	 */
	private String password;

	/**
	 * 0:女 1:男 2:未知
	 */
	private Integer sex;

	/**
	 * 出生日期
	 */
	private String birthday;

	/**
	 * 学校
	 */
	private String school;

	/**
	 * 个人简介
	 */
	private String personIntroduction;

	/**
	 * 加入时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date joinTime;

	/**
	 * 最后登录时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date lastLoginTime;

	/**
	 * 最后登录ip
	 */
	private String lastLoginIp;

	/**
	 * 0:禁用 1:正常
	 */
	private Integer status;
//	private Integer status;
	/**
	 * 空间公告
	 */
	private String noticeInfo;

	/**
	 * 硬币总数量
	 */
	private Integer totalCoinCount;

	/**
	 * 当前硬币数
	 */
	private Integer currentCoinCount;

	/**
	 * 主题
	 */
	private Integer theme;

	/**
	 * 头像
	 */
	private String avatar;

	private Integer fansCount;

	private Integer focusCount;

	private Integer likeCount;

	private Integer playCount;

	private Boolean haveFocus;

	public Integer getFansCount() {
		return fansCount;
	}

	public void setFansCount(Integer fansCount) {
		this.fansCount = fansCount;
	}

	public Integer getFocusCount() {
		return focusCount;
	}

	public void setFocusCount(Integer focusCount) {
		this.focusCount = focusCount;
	}

	public Integer getLikeCount() {
		return likeCount;
	}

	public void setLikeCount(Integer likeCount) {
		this.likeCount = likeCount;
	}

	public Integer getPlayCount() {
		return playCount;
	}

	public void setPlayCount(Integer playCount) {
		this.playCount = playCount;
	}

	public Boolean getHaveFocus() {
		return haveFocus;
	}

	public void setHaveFocus(Boolean haveFocus) {
		this.haveFocus = haveFocus;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public void setSchool(String school) {
		this.school = school;
	}

	public void setPersonIntroduction(String personIntroduction) {
		this.personIntroduction = personIntroduction;
	}

	public void setJoinTime(Date joinTime) {
		this.joinTime = joinTime;
	}

	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public void setLastLoginIp(String lastLoginIp) {
		this.lastLoginIp = lastLoginIp;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public void setNoticeInfo(String noticeInfo) {
		this.noticeInfo = noticeInfo;
	}

	public void setTotalCoinCount(Integer totalCoinCount) {
		this.totalCoinCount = totalCoinCount;
	}

	public void setCurrentCoinCount(Integer currentCoinCount) {
		this.currentCoinCount = currentCoinCount;
	}

	public void setTheme(Integer theme) {
		this.theme = theme;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getUserId() {
		return this.userId;
	}

	public String getNickName() {
		return this.nickName;
	}

	public String getEmail() {
		return this.email;
	}

	public String getPassword() {
		return this.password;
	}

	public Integer getSex() {
		return this.sex;
	}

	public String getBirthday() {
		return this.birthday;
	}

	public String getSchool() {
		return this.school;
	}

	public String getPersonIntroduction() {
		return this.personIntroduction;
	}

	public Date getJoinTime() {
		return this.joinTime;
	}

	public Date getLastLoginTime() {
		return this.lastLoginTime;
	}

	public String getLastLoginIp() {
		return this.lastLoginIp;
	}

	public Integer getStatus() {
		return this.status;
	}

	public String getNoticeInfo() {
		return this.noticeInfo;
	}

	public Integer getTotalCoinCount() {
		return this.totalCoinCount;
	}

	public Integer getCurrentCoinCount() {
		return this.currentCoinCount;
	}

	public Integer getTheme() {
		return this.theme;
	}

	public String getAvatar() {
		return this.avatar;
	}

	@Override
	public String toString() {
		return "用户id:" + (userId == null ? "null" : userId) + ",昵称:" + (nickName == null ? "null" : nickName) + ",邮箱:" + (email == null ? "null" : email) + ",密码:" + (password == null ? "null" : password) + ",0:女 1:男 2:未知:" + (sex == null ? "null" : sex) + ",出生日期:" + (birthday == null ? "null" : birthday) + ",学校:" + (school == null ? "null" : school) + ",个人简介:" + (personIntroduction == null ? "null" : personIntroduction) + ",加入时间:" + (joinTime == null ? "null" : joinTime) + ",最后登录时间:" + (lastLoginTime == null ? "null" : lastLoginTime) + ",最后登录ip:" + (lastLoginIp == null ? "null" : lastLoginIp) + ",0:禁用 1:正常:" + (status == null ? "null" : status) + ",空间公告:" + (noticeInfo == null ? "null" : noticeInfo) + ",硬币总数量:" + (totalCoinCount == null ? "null" : totalCoinCount) + ",当前硬币数:" + (currentCoinCount == null ? "null" : currentCoinCount) + ",主题:" + (theme == null ? "null" : theme) + ",头像:" + (avatar == null ? "null" : avatar);
	}
}