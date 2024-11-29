package com.Bibibi.entity.po;

import java.io.Serializable;

/**
 * @Description:
 * @date:2024-11-29
 * @author: liujun
 */
public class UserVideoSeriesVideo implements Serializable {
	/**
	 * 列表ID
	 */
	private Integer seriesId;

	/**
	 * 视频ID
	 */
	private String videoId;

	/**
	 * 用户ID
	 */
	private String userId;

	/**
	 * 排序
	 */
	private Integer sort;


	public void setSeriesId(Integer seriesId) {
		this.seriesId = seriesId;
	}

	public void setVideoId(String videoId) {
		this.videoId = videoId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public Integer getSeriesId() {
		return this.seriesId;
	}

	public String getVideoId() {
		return this.videoId;
	}

	public String getUserId() {
		return this.userId;
	}

	public Integer getSort() {
		return this.sort;
	}

	@Override
	public String toString() {
		return "列表ID:" + (seriesId == null ? "null" : seriesId) + ",视频ID:" + (videoId == null ? "null" : videoId) + ",用户ID:" + (userId == null ? "null" : userId) + ",排序:" + (sort == null ? "null" : sort);
	}
}