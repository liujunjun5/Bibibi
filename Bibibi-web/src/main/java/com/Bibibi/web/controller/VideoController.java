package com.Bibibi.web.controller;

import com.Bibibi.component.EsSearchComponent;
import com.Bibibi.component.RedisComponent;
import com.Bibibi.entity.constants.Constants;
import com.Bibibi.entity.dto.TokenUserInfoDto;
import com.Bibibi.entity.po.UserAction;
import com.Bibibi.entity.po.VideoInfo;
import com.Bibibi.entity.po.VideoInfoFile;
import com.Bibibi.entity.query.UserActionQuery;
import com.Bibibi.entity.query.VideoInfoFileQuery;
import com.Bibibi.entity.query.VideoInfoQuery;
import com.Bibibi.entity.vo.PaginationResultVO;
import com.Bibibi.entity.vo.ResponseVO;
import com.Bibibi.entity.vo.VideoInfoResultVo;
import com.Bibibi.enums.*;
import com.Bibibi.exception.BusinessException;
import com.Bibibi.service.UserActionService;
import com.Bibibi.service.VideoInfoFileService;
import com.Bibibi.service.VideoInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Validated
@RequestMapping("/video")
@Slf4j
public class VideoController extends ABaseController{

    @Resource
    private VideoInfoService videoInfoService;

    @Resource
    private VideoInfoFileService videoInfoFileService;

    @Resource
    private UserActionService userActionService;

    @Resource
    private RedisComponent redisComponent;

    @Resource
    private EsSearchComponent esSearchComponent;

    @RequestMapping("/loadRecommendVideo")
    public ResponseVO loadRecommendVideo() {
        VideoInfoQuery videoInfoQuery = new VideoInfoQuery();
        videoInfoQuery.setQueryUserInfo(true);
        videoInfoQuery.setOrderBy("create_time desc");
        videoInfoQuery.setRecommendType(VideoRecommendTypeEnum.RECOMMEND.getType());
        List<VideoInfo> recommendVideoList = videoInfoService.findListByParam(videoInfoQuery);
        return getSuccessResponseVO(recommendVideoList);
    }

    @RequestMapping("/loadVideo")
    public ResponseVO loadVideo(Integer pCategoryId, Integer categoryId, Integer pageNo) {
        VideoInfoQuery videoInfoQuery = new VideoInfoQuery();
        videoInfoQuery.setCategoryId(categoryId);
        videoInfoQuery.setPCategoryId(pCategoryId);
        videoInfoQuery.setPageNo(pageNo);
        videoInfoQuery.setQueryUserInfo(true);
        videoInfoQuery.setOrderBy("create_time desc");
        videoInfoQuery.setRecommendType(VideoRecommendTypeEnum.NO_RECOMMEND.getType());
        PaginationResultVO resultVO = videoInfoService.findByPage(videoInfoQuery);
        return getSuccessResponseVO(resultVO);
    }

    @RequestMapping("/loadVideoPList")
    public ResponseVO loadVideoPList(@NotEmpty String videoId) {
        VideoInfoFileQuery videoInfoQuery = new VideoInfoFileQuery();
        videoInfoQuery.setVideoId(videoId);
        videoInfoQuery.setOrderBy("file_index asc");
        List<VideoInfoFile> fileList = videoInfoFileService.findListByParam(videoInfoQuery);
        return getSuccessResponseVO(fileList);
    }

    @RequestMapping("/getVideoInfo")
    public ResponseVO getVideoInfo(@NotEmpty String videoId) throws BusinessException {
        VideoInfo videoInfo = videoInfoService.getByVideoId(videoId);
        if (videoInfo == null) {
            throw new BusinessException(ResponseCodeEnum.CODE_404);
        }

        TokenUserInfoDto userInfoDto = getTokenUserInfoDto();
        List<UserAction> userActionList = new ArrayList<>();

        if (userInfoDto != null) {
            UserActionQuery actionQuery = new UserActionQuery();
            actionQuery.setVideoId(videoId);
            actionQuery.setUserId(userInfoDto.getUserId());
            actionQuery.setActionTypeArray(new Integer[]{UserActionTypeEnum.VIDEO_LIKE.getType(), UserActionTypeEnum.VIDEO_COLLECT.getType(),
                    UserActionTypeEnum.VIDEO_COIN.getType(),});
            userActionList = userActionService.findListByParam(actionQuery);
        }

        VideoInfoResultVo resultVo = new VideoInfoResultVo(videoInfo, new ArrayList<>());
        resultVo.setUserActionList(userActionList);
        return getSuccessResponseVO(resultVo);
    }

    @RequestMapping("/reportVideoPlayOnline")
    public ResponseVO reportVideoPlayOnline(@NotEmpty String fileId, @NotEmpty String deviceId) {
        return getSuccessResponseVO(redisComponent.reportVideoPlayOnline(fileId, deviceId));
    }

    @RequestMapping("/search")
    public ResponseVO search(@NotEmpty String keyword, Integer orderType, Integer pageNo) throws BusinessException {
        redisComponent.addKeywordCount(keyword);
        PaginationResultVO resultVO = esSearchComponent.search(true, keyword, orderType, pageNo, PageSize.SIZE30.getSize());
        return getSuccessResponseVO(resultVO);
    }

    @RequestMapping("/getSearchKeywordTop")
    public ResponseVO getSearchKeywordTop() throws BusinessException {
        //TODO 按时间段区分
        List<String> keywordTop = redisComponent.getKeywordTop(Constants.LENGTH_10);
        return getSuccessResponseVO(keywordTop);
    }

    @RequestMapping("/getVideoRecommend")
    public ResponseVO getVideoRecommend(@NotEmpty String keyword, @NotEmpty String videoId) throws BusinessException {
        List<VideoInfo> videoInfoList = esSearchComponent.search(false, keyword, SearchOrderTypeEnum.VIDEO_PLAY.getType(), Constants.ONE, PageSize.SIZE10.getSize()).getList();
        videoInfoList.stream().filter(item -> !item.getVideoId().equals(videoId)).collect(Collectors.toList());
        return getSuccessResponseVO(videoInfoList);
    }

    @RequestMapping("/loadHotVideoList")
    public ResponseVO loadHotVideoList(Integer pageNo) {
        VideoInfoQuery videoInfoQuery = new VideoInfoQuery();
        videoInfoQuery.setPageNo(pageNo);
        videoInfoQuery.setQueryUserInfo(true);
        videoInfoQuery.setOrderBy("play_count desc");
        videoInfoQuery.setLastPlayHour(Constants.HOUR_24);
        PaginationResultVO<VideoInfo> resultVO = videoInfoService.findByPage(videoInfoQuery);
        return getSuccessResponseVO(resultVO);
    }
}
