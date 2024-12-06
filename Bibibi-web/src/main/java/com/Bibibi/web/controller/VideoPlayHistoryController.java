package com.Bibibi.web.controller;

import com.Bibibi.entity.dto.TokenUserInfoDto;
import com.Bibibi.entity.query.VideoPlayHistoryQuery;
import com.Bibibi.entity.vo.ResponseVO;
import com.Bibibi.service.VideoPlayHistoryService;
import com.Bibibi.web.annotation.GlobalInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;

@RequestMapping("/history")
@Slf4j
@RestController
public class VideoPlayHistoryController extends ABaseController {

    @Resource
    private VideoPlayHistoryService videoPlayHistoryService;

    @RequestMapping("/loadHistory")
    @GlobalInterceptor(checkLogin = true)
    public ResponseVO loadHistory(Integer pageNo) {
        TokenUserInfoDto tokenUserInfoDto = getTokenUserInfoDto();
        VideoPlayHistoryQuery historyQuery = new VideoPlayHistoryQuery();
        historyQuery.setUserId(tokenUserInfoDto.getUserId());
        historyQuery.setOrderBy("last_update_time desc");
        historyQuery.setPageNo(pageNo);
        historyQuery.setQueryVideoDetail(true);
        return getSuccessResponseVO(videoPlayHistoryService.findByPage(historyQuery));
    }

    @RequestMapping("/cleanHistory")
    @GlobalInterceptor(checkLogin = true)
    public ResponseVO cleanHistory() {
        TokenUserInfoDto tokenUserInfoDto = getTokenUserInfoDto();
        VideoPlayHistoryQuery historyQuery = new VideoPlayHistoryQuery();
        historyQuery.setUserId(tokenUserInfoDto.getUserId());
        videoPlayHistoryService.deleteByParam(historyQuery);
        return getSuccessResponseVO(null);
    }

    @RequestMapping("/delHistory")
    @GlobalInterceptor(checkLogin = true)
    public ResponseVO delHistory(@NotEmpty String videoId) {
        TokenUserInfoDto tokenUserInfoDto = getTokenUserInfoDto();
        videoPlayHistoryService.deleteByUserIdAndVideoId(tokenUserInfoDto.getUserId(), videoId);
        return getSuccessResponseVO(null);
    }
}
