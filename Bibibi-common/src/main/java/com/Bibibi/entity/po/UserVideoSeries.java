package com.Bibibi.entity.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Description:用户视频序列归档
 * @date:2024-11-29
 * @author: liujun
 */
public class UserVideoSeries implements Serializable {
	/**
	 * 列表ID
	 */
	private Integer seriesId;

	/**
	 * 列表名称
	 */
	private String seriesName;

	/**
	 * 描述
	 */
	private String seriesDescription;

	/**
	 * 用户ID
	 */
	private String userId;

	/**
	 * 排序
	 */
	private Integer sort;

	/**
	 * 更新时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date updateTime;


	private String cover;

	/**
	 * 专题下的视频
	 */
	private List<VideoInfo> videoInfoList;

	public List<VideoInfo> getVideoInfoList() {
		return videoInfoList;
	}

	public void setVideoInfoList(List<VideoInfo> videoInfoList) {
		this.videoInfoList = videoInfoList;
	}

	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}

	public void setSeriesId(Integer seriesId) {
		this.seriesId = seriesId;
	}

	public void setSeriesName(String seriesName) {
		this.seriesName = seriesName;
	}

	public void setSeriesDescription(String seriesDescription) {
		this.seriesDescription = seriesDescription;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Integer getSeriesId() {
		return this.seriesId;
	}

	public String getSeriesName() {
		return this.seriesName;
	}

	public String getSeriesDescription() {
		return this.seriesDescription;
	}

	public String getUserId() {
		return this.userId;
	}

	public Integer getSort() {
		return this.sort;
	}

	public Date getUpdateTime() {
		return this.updateTime;
	}



	@Override
	public String toString() {
		return "列表ID:" + (seriesId == null ? "null" : seriesId) + ",列表名称:" + (seriesName == null ? "null" : seriesName) + ",描述:" + (seriesDescription == null ? "null" : seriesDescription) + ",用户ID:" + (userId == null ? "null" : userId) + ",排序:" + (sort == null ? "null" : sort) + ",更新时间:" + (updateTime == null ? "null" : updateTime);
	}

}