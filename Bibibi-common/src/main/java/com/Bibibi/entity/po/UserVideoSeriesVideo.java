package com.Bibibi.entity.po;

import java.io.Serializable;
import java.util.Date;

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

	private String videoCover;

	private String videoName;

	private Integer playCount;

	private Date createTime;



	public String getVideoCover() {
		return videoCover;
	}

	public void setVideoCover(String videoCover) {
		this.videoCover = videoCover;
	}

	public String getVideoName() {
		return videoName;
	}

	public void setVideoName(String videoName) {
		this.videoName = videoName;
	}

	public Integer getPlayCount() {
		return playCount;
	}

	public void setPlayCount(Integer playCount) {
		this.playCount = playCount;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

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