package com.Bibibi.web.controller;

import com.Bibibi.entity.constants.Constants;
import com.Bibibi.entity.po.VideoDanmu;
import com.Bibibi.entity.po.VideoInfo;
import com.Bibibi.entity.query.VideoDanmuQuery;
import com.Bibibi.entity.vo.ResponseVO;
import com.Bibibi.exception.BusinessException;
import com.Bibibi.service.VideoDanmuService;
import com.Bibibi.service.VideoInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Date;

@Slf4j
@RequestMapping("/danmu")
@RestController
public class VideoDanmuController extends ABaseController {
    @Resource
    private VideoDanmuService videoDanmuService;
    @Resource
    private VideoInfoService videoInfoService;

    @RequestMapping("/postDanmu")
    public ResponseVO postDanmu(@NotEmpty String videoId, @NotEmpty String fileId, @NotEmpty @Size(max = 200) String text, @NotNull Integer mode, @NotEmpty String color, @NotNull Integer time) throws BusinessException {

        VideoDanmu videoDanmu = new VideoDanmu();
        videoDanmu.setVideoId(videoId);
        videoDanmu.setFileId(fileId);
        videoDanmu.setText(text);
        videoDanmu.setMode(mode);
        videoDanmu.setColor(color);
        videoDanmu.setTime(time);
        videoDanmu.setUserId(getTokenUserInfoDto().getUserId());
        videoDanmu.setPostTime(new Date());
        videoDanmuService.saveVideoDanmu(videoDanmu);
        return getSuccessResponseVO(null);
    }

    @RequestMapping("/loadDanmu")
    public ResponseVO loadDanmu(@NotEmpty String videoId, @NotEmpty String fileId) {
        VideoInfo videoInfo = videoInfoService.getByVideoId(videoId);
        if (videoInfo.getInteraction() != null && videoInfo.getInteraction().contains(Constants.ONE.toString())) {
            return getSuccessResponseVO(new ArrayList<>());
        }
        VideoDanmuQuery videoDanmuQuery = new VideoDanmuQuery();
        videoDanmuQuery.setVideoId(videoId);
        videoDanmuQuery.setFileId(fileId);
        videoDanmuQuery.setOrderBy("danmu_id asc");
        return getSuccessResponseVO(videoDanmuService.findListByParam(videoDanmuQuery));
    }
}
