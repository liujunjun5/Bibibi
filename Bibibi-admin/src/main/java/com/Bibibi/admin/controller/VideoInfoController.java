package com.Bibibi.admin.controller;

import com.Bibibi.entity.query.VideoInfoPostQuery;
import com.Bibibi.entity.vo.PaginationResultVO;
import com.Bibibi.entity.vo.ResponseVO;
import com.Bibibi.exception.BusinessException;
import com.Bibibi.service.VideoInfoFilePostService;
import com.Bibibi.service.VideoInfoPostService;
import com.Bibibi.service.VideoInfoService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@RestController
@Validated
@RequestMapping("/videoInfo")
public class VideoInfoController extends ABaseController {

    @Resource
    private VideoInfoPostService videoInfoPostService;

    @Resource
    private VideoInfoFilePostService videoInfoFilePostService;

    @Resource
    private VideoInfoService videoInfoService;

    @RequestMapping("/loadVideoList")
    public ResponseVO loadVideoList(VideoInfoPostQuery videoInfoPostQuery) {
        videoInfoPostQuery.setOrderBy("v.last_update_time desc");
        videoInfoPostQuery.setQueryCountInfo(true);
        videoInfoPostQuery.setQueryUserInfo(true);
        PaginationResultVO resultVO = videoInfoPostService.findByPage(videoInfoPostQuery);
        return getSuccessResponseVO(resultVO);
    }

    @RequestMapping("/auditVideo")
    public ResponseVO auditVideo(@NotEmpty String videoId, @NotNull Integer status, String reason) throws BusinessException {
        videoInfoPostService.auditVideo(videoId, status, reason);
        return getSuccessResponseVO(null);
    }
}
