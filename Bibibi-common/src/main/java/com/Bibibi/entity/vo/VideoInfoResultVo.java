package com.Bibibi.entity.vo;

import com.Bibibi.entity.po.UserAction;
import com.Bibibi.entity.po.VideoInfo;

import java.util.List;

public class VideoInfoResultVo {
    private VideoInfo videoInfo;
    private List userActionList;



    public VideoInfoResultVo(VideoInfo videoInfo, List userActionList) {
        this.videoInfo = videoInfo;
        this.userActionList = userActionList;
    }

    public List getUserActionList() {
        return userActionList;
    }

    public void setUserActionList(List userActionList) {
        this.userActionList = userActionList;
    }

    public VideoInfo getVideoInfo() {
        return videoInfo;
    }

    public void setVideoInfo(VideoInfo videoInfo) {
        this.videoInfo = videoInfo;
    }
}
