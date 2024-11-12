package com.Bibibi.entity.po;

import java.io.Serializable;

/**
 * @Description:视频文件信息
 * @date:2024-11-11
 * @author: liujun
 */
public class VideoInfoFilePost implements Serializable {
	/**
	 * 唯一ID
	 */
	private String fileId;

	/**
	 * 上传ID
	 */
	private String uploadId;

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
	 * 文件名
	 */
	private String fileName;

	/**
	 * 文件大小
	 */
	private Long fileSize;

	/**
	 * 文件路径
	 */
	private String filePath;

	/**
	 * 0:无更新 1:有更新
	 */
	private Integer updateType;

	/**
	 * 0:转码中 1:转码成功 2:转码失败
	 */
	private Integer transferResult;

	/**
	 * 持续时间（秒）
	 */
	private Integer duration;


	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public void setUploadId(String uploadId) {
		this.uploadId = uploadId;
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

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public void setUpdateType(Integer updateType) {
		this.updateType = updateType;
	}

	public void setTransferResult(Integer transferResult) {
		this.transferResult = transferResult;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public String getFileId() {
		return this.fileId;
	}

	public String getUploadId() {
		return this.uploadId;
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

	public String getFileName() {
		return this.fileName;
	}

	public Long getFileSize() {
		return this.fileSize;
	}

	public String getFilePath() {
		return this.filePath;
	}

	public Integer getUpdateType() {
		return this.updateType;
	}

	public Integer getTransferResult() {
		return this.transferResult;
	}

	public Integer getDuration() {
		return this.duration;
	}

	@Override
	public String toString() {
		return "唯一ID:" + (fileId == null ? "null" : fileId) + ",上传ID:" + (uploadId == null ? "null" : uploadId) + ",用户ID:" + (userId == null ? "null" : userId) + ",视频ID:" + (videoId == null ? "null" : videoId) + ",文件索引:" + (fileIndex == null ? "null" : fileIndex) + ",文件名:" + (fileName == null ? "null" : fileName) + ",文件大小:" + (fileSize == null ? "null" : fileSize) + ",文件路径:" + (filePath == null ? "null" : filePath) + ",0:无更新 1:有更新:" + (updateType == null ? "null" : updateType) + ",0:转码中 1:转码成功 2:转码失败:" + (transferResult == null ? "null" : transferResult) + ",持续时间（秒）:" + (duration == null ? "null" : duration);
	}
}