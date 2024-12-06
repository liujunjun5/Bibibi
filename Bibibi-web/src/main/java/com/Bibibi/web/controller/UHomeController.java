package com.Bibibi.web.controller;

import com.Bibibi.web.annotation.GlobalInterceptor;
import com.Bibibi.entity.constants.Constants;
import com.Bibibi.entity.dto.TokenUserInfoDto;
import com.Bibibi.entity.po.UserAction;
import com.Bibibi.entity.po.UserInfo;
import com.Bibibi.entity.po.VideoInfo;
import com.Bibibi.entity.query.UserActionQuery;
import com.Bibibi.entity.query.UserFocusQuery;
import com.Bibibi.entity.query.VideoInfoQuery;
import com.Bibibi.entity.vo.PaginationResultVO;
import com.Bibibi.entity.vo.ResponseVO;
import com.Bibibi.entity.vo.UserInfoVO;
import com.Bibibi.enums.PageSize;
import com.Bibibi.enums.UserActionTypeEnum;
import com.Bibibi.enums.VideoOrderTypeEnum;
import com.Bibibi.exception.BusinessException;
import com.Bibibi.service.UserActionService;
import com.Bibibi.service.UserFocusService;
import com.Bibibi.service.UserInfoService;
import com.Bibibi.service.VideoInfoService;
import com.Bibibi.utils.CopyTools;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.*;

@RequestMapping("/uhome")
@RestController
@Validated
public class UHomeController extends ABaseController {
    @Resource
    private UserInfoService userInfoService;

    @Resource
    private VideoInfoService videoInfoService;

    @Resource
    private UserFocusService userFocusService;

    @Resource
    private UserActionService userActionService;

    @GlobalInterceptor(checkLogin = true)
    @RequestMapping("/getUserInfo")
    public ResponseVO getUserInfo(@NotEmpty String userId) throws BusinessException {
        TokenUserInfoDto tokenUserInfoDto = getTokenUserInfoDto();
        UserInfo userInfo = userInfoService.getUserDetailInfo(tokenUserInfoDto == null ? null : tokenUserInfoDto.getUserId(), userId);
        UserInfoVO userInfoVO = CopyTools.copy(userInfo, UserInfoVO.class);

        return getSuccessResponseVO(userInfoVO);
    }

    @GlobalInterceptor(checkLogin = true)
    @RequestMapping("/updateUserInfo")
    public ResponseVO updateUserInfo(@NotEmpty @Size(max = 20) String nickName,
                                     @NotEmpty @Size(max = 100) String avatar,
                                     @NotNull Integer sex,
                                     @Size(max = 10) String birthday,
                                     @Size(max = 150) String school,
                                     @Size(max = 80) String personIntroduction,
                                     @Size(max = 300) String noticeInfo) throws BusinessException {
        TokenUserInfoDto tokenUserInfoDto = getTokenUserInfoDto();

        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(tokenUserInfoDto.getUserId());
        userInfo.setNickName(nickName);
        userInfo.setAvatar(avatar);
        userInfo.setSex(sex);
        userInfo.setBirthday(birthday);
        userInfo.setSchool(school);
        userInfo.setPersonIntroduction(personIntroduction);
        userInfo.setNoticeInfo(noticeInfo);

        userInfoService.updateUserInfo(userInfo, tokenUserInfoDto);
        return getSuccessResponseVO(null);
    }

    @GlobalInterceptor(checkLogin = true)
    @RequestMapping("/saveTheme")
    public ResponseVO saveTheme(@Min(1) @Max(10) @NotNull Integer theme) {
        TokenUserInfoDto tokenUserInfoDto = getTokenUserInfoDto();
        UserInfo userInfo = new UserInfo();
        userInfo.setTheme(theme);
        userInfoService.updateByUserId(userInfo, tokenUserInfoDto.getUserId());
        return getSuccessResponseVO(null);
    }

    @GlobalInterceptor(checkLogin = true)
    @RequestMapping("/focus")
//    @GlobalInterceptor(checkLogin = true)
    public ResponseVO focus(@NotEmpty String focusUserId) throws BusinessException {
        userFocusService.focusUser(getTokenUserInfoDto().getUserId(), focusUserId);
        return getSuccessResponseVO(null);
    }

    @GlobalInterceptor(checkLogin = true)
    @RequestMapping("/cancelFocus")
    public ResponseVO cancelFocus(@NotEmpty String focusUserId) {
        userFocusService.cancelFocus(getTokenUserInfoDto().getUserId(), focusUserId);
        return getSuccessResponseVO(null);
    }

    @GlobalInterceptor(checkLogin = true)
    @RequestMapping("/loadFocusList")
    public ResponseVO loadFocusList(Integer pageNo) {
        TokenUserInfoDto tokenUserInfoDto = getTokenUserInfoDto();

        UserFocusQuery focusQuery = new UserFocusQuery();
        focusQuery.setUserId(tokenUserInfoDto.getUserId());
        focusQuery.setPageNo(pageNo);
        focusQuery.setOrderBy("focus_time desc");
        focusQuery.setQueryType(Constants.ZERO);
        PaginationResultVO resultVO = userFocusService.findByPage(focusQuery);
        return getSuccessResponseVO(resultVO);
    }

    @GlobalInterceptor(checkLogin = true)
    @RequestMapping("/loadFansList")
    public ResponseVO loadFansList(Integer pageNo) {
        TokenUserInfoDto tokenUserInfoDto = getTokenUserInfoDto();

        UserFocusQuery focusQuery = new UserFocusQuery();
        focusQuery.setFocusUserId(tokenUserInfoDto.getUserId());
        focusQuery.setPageNo(pageNo);
        focusQuery.setOrderBy("focus_time desc");
        focusQuery.setQueryType(Constants.ONE);
        PaginationResultVO resultVO = userFocusService.findByPage(focusQuery);
        return getSuccessResponseVO(resultVO);
    }

    @GlobalInterceptor(checkLogin = true)
    @RequestMapping("/loadVideoList")
    public ResponseVO loadVideoList(@NotEmpty String userId, Integer type, Integer pageNo, String videoName, Integer orderType) {
        VideoInfoQuery infoQuery = new VideoInfoQuery();
        if (type != null) {
            infoQuery.setPageSize(PageSize.SIZE10.getSize());
        }
        VideoOrderTypeEnum videoOrderTypeEnum = VideoOrderTypeEnum.getByType(orderType);
        if (videoOrderTypeEnum == null) {
            videoOrderTypeEnum = VideoOrderTypeEnum.CREATE_TIME;
        }
        infoQuery.setOrderBy(videoOrderTypeEnum.getField() + " desc");
        infoQuery.setVideoNameFuzzy(videoName);
        infoQuery.setPageNo(pageNo);
        infoQuery.setUserId(userId);
        PaginationResultVO<VideoInfo> resultVO = videoInfoService.findByPage(infoQuery);
        return getSuccessResponseVO(resultVO);
    }

    @GlobalInterceptor(checkLogin = true)
    @RequestMapping("/loadUserCollection")
    public ResponseVO loadUserCollection(@NotEmpty String userId, Integer pageNo) {
        UserActionQuery actionQuery = new UserActionQuery();
        actionQuery.setActionType(UserActionTypeEnum.VIDEO_COLLECT.getType());
        actionQuery.setUserId(userId);
        actionQuery.setPageNo(pageNo);
        actionQuery.setOrderBy("action_time desc");
        actionQuery.setQueryVideoInfo(true);
        PaginationResultVO<UserAction> resultVO = userActionService.findByPage(actionQuery);
        return getSuccessResponseVO(resultVO);
    }
}
