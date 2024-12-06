package com.Bibibi.web.controller;

import com.Bibibi.entity.dto.TokenUserInfoDto;
import com.Bibibi.entity.dto.UserMessageCountDto;
import com.Bibibi.entity.po.UserMessage;
import com.Bibibi.entity.query.UserMessageQuery;
import com.Bibibi.entity.vo.PaginationResultVO;
import com.Bibibi.entity.vo.ResponseVO;
import com.Bibibi.enums.MessageReadTypeEnum;
import com.Bibibi.exception.BusinessException;
import com.Bibibi.mappers.UserMessageMappers;
import com.Bibibi.service.UserMessageService;
import com.Bibibi.web.annotation.GlobalInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/message")
public class UserMessageController extends ABaseController {

    @Resource
    private UserMessageService userMessageService;
    @Autowired
    private UserMessageMappers userMessageMappers;

    @RequestMapping("/getNoReadCount")
    @GlobalInterceptor(checkLogin = true)
    public ResponseVO getNoReadCount() {
        TokenUserInfoDto tokenUserInfoDto = getTokenUserInfoDto();
        UserMessageQuery userMessageQuery = new UserMessageQuery();
        userMessageQuery.setUserId(tokenUserInfoDto.getUserId());
        userMessageQuery.setReadType(MessageReadTypeEnum.NO_READ.getType());
        Integer count = userMessageService.findCountByParam(userMessageQuery);
        return getSuccessResponseVO(count);
    }

    @RequestMapping("/getNoReadCountGroup")
    @GlobalInterceptor(checkLogin = true)
    public ResponseVO getNoReadCountGroup() {
        TokenUserInfoDto tokenUserInfoDto = getTokenUserInfoDto();
        List<UserMessageCountDto> countDtoList = userMessageService.getMessageTypeNoReadCount(tokenUserInfoDto.getUserId());
        return getSuccessResponseVO(countDtoList);
    }

    @RequestMapping("/readAll")
    @GlobalInterceptor(checkLogin = true)
    public ResponseVO readAll(Integer messageType) throws BusinessException {
        TokenUserInfoDto tokenUserInfoDto = getTokenUserInfoDto();

        UserMessageQuery userMessageQuery = new UserMessageQuery();
        userMessageQuery.setUserId(tokenUserInfoDto.getUserId());
        userMessageQuery.setMessageType(messageType);

        UserMessage userMessage = new UserMessage();
        userMessage.setReadType(MessageReadTypeEnum.READ.getType());
        userMessageService.updateByParam(userMessage, userMessageQuery);

        return getSuccessResponseVO(null);
    }

    @RequestMapping("/loadMessage")
    @GlobalInterceptor(checkLogin = true)
    public ResponseVO loadMessage(@NotNull Integer messageType, Integer pageNo) {
        TokenUserInfoDto tokenUserInfoDto = getTokenUserInfoDto();
        UserMessageQuery userMessageQuery = new UserMessageQuery();
        userMessageQuery.setUserId(tokenUserInfoDto.getUserId());
        userMessageQuery.setMessageType(messageType);
        userMessageQuery.setPageNo(pageNo);
        userMessageQuery.setOrderBy("message_id desc");
        PaginationResultVO<UserMessage> resultVO = userMessageService.findByPage(userMessageQuery);
        return getSuccessResponseVO(resultVO);
    }

    @RequestMapping("/delMessage")
    @GlobalInterceptor(checkLogin = true)
    public ResponseVO delMessage(@NotNull Integer messageId) {
        TokenUserInfoDto tokenUserInfoDto = getTokenUserInfoDto();
        UserMessageQuery userMessageQuery = new UserMessageQuery();
        userMessageQuery.setUserId(tokenUserInfoDto.getUserId());
        userMessageQuery.setMessageId(messageId);
        userMessageMappers.deleteByParam(userMessageQuery);
        return getSuccessResponseVO(null);
    }


}
