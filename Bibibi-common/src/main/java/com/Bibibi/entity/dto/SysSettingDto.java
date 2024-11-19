package com.Bibibi.entity.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SysSettingDto implements Serializable {
    //注册送的硬币
    private Integer registerCoinCount = 10;
    //上传视频送的硬币
    private Integer postVideoCoinCount = 5;
    //文件大小
    private Integer videoSize = 100;
    //文件批数
    private Integer videoPCount = 10;
    //文件数
    private Integer videoCount = 10;
    //评论数
    private Integer commentCount = 20;
    //弹幕数
    private Integer danmuCount = 20;

    public Integer getRegisterCoinCount() {
        return registerCoinCount;
    }

    public void setRegisterCoinCount(Integer registerCoinCount) {
        this.registerCoinCount = registerCoinCount;
    }

    public Integer getPostVideoCoinCount() {
        return postVideoCoinCount;
    }

    public void setPostVideoCoinCount(Integer postVideoCoinCount) {
        this.postVideoCoinCount = postVideoCoinCount;
    }

    public Integer getVideoSize() {
        return videoSize;
    }

    public void setVideoSize(Integer videoSize) {
        this.videoSize = videoSize;
    }

    public Integer getVideoPCount() {
        return videoPCount;
    }

    public void setVideoPCount(Integer videoPCount) {
        this.videoPCount = videoPCount;
    }

    public Integer getVideoCount() {
        return videoCount;
    }

    public void setVideoCount(Integer videoCount) {
        this.videoCount = videoCount;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    public Integer getDanmuCount() {
        return danmuCount;
    }

    public void setDanmuCount(Integer danmuCount) {
        this.danmuCount = danmuCount;
    }
}
