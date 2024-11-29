package com.Bibibi.entity.query;

import java.util.Date;

/**
 * @Description:视频弹幕查询对象
 * @date:2024-11-20
 * @author: liujun
 */
public class VideoDanmuQuery extends BaseQuery {
    /**
     * 自增ID
     */
    private Integer danmuId;

    /**
     * 视频ID
     */
    private String videoId;

    private String videoIdFuzzy;

    /**
     * 唯一ID
     */
    private String fileId;

    private String fileIdFuzzy;

    /**
     * 用户ID
     */
    private String userId;

    private String userIdFuzzy;

    /**
     * 发布时间
     */
    private Date postTime;

    private String postTimeStart;

    private String postTimeEnd;

    /**
     * 内容
     */
    private String text;

    private String textFuzzy;

    /**
     * 展示位置
     */
    private Integer mode;

    /**
     * 颜色
     */
    private String color;

    private String colorFuzzy;

    /**
     * 展示时间
     */
    private Integer time;

    private Boolean queryVideoInfo;

    private String videoUserId;

    private String videoNameFuzzy;

    public Boolean getQueryVideoInfo() {
        return queryVideoInfo;
    }

    public void setQueryVideoInfo(Boolean queryVideoInfo) {
        this.queryVideoInfo = queryVideoInfo;
    }

    public String getVideoUserId() {
        return videoUserId;
    }

    public void setVideoUserId(String videoUserId) {
        this.videoUserId = videoUserId;
    }

    public String getVideoNameFuzzy() {
        return videoNameFuzzy;
    }

    public void setVideoNameFuzzy(String videoNameFuzzy) {
        this.videoNameFuzzy = videoNameFuzzy;
    }

    public String getVideoIdFuzzy() {
        return this.videoIdFuzzy;
    }

    public void setVideoIdFuzzy(String videoIdFuzzy) {
        this.videoIdFuzzy = videoIdFuzzy;
    }

    public String getFileIdFuzzy() {
        return this.fileIdFuzzy;
    }

    public void setFileIdFuzzy(String fileIdFuzzy) {
        this.fileIdFuzzy = fileIdFuzzy;
    }

    public String getUserIdFuzzy() {
        return this.userIdFuzzy;
    }

    public void setUserIdFuzzy(String userIdFuzzy) {
        this.userIdFuzzy = userIdFuzzy;
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

    public String getTextFuzzy() {
        return this.textFuzzy;
    }

    public void setTextFuzzy(String textFuzzy) {
        this.textFuzzy = textFuzzy;
    }

    public String getColorFuzzy() {
        return this.colorFuzzy;
    }

    public void setColorFuzzy(String colorFuzzy) {
        this.colorFuzzy = colorFuzzy;
    }

    public Integer getDanmuId() {
        return this.danmuId;
    }

    public void setDanmuId(Integer danmuId) {
        this.danmuId = danmuId;
    }

    public String getVideoId() {
        return this.videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getFileId() {
        return this.fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getPostTime() {
        return this.postTime;
    }

    public void setPostTime(Date postTime) {
        this.postTime = postTime;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getMode() {
        return this.mode;
    }

    public void setMode(Integer mode) {
        this.mode = mode;
    }

    public String getColor() {
        return this.color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Integer getTime() {
        return this.time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

}