package com.Bibibi.web.controller;

import com.Bibibi.entity.constants.Constants;
import com.Bibibi.entity.dto.TokenUserInfoDto;
import com.Bibibi.entity.po.UserAction;
import com.Bibibi.entity.po.VideoComment;
import com.Bibibi.entity.po.VideoInfo;
import com.Bibibi.entity.query.UserActionQuery;
import com.Bibibi.entity.query.VideoCommentQuery;
import com.Bibibi.entity.vo.PaginationResultVO;
import com.Bibibi.entity.vo.ResponseVO;
import com.Bibibi.entity.vo.VideoCommentResultVO;
import com.Bibibi.enums.CommentTopTypeEnum;
import com.Bibibi.enums.PageSize;
import com.Bibibi.enums.UserActionTypeEnum;
import com.Bibibi.exception.BusinessException;
import com.Bibibi.service.UserActionService;
import com.Bibibi.service.VideoCommentService;
import com.Bibibi.service.VideoInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/comment")
@Validated
@Slf4j
public class VideoCommentController extends ABaseController {

    @Resource
    private VideoCommentService videoCommentService;

    @Resource
    private UserActionService userActionService;

    @Resource
    private VideoInfoService videoInfoService;

    /**
     * 發表評論
     *
     * @param videoId        視頻id
     * @param content        内容
     * @param replyCommentId 回復的評論id
     * @param imgPath        圖片
     * @throws BusinessException
     */
    @RequestMapping("/postComment")
    public ResponseVO postComment(@NotEmpty String videoId,
                                  @NotEmpty @Size(max = 500) String content,
                                  Integer replyCommentId,
                                  @Size(max = 50) String imgPath) throws BusinessException {
        TokenUserInfoDto tokenUserInfoDto = getTokenUserInfoDto();
        VideoComment comment = new VideoComment();
        comment.setUserId(tokenUserInfoDto.getUserId());
        comment.setAvatar(tokenUserInfoDto.getAvatar());
        comment.setNickName(tokenUserInfoDto.getNickName());
        comment.setVideoId(videoId);
        comment.setContent(content);
        comment.setImgPath(imgPath);
        videoCommentService.postComment(comment, replyCommentId);
//        comment.setReplyAvatar(tokenUserInfoDto.getAvatar());
        return getSuccessResponseVO(comment);
    }

    @RequestMapping("/loadComment")
    public ResponseVO loadComment(@NotEmpty String videoId, Integer pageNo, Integer orderType) {
        VideoInfo videoInfo = videoInfoService.getByVideoId(videoId);
        if (videoInfo.getInteraction() != null && videoInfo.getInteraction().contains(Constants.ZERO.toString())) {
            return getSuccessResponseVO(new ArrayList<>());
        }
        VideoCommentQuery commentQuery = new VideoCommentQuery();
        commentQuery.setVideoId(videoId);
        commentQuery.setLoadChildren(Boolean.TRUE);
        commentQuery.setPageNo(pageNo);
        commentQuery.setPageSize(PageSize.SIZE15.getSize());
        commentQuery.setPCommentId(0);
        String orderBy = orderType == null || orderType == 0 ? "like_count desc,comment_id desc" : "comment_id desc";
        commentQuery.setOrderBy(orderBy);

        PaginationResultVO<VideoComment> commentData = videoCommentService.findByPage(commentQuery);

        //置顶评论
        if (pageNo == null || pageNo == 1) {
            List<VideoComment> topCommentList = topComment(videoId);
            if (!topCommentList.isEmpty()) {
                List<VideoComment> commentList =
                        commentData.getList().stream().filter(item -> !item.getCommentId().equals(topCommentList.get(0).getCommentId())).collect(Collectors.toList());
                commentList.addAll(0, topCommentList);
                commentData.setList(commentList);
            }
        }

        VideoCommentResultVO resultVO = new VideoCommentResultVO();
        resultVO.setCommentData(commentData);

        List<UserAction> userActionList = new ArrayList<>();
        TokenUserInfoDto tokenUserInfoDto = getTokenUserInfoDto();
        if (tokenUserInfoDto != null) {
            UserActionQuery actionQuery = new UserActionQuery();
            actionQuery.setUserId(tokenUserInfoDto.getUserId());
            actionQuery.setVideoId(videoId);
            actionQuery.setActionTypeArray(new Integer[]{UserActionTypeEnum.COMMENT_LIKE.getType(), UserActionTypeEnum.COMMENT_HATE.getType()});
            userActionList = userActionService.findListByParam(actionQuery);
        }
        resultVO.setUserActionList(userActionList);

        return getSuccessResponseVO(resultVO);
    }

    private List<VideoComment> topComment(String videoId) {
        VideoCommentQuery commentQuery = new VideoCommentQuery();
        commentQuery.setVideoId(videoId);
        commentQuery.setTopType(CommentTopTypeEnum.TOP.getType());
        commentQuery.setLoadChildren(true);
        List<VideoComment> videoCommentList = videoCommentService.findListByParam(commentQuery);
        return videoCommentList;
    }

    @RequestMapping("/topComment")
    public ResponseVO topComment(@NotNull Integer commentId) throws BusinessException {
        TokenUserInfoDto tokenUserInfoDto = getTokenUserInfoDto();
        videoCommentService.topComment(commentId, tokenUserInfoDto.getUserId());
        return getSuccessResponseVO(null);
    }

    @RequestMapping("/cancelTopComment")
    public ResponseVO cancelTopComment(@NotNull Integer commentId) throws BusinessException {
        TokenUserInfoDto tokenUserInfoDto = getTokenUserInfoDto();
        videoCommentService.cancelTopComment(commentId, tokenUserInfoDto.getUserId());
        return getSuccessResponseVO(null);
    }

    @RequestMapping("/userDelComment")
    public ResponseVO userDelComment(@NotNull Integer commentId) throws BusinessException {
        TokenUserInfoDto tokenUserInfoDto = getTokenUserInfoDto();
        VideoComment comment = new VideoComment();
        videoCommentService.deleteComment(commentId, tokenUserInfoDto.getUserId());
        return getSuccessResponseVO(comment);
    }
}
