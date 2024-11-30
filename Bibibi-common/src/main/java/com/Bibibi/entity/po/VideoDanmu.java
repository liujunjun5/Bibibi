package com.Bibibi.entity.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description:视频弹幕
 * @date:2024-11-20
 * @author: liujun
 */
public class VideoDanmu implements Serializable {
    /**
     * 自增ID
     */
    private Integer danmuId;

    /**
     * 视频ID
     */
    private String videoId;

    /**
     * 唯一ID
     */
    private String fileId;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 发布时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date postTime;

    /**
     * 内容
     */
    private String text;

    /**
     * 展示位置
     */
    private Integer mode;

    /**
     * 颜色
     */
    private String color;

    /**
     * 展示时间
     */
    private Integer time;

    private String videoName;

    private String videoCover;

    private String nickName;

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

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
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

    @Override
    public String toString() {
        return "自增ID:" + (danmuId == null ? "null" : danmuId) + ",视频ID:" + (videoId == null ? "null" : videoId) + ",唯一ID:" + (fileId == null ? "null" : fileId) + ",用户ID:" + (userId == null ? "null" : userId) + ",发布时间:" + (postTime == null ? "null" : postTime) + ",内容:" + (text == null ? "null" : text) + ",展示位置:" + (mode == null ? "null" : mode) + ",颜色:" + (color == null ? "null" : color) + ",展示时间:" + (time == null ? "null" : time);
    }
}