package com.Bibibi.web.controller;

import com.Bibibi.annotation.RecordUserMessage;
import com.Bibibi.enums.MessageTypeEnum;
import com.Bibibi.web.annotation.GlobalInterceptor;
import com.Bibibi.entity.constants.Constants;
import com.Bibibi.entity.po.UserAction;
import com.Bibibi.entity.vo.ResponseVO;
import com.Bibibi.exception.BusinessException;
import com.Bibibi.service.UserActionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@RequestMapping("userAction")
@RestController
@Validated
@Slf4j
public class UserActionController extends ABaseController {

    @Resource
    private UserActionService userActionService;

    @GlobalInterceptor(checkLogin = true)
    @RequestMapping("doAction")
    @RecordUserMessage(messageType = MessageTypeEnum.LIKE)
    public ResponseVO doAction(@NotEmpty String videoId,
                               @NotNull Integer actionType,
                               @Max(2) @Min(1) Integer actionCount,
                               Integer commentId) throws BusinessException {
        UserAction userAction = new UserAction();
        userAction.setUserId(getTokenUserInfoDto().getUserId());
        userAction.setVideoId(videoId);
        userAction.setActionType(actionType);
        actionCount = actionCount == null ? Constants.ONE : actionCount;
        userAction.setActionCount(actionCount);
        commentId = commentId == null ? 0 : commentId;
        userAction.setCommentId(commentId);
        userActionService.saveAction(userAction);
        return getSuccessResponseVO(null);
    }
}
