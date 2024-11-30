package com.Bibibi.entity.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Description:评论
 * @date:2024-11-20
 * @author: liujun
 */
public class VideoComment implements Serializable {
    public List<VideoComment> children;
    /**
     * 评论ID
     */
    private Integer commentId;
    /**
     * 父级评论ID
     */
    private Integer pCommentId;
    /**
     * 视频ID
     */
    private String videoId;
    /**
     * 视频用户ID
     */
    private String videoUserId;
    /**
     * 回复内容
     */
    private String content;
    /**
     * 图片
     */
    private String imgPath;
    /**
     * 用户ID
     */
    private String userId;
    /**
     * 回复人ID
     */
    private String replyUserId;
    /**
     * 0:未置顶  1:置顶
     */
    private Integer topType;
    /**
     * 发布时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date postTime;

    /**
     * 喜欢数量
     */
    private Integer likeCount;

    /**
     * 讨厌数量
     */
    private Integer hateCount;

    private String avatar;

    private String nickName;

    private String replyAvatar;

    private String replyNickName;

    private String videoName;

    private String videoCover;

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

    public List<VideoComment> getChildren() {
        return children;
    }

    public void setChildren(List<VideoComment> children) {
        this.children = children;
    }

    public String getReplyNickName() {
        return replyNickName;
    }

    public void setReplyNickName(String replyNickName) {
        this.replyNickName = replyNickName;
    }

    public String getReplyAvatar() {
        return replyAvatar;
    }

    public void setReplyAvatar(String replyAvatar) {
        this.replyAvatar = replyAvatar;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Integer getCommentId() {
        return this.commentId;
    }

    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
    }

    public Integer getPCommentId() {
        return this.pCommentId;
    }

    public void setPCommentId(Integer pCommentId) {
        this.pCommentId = pCommentId;
    }

    public String getVideoId() {
        return this.videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getVideoUserId() {
        return this.videoUserId;
    }

    public void setVideoUserId(String videoUserId) {
        this.videoUserId = videoUserId;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImgPath() {
        return this.imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getReplyUserId() {
        return this.replyUserId;
    }

    public void setReplyUserId(String replyUserId) {
        this.replyUserId = replyUserId;
    }

    public Integer getTopType() {
        return this.topType;
    }

    public void setTopType(Integer topType) {
        this.topType = topType;
    }

    public Date getPostTime() {
        return this.postTime;
    }

    public void setPostTime(Date postTime) {
        this.postTime = postTime;
    }

    public Integer getLikeCount() {
        return this.likeCount;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

    public Integer getHateCount() {
        return this.hateCount;
    }

    public void setHateCount(Integer hateCount) {
        this.hateCount = hateCount;
    }

    @Override
    public String toString() {
        return "评论ID:" + (commentId == null ? "null" : commentId) + ",父级评论ID:" + (pCommentId == null ? "null" : pCommentId) + ",视频ID:" + (videoId == null ? "null" : videoId) + ",视频用户ID:" + (videoUserId == null ? "null" : videoUserId) + ",回复内容:" + (content == null ? "null" : content) + ",图片:" + (imgPath == null ? "null" : imgPath) + ",用户ID:" + (userId == null ? "null" : userId) + ",回复人ID:" + (replyUserId == null ? "null" : replyUserId) + ",0:未置顶  1:置顶:" + (topType == null ? "null" : topType) + ",发布时间:" + (postTime == null ? "null" : postTime) + ",喜欢数量:" + (likeCount == null ? "null" : likeCount) + ",讨厌数量:" + (hateCount == null ? "null" : hateCount);
    }
}