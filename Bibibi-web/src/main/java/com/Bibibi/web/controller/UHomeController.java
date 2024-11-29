package com.Bibibi.web.controller;

import com.Bibibi.entity.dto.TokenUserInfoDto;
import com.Bibibi.entity.po.UserInfo;
import com.Bibibi.entity.vo.ResponseVO;
import com.Bibibi.entity.vo.UserInfoVO;
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

    @RequestMapping("/getUserInfo")
    public ResponseVO getUserInfo(@NotEmpty String userId) throws BusinessException {
        TokenUserInfoDto tokenUserInfoDto = getTokenUserInfoDto();
        UserInfo userInfo = userInfoService.getUserDetailInfo(tokenUserInfoDto == null ? null : tokenUserInfoDto.getUserId(), userId);
        UserInfoVO userInfoVO = CopyTools.copy(userInfo, UserInfoVO.class);

        return getSuccessResponseVO(userInfoVO);
    }

    @RequestMapping("updateUserInfo")
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

    @RequestMapping("/saveTheme")
    public ResponseVO saveTheme(@Min(1) @Max(10) @NotNull Integer theme) {
        TokenUserInfoDto tokenUserInfoDto = getTokenUserInfoDto();
        UserInfo userInfo = new UserInfo();
        userInfo.setTheme(theme);
        userInfoService.updateByUserId(userInfo, tokenUserInfoDto.getUserId());
        return getSuccessResponseVO(null);
    }
}
