package com.Bibibi.entity.query;

import java.util.Date;

/**
 * @Description:用户行为 点赞、评论查询对象
 * @date:2024-11-20
 * @author: liujun
 */
public class UserActionQuery extends BaseQuery {
    /**
     * 自增ID
     */
    private Integer actionId;

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
     * 评论ID
     */
    private Integer commentId;

    /**
     * 0:评论喜欢点赞 1:讨厌评论 2:视频点赞 3:视频收藏 4:视频投币
     */
    private Integer actionType;

    /**
     * 数量
     */
    private Integer actionCount;

    /**
     * 用户ID
     */
    private String userId;

    private String userIdFuzzy;

    /**
     * 操作时间
     */
    private Date actionTime;

    private String actionTimeStart;

    private String actionTimeEnd;

    private Integer[] actionTypeArray;

    private Boolean queryVideoInfo;

    public Boolean getQueryVideoInfo() {
        return queryVideoInfo;
    }

    public void setQueryVideoInfo(Boolean queryVideoInfo) {
        this.queryVideoInfo = queryVideoInfo;
    }

    public Integer[] getActionTypeArray() {
        return actionTypeArray;
    }

    public void setActionTypeArray(Integer[] actionTypeArray) {
        this.actionTypeArray = actionTypeArray;
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

    public String getUserIdFuzzy() {
        return this.userIdFuzzy;
    }

    public void setUserIdFuzzy(String userIdFuzzy) {
        this.userIdFuzzy = userIdFuzzy;
    }

    public String getActionTimeStart() {
        return this.actionTimeStart;
    }

    public void setActionTimeStart(String actionTimeStart) {
        this.actionTimeStart = actionTimeStart;
    }

    public String getActionTimeEnd() {
        return this.actionTimeEnd;
    }

    public void setActionTimeEnd(String actionTimeEnd) {
        this.actionTimeEnd = actionTimeEnd;
    }

    public Integer getActionId() {
        return this.actionId;
    }

    public void setActionId(Integer actionId) {
        this.actionId = actionId;
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

    public Integer getCommentId() {
        return this.commentId;
    }

    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
    }

    public Integer getActionType() {
        return this.actionType;
    }

    public void setActionType(Integer actionType) {
        this.actionType = actionType;
    }

    public Integer getActionCount() {
        return this.actionCount;
    }

    public void setActionCount(Integer actionCount) {
        this.actionCount = actionCount;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getActionTime() {
        return this.actionTime;
    }

    public void setActionTime(Date actionTime) {
        this.actionTime = actionTime;
    }

}