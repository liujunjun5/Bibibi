package com.Bibibi.entity.po;

import java.io.Serializable;

/**
 * @Description:视频文件信息
 * @date:2024-11-11
 * @author: liujun
 */
public class VideoInfoFile implements Serializable {
	/**
	 * 唯一ID
	 */
	private String fileId;

	/**
	 * 用户ID
	 */
	private String userId;

	/**
	 * 视频ID
	 */
	private String videoId;

	/**
	 * 文件名
	 */
	private String fileName;

	/**
	 * 文件索引
	 */
	private Integer fileIndex;

	/**
	 * 文件大小
	 */
	private Long fileSize;

	/**
	 * 文件路径
	 */
	private String filePath;

	/**
	 * 持续时间（秒）
	 */
	private Integer duration;


	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void setVideoId(String videoId) {
		this.videoId = videoId;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public void setFileIndex(Integer fileIndex) {
		this.fileIndex = fileIndex;
	}

	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public String getFileId() {
		return this.fileId;
	}

	public String getUserId() {
		return this.userId;
	}

	public String getVideoId() {
		return this.videoId;
	}

	public String getFileName() {
		return this.fileName;
	}

	public Integer getFileIndex() {
		return this.fileIndex;
	}

	public Long getFileSize() {
		return this.fileSize;
	}

	public String getFilePath() {
		return this.filePath;
	}

	public Integer getDuration() {
		return this.duration;
	}

	@Override
	public String toString() {
		return "唯一ID:" + (fileId == null ? "null" : fileId) + ",用户ID:" + (userId == null ? "null" : userId) + ",视频ID:" + (videoId == null ? "null" : videoId) + ",文件名:" + (fileName == null ? "null" : fileName) + ",文件索引:" + (fileIndex == null ? "null" : fileIndex) + ",文件大小:" + (fileSize == null ? "null" : fileSize) + ",文件路径:" + (filePath == null ? "null" : filePath) + ",持续时间（秒）:" + (duration == null ? "null" : duration);
	}
}