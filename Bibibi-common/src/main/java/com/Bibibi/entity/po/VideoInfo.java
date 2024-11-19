package com.Bibibi.entity.po;

import java.io.Serializable;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Description:视频信息
 * @date:2024-11-11
 * @author: liujun
 */
public class VideoInfo implements Serializable {
	/**
	 * 视频ID
	 */
	private String videoId;

	/**
	 * 视频封面
	 */
	private String videoCover;

	/**
	 * 视频名称
	 */
	private String videoName;

	/**
	 * 用户ID
	 */
	private String userId;

	/**
	 * 创建时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;

	/**
	 * 最后更新时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date lastUpdateTime;

	/**
	 * 父级分类ID
	 */
	private Integer pCategoryId;

	/**
	 * 分类ID
	 */
	private Integer categoryId;

	/**
	 * 0:自制作  1:转载
	 */
	private Integer postType;

	/**
	 * 原资源说明
	 */
	private String originInfo;

	/**
	 * 标签
	 */
	private String tags;

	/**
	 * 简介
	 */
	private String introduction;

	/**
	 * 互动设置
	 */
	private String interaction;

	/**
	 * 持续时间（秒）
	 */
	private Integer duration;

	/**
	 * 播放数量
	 */
	private Integer playCount;

	/**
	 * 点赞数量
	 */
	private Integer likeCount;

	/**
	 * 弹幕数量
	 */
	private Integer danmuCount;

	/**
	 * 评论数量
	 */
	private Integer commentCount;

	/**
	 * 投币数量
	 */
	private Integer coinCount;

	/**
	 * 收藏数量
	 */
	private Integer collectCount;

	/**
	 * 是否推荐0:未推荐  1:已推荐
	 */
	private Integer recommendType;

	/**
	 * 最后播放时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date lastPlayTime;

	/**
	 * 昵称
	 */
	private String nickName;

	/**
	 * 头像
	 */
	private String avatar;

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public void setVideoId(String videoId) {
		this.videoId = videoId;
	}

	public void setVideoCover(String videoCover) {
		this.videoCover = videoCover;
	}

	public void setVideoName(String videoName) {
		this.videoName = videoName;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public void setPCategoryId(Integer pCategoryId) {
		this.pCategoryId = pCategoryId;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}

	public void setPostType(Integer postType) {
		this.postType = postType;
	}

	public void setOriginInfo(String originInfo) {
		this.originInfo = originInfo;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public void setInteraction(String interaction) {
		this.interaction = interaction;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public void setPlayCount(Integer playCount) {
		this.playCount = playCount;
	}

	public void setLikeCount(Integer likeCount) {
		this.likeCount = likeCount;
	}

	public void setDanmuCount(Integer danmuCount) {
		this.danmuCount = danmuCount;
	}

	public void setCommentCount(Integer commentCount) {
		this.commentCount = commentCount;
	}

	public void setCoinCount(Integer coinCount) {
		this.coinCount = coinCount;
	}

	public void setCollectCount(Integer collectCount) {
		this.collectCount = collectCount;
	}

	public void setRecommendType(Integer recommendType) {
		this.recommendType = recommendType;
	}

	public void setLastPlayTime(Date lastPlayTime) {
		this.lastPlayTime = lastPlayTime;
	}

	public String getVideoId() {
		return this.videoId;
	}

	public String getVideoCover() {
		return this.videoCover;
	}

	public String getVideoName() {
		return this.videoName;
	}

	public String getUserId() {
		return this.userId;
	}

	public Date getCreateTime() {
		return this.createTime;
	}

	public Date getLastUpdateTime() {
		return this.lastUpdateTime;
	}

	public Integer getPCategoryId() {
		return this.pCategoryId;
	}

	public Integer getCategoryId() {
		return this.categoryId;
	}

	public Integer getPostType() {
		return this.postType;
	}

	public String getOriginInfo() {
		return this.originInfo;
	}

	public String getTags() {
		return this.tags;
	}

	public String getIntroduction() {
		return this.introduction;
	}

	public String getInteraction() {
		return this.interaction;
	}

	public Integer getDuration() {
		return this.duration;
	}

	public Integer getPlayCount() {
		return this.playCount;
	}

	public Integer getLikeCount() {
		return this.likeCount;
	}

	public Integer getDanmuCount() {
		return this.danmuCount;
	}

	public Integer getCommentCount() {
		return this.commentCount;
	}

	public Integer getCoinCount() {
		return this.coinCount;
	}

	public Integer getCollectCount() {
		return this.collectCount;
	}

	public Integer getRecommendType() {
		return this.recommendType;
	}

	public Date getLastPlayTime() {
		return this.lastPlayTime;
	}

	@Override
	public String toString() {
		return "视频ID:" + (videoId == null ? "null" : videoId) + ",视频封面:" + (videoCover == null ? "null" : videoCover) + ",视频名称:" + (videoName == null ? "null" : videoName) + ",用户ID:" + (userId == null ? "null" : userId) + ",创建时间:" + (createTime == null ? "null" : createTime) + ",最后更新时间:" + (lastUpdateTime == null ? "null" : lastUpdateTime) + ",父级分类ID:" + (pCategoryId == null ? "null" : pCategoryId) + ",分类ID:" + (categoryId == null ? "null" : categoryId) + ",0:自制作  1:转载:" + (postType == null ? "null" : postType) + ",原资源说明:" + (originInfo == null ? "null" : originInfo) + ",标签:" + (tags == null ? "null" : tags) + ",简介:" + (introduction == null ? "null" : introduction) + ",互动设置:" + (interaction == null ? "null" : interaction) + ",持续时间（秒）:" + (duration == null ? "null" : duration) + ",播放数量:" + (playCount == null ? "null" : playCount) + ",点赞数量:" + (likeCount == null ? "null" : likeCount) + ",弹幕数量:" + (danmuCount == null ? "null" : danmuCount) + ",评论数量:" + (commentCount == null ? "null" : commentCount) + ",投币数量:" + (coinCount == null ? "null" : coinCount) + ",收藏数量:" + (collectCount == null ? "null" : collectCount) + ",是否推荐0:未推荐  1:已推荐:" + (recommendType == null ? "null" : recommendType) + ",最后播放时间:" + (lastPlayTime == null ? "null" : lastPlayTime);
	}
}