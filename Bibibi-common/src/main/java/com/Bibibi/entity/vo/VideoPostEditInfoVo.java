
package com.Bibibi.entity.vo;

import com.Bibibi.entity.po.VideoInfoFilePost;
import com.Bibibi.entity.po.VideoInfoPost;

import java.util.List;

public class VideoPostEditInfoVo {
    private VideoInfoPost videoInfo;
    private List<VideoInfoFilePost> videoInfoFileList;

    public VideoInfoPost getVideoInfo() {
        return videoInfo;
    }

    public void setVideoInfo(VideoInfoPost videoInfo) {
        this.videoInfo = videoInfo;
    }

    public List<VideoInfoFilePost> getVideoInfoFileList() {
        return videoInfoFileList;
    }

    public void setVideoInfoFileList(List<VideoInfoFilePost> videoInfoFileList) {
        this.videoInfoFileList = videoInfoFileList;
    }
}