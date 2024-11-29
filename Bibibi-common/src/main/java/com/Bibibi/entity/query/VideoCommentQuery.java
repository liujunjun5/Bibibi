package com.Bibibi.entity.query;

import java.util.Date;

/**
 * @Description:评论查询对象
 * @date:2024-11-20
 * @author: liujun
 */
public class VideoCommentQuery extends BaseQuery {
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

    private String videoIdFuzzy;

    /**
     * 视频用户ID
     */
    private String videoUserId;

    private String videoUserIdFuzzy;

    /**
     * 回复内容
     */
    private String content;

    private String contentFuzzy;

    /**
     * 图片
     */
    private String imgPath;

    private String imgPathFuzzy;

    /**
     * 用户ID
     */
    private String userId;

    private String userIdFuzzy;

    /**
     * 回复人ID
     */
    private String replyUserId;

    private String replyUserIdFuzzy;

    /**
     * 0:未置顶  1:置顶
     */
    private Integer topType;

    /**
     * 发布时间
     */
    private Date postTime;

    private String postTimeStart;

    private String postTimeEnd;

    /**
     * 喜欢数量
     */
    private Integer likeCount;

    /**
     * 讨厌数量
     */
    private Integer hateCount;

    private Boolean loadChildren;

    private Boolean queryVideoInfo;

    private String videoNameFuzzy;

    public String getVideoNameFuzzy() {
        return videoNameFuzzy;
    }

    public void setVideoNameFuzzy(String videoNameFuzzy) {
        this.videoNameFuzzy = videoNameFuzzy;
    }

    public Boolean getQueryVideoInfo() {
        return queryVideoInfo;
    }

    public void setQueryVideoInfo(Boolean queryVideoInfo) {
        this.queryVideoInfo = queryVideoInfo;
    }

    public Boolean getLoadChildren() {
        return loadChildren;
    }

    public void setLoadChildren(Boolean loadChildren) {
        this.loadChildren = loadChildren;
    }

    public String getVideoIdFuzzy() {
        return this.videoIdFuzzy;
    }

    public void setVideoIdFuzzy(String videoIdFuzzy) {
        this.videoIdFuzzy = videoIdFuzzy;
    }

    public String getVideoUserIdFuzzy() {
        return this.videoUserIdFuzzy;
    }

    public void setVideoUserIdFuzzy(String videoUserIdFuzzy) {
        this.videoUserIdFuzzy = videoUserIdFuzzy;
    }

    public String getContentFuzzy() {
        return this.contentFuzzy;
    }

    public void setContentFuzzy(String contentFuzzy) {
        this.contentFuzzy = contentFuzzy;
    }

    public String getImgPathFuzzy() {
        return this.imgPathFuzzy;
    }

    public void setImgPathFuzzy(String imgPathFuzzy) {
        this.imgPathFuzzy = imgPathFuzzy;
    }

    public String getUserIdFuzzy() {
        return this.userIdFuzzy;
    }

    public void setUserIdFuzzy(String userIdFuzzy) {
        this.userIdFuzzy = userIdFuzzy;
    }

    public String getReplyUserIdFuzzy() {
        return this.replyUserIdFuzzy;
    }

    public void setReplyUserIdFuzzy(String replyUserIdFuzzy) {
        this.replyUserIdFuzzy = replyUserIdFuzzy;
    }

    public String getPostTimeStart() {
        return this.postTimeStart;
    }

    public void setPostTimeStart(String postTimeStart) {
        this.postTimeStart = postTimeStart;
    }

    public String getPostTimeEnd() {
        return this.postTimeEnd;
    }

    public void setPostTimeEnd(String postTimeEnd) {
        this.postTimeEnd = postTimeEnd;
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

}