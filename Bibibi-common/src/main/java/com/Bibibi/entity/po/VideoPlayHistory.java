package com.Bibibi.entity.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description:视频播放历史
 * @date:2024-12-04
 * @author: liujun
 */
public class VideoPlayHistory implements Serializable {
	/**
	 * 用户ID
	 */
	private String userId;

	/**
	 * 视频ID
	 */
	private String videoId;

	/**
	 * 文件索引
	 */
	private Integer fileIndex;

	/**
	 * 最后更新时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date lastUpdateTime;

	/**
	 * 视频封面
	 */
	private String videoCover;

	/**
	 * 视频名称
	 */
	private String videoName;

	public String getVideoName() {
		return videoName;
	}

	public void setVideoName(String videoName) {
		this.videoName = videoName;
	}

	public String getVideoCover() {
		return videoCover;
	}

	public void setVideoCover(String videoCover) {
		this.videoCover = videoCover;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void setVideoId(String videoId) {
		this.videoId = videoId;
	}

	public void setFileIndex(Integer fileIndex) {
		this.fileIndex = fileIndex;
	}

	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public String getUserId() {
		return this.userId;
	}

	public String getVideoId() {
		return this.videoId;
	}

	public Integer getFileIndex() {
		return this.fileIndex;
	}

	public Date getLastUpdateTime() {
		return this.lastUpdateTime;
	}

	@Override
	public String toString() {
		return "用户ID:" + (userId == null ? "null" : userId) + ",视频ID:" + (videoId == null ? "null" : videoId) + ",文件索引:" + (fileIndex == null ? "null" : fileIndex) + ",最后更新时间:" + (lastUpdateTime == null ? "null" : lastUpdateTime);
	}
}