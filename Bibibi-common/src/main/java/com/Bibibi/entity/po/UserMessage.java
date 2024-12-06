package com.Bibibi.entity.po;

import com.Bibibi.entity.dto.UserMessageExtendDto;
import com.Bibibi.utils.JsonUtils;
import com.Bibibi.utils.StringTools;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description:用户消息表
 * @date:2024-12-04
 * @author: liujun
 */
public class UserMessage implements Serializable {
    /**
     * 消息ID自增
     */
    private Integer messageId;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 主体ID
     */
    private String videoId;

    /**
     * 消息类型
     */
    private Integer messageType;

    /**
     * 发送人ID
     */
    private String sendUserId;

    /**
     * 0:未读 1:已读
     */
    private Integer readType;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 扩展信息
     */
    private String extendJson;

    private String videoName;

    private String sendUserName;

    private String videoCover;

    private String sendUserAvatar;

    private UserMessageExtendDto extendDto;

    public UserMessageExtendDto getExtendDto() {
        return StringTools.isEmpty(extendJson) ? new UserMessageExtendDto() : JsonUtils.convertJson2Obj(extendJson, UserMessageExtendDto.class);
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public String getSendUserName() {
        return sendUserName;
    }

    public void setSendUserName(String sendUserName) {
        this.sendUserName = sendUserName;
    }

    public String getVideoCover() {
        return videoCover;
    }

    public void setVideoCover(String videoCover) {
        this.videoCover = videoCover;
    }

    public String getSendUserAvatar() {
        return sendUserAvatar;
    }

    public void setSendUserAvatar(String sendUserAvatar) {
        this.sendUserAvatar = sendUserAvatar;
    }

    public void setMessageId(Integer messageId) {
        this.messageId = messageId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public void setMessageType(Integer messageType) {
        this.messageType = messageType;
    }

    public void setSendUserId(String sendUserId) {
        this.sendUserId = sendUserId;
    }

    public void setReadType(Integer readType) {
        this.readType = readType;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public void setExtendJson(String extendJson) {
        this.extendJson = extendJson;
    }

    public Integer getMessageId() {
        return this.messageId;
    }

    public String getUserId() {
        return this.userId;
    }

    public String getVideoId() {
        return this.videoId;
    }

    public Integer getMessageType() {
        return this.messageType;
    }

    public String getSendUserId() {
        return this.sendUserId;
    }

    public Integer getReadType() {
        return this.readType;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public String getExtendJson() {
        return this.extendJson;
    }

    @Override
    public String toString() {
        return "消息ID自增:" + (messageId == null ? "null" : messageId) + ",用户ID:" + (userId == null ? "null" : userId) + ",主体ID:" + (videoId == null ? "null" : videoId) + ",消息类型:" + (messageType == null ? "null" : messageType) + ",发送人ID:" + (sendUserId == null ? "null" : sendUserId) + ",0:未读 1:已读:" + (readType == null ? "null" : readType) + ",创建时间:" + (createTime == null ? "null" : createTime) + ",扩展信息:" + (extendJson == null ? "null" : extendJson);
    }
}