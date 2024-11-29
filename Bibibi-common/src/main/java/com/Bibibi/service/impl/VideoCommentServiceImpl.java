package com.Bibibi.service.impl;

import com.Bibibi.entity.constants.Constants;
import com.Bibibi.entity.po.UserInfo;
import com.Bibibi.entity.po.VideoComment;
import com.Bibibi.entity.po.VideoInfo;
import com.Bibibi.entity.query.SimplePage;
import com.Bibibi.entity.query.UserInfoQuery;
import com.Bibibi.entity.query.VideoCommentQuery;
import com.Bibibi.entity.query.VideoInfoQuery;
import com.Bibibi.entity.vo.PaginationResultVO;
import com.Bibibi.enums.CommentTopTypeEnum;
import com.Bibibi.enums.PageSize;
import com.Bibibi.enums.ResponseCodeEnum;
import com.Bibibi.enums.UserActionTypeEnum;
import com.Bibibi.exception.BusinessException;
import com.Bibibi.mappers.UserInfoMappers;
import com.Bibibi.mappers.VideoCommentMappers;
import com.Bibibi.mappers.VideoInfoMappers;
import com.Bibibi.service.VideoCommentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @Description:评论Service
 * @date:2024-11-20
 * @author: liujun
 */
@Service("VideoCommentService")
public class VideoCommentServiceImpl implements VideoCommentService {

    @Resource
    private VideoInfoMappers<VideoInfo, VideoInfoQuery> videoInfoMappers;

    @Resource
    private VideoCommentMappers<VideoComment, VideoCommentQuery> videoCommentMappers;

    @Resource
    private UserInfoMappers<UserInfo, UserInfoQuery> userInfoMappers;

    /**
     * 根据条件查询列表
     */
    public List<VideoComment> findListByParam(VideoCommentQuery query) {
        if (query.getLoadChildren() != null && query.getLoadChildren()) {
            return this.videoCommentMappers.selectListWithChildren(query);
        }
        return this.videoCommentMappers.selectList(query);
    }

    /**
     * 根据条件查询数量
     */
    public Integer findCountByParam(VideoCommentQuery query) {
        return this.videoCommentMappers.selectCount(query);
    }

    /**
     * 分页查询
     */
    public PaginationResultVO<VideoComment> findByPage(VideoCommentQuery query) {
        Integer count = this.findCountByParam(query);
        Integer pageSize = query.getPageSize() == null ? PageSize.SIZE15.getSize() : query.getPageSize();
        SimplePage page = new SimplePage(query.getPageNo(), count, pageSize);
        query.setSimplePage(page);
        List<VideoComment> list = this.findListByParam(query);
        PaginationResultVO<VideoComment> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
        return result;
    }

    /**
     * 新增
     */
    public Integer add(VideoComment bean) {
        return this.videoCommentMappers.insert(bean);
    }

    /**
     * 批量新增
     */
    public Integer addBatch(List<VideoComment> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return this.videoCommentMappers.insertBatch(listBean);
    }

    /**
     * 批量新增或更新
     */
    public Integer addOrUpdateBatch(List<VideoComment> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return this.videoCommentMappers.insertOrUpdateBatch(listBean);
    }


    /**
     * 根据CommentId查询
     */
    public VideoComment getByCommentId(Integer commentId) {
        return this.videoCommentMappers.selectByCommentId(commentId);
    }

    /**
     * 根据CommentId更新
     */
    public Integer updateByCommentId(VideoComment bean, Integer commentId) {
        return this.videoCommentMappers.updateByCommentId(bean, commentId);
    }

    /**
     * 根据CommentId删除
     */
    public Integer deleteByCommentId(Integer commentId) {
        return this.videoCommentMappers.deleteByCommentId(commentId);
    }

    /**
     * @param comment        评论信息
     * @param replyCommentId 回复评论父级id
     */
    @Override
    public void postComment(VideoComment comment, Integer replyCommentId) throws BusinessException {
        VideoInfo videoInfo = videoInfoMappers.selectByVideoId(comment.getVideoId());
        if (videoInfo == null) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        if (videoInfo.getInteraction() != null && videoInfo.getInteraction().contains(Constants.ZERO.toString())) {
            throw new BusinessException("UP主已关闭评论区");
        }
        //pCommentId=0即最高级评论
        if (replyCommentId != null) {
            VideoComment replyComment = getByCommentId(replyCommentId);
            if (replyComment == null || !replyComment.getVideoId().equals(comment.getVideoId())) {
                throw new BusinessException(ResponseCodeEnum.CODE_600);
            }
            if (replyComment.getPCommentId() == 0) {
                comment.setPCommentId(replyCommentId);
            } else {
                comment.setPCommentId(replyComment.getPCommentId());
                comment.setReplyUserId(replyComment.getUserId());
            }
            UserInfo userInfo = userInfoMappers.selectByUserId(replyComment.getUserId());
            comment.setReplyNickName(userInfo.getNickName());
            comment.setReplyAvatar(userInfo.getAvatar());
        } else {
            //是獨立的回復
            comment.setPCommentId(0);
        }

        comment.setPostTime(new Date());
        comment.setVideoUserId(videoInfo.getUserId());
        this.videoCommentMappers.insert(comment);
        if (comment.getPCommentId() == 0) {
            this.videoInfoMappers.updateCountInfo(comment.getVideoId(), UserActionTypeEnum.VIDEO_COMMENT.getField(), 1);
        }
    }

    /**
     * @param commentId
     * @param userId
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void topComment(Integer commentId, String userId) throws BusinessException {
        this.cancelTopComment(commentId, userId);
        VideoComment videoComment = new VideoComment();
        videoComment.setTopType(CommentTopTypeEnum.TOP.getType());
        videoCommentMappers.updateByCommentId(videoComment, commentId);
    }

    /**
     * @param commentId
     * @param userId
     */
    @Override
    public void cancelTopComment(Integer commentId, String userId) throws BusinessException {
        VideoComment videoComment = videoCommentMappers.selectByCommentId(commentId);
        if (videoComment == null) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        VideoInfo videoInfo = videoInfoMappers.selectByVideoId(videoComment.getVideoId());
        if (videoInfo == null) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        if (!videoInfo.getUserId().equals(userId)) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
//        VideoInfo videoInfo1 = new VideoInfo();
        VideoComment newVideoComment = new VideoComment();
        newVideoComment.setTopType(CommentTopTypeEnum.NO_TOP.getType());

        VideoCommentQuery commentQuery = new VideoCommentQuery();
        commentQuery.setVideoId(videoComment.getVideoId());
        commentQuery.setTopType(CommentTopTypeEnum.TOP.getType());

        videoCommentMappers.updateByParam(newVideoComment, commentQuery);
    }

    /**
     * 删除评论，检查用户是否是评论主人或者是视频发布者
     * 除了删除评论，还需要检查是否是最高级评论，如果是 还要改变视频信息数据 且删除所有下级评论
     *
     * @param commentId
     * @param userId
     */
    @Override
    public void deleteComment(Integer commentId, String userId) throws BusinessException {
        VideoComment comment = videoCommentMappers.selectByCommentId(commentId);
        if (null == comment) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }

        VideoInfo videoInfo = videoInfoMappers.selectByVideoId(comment.getVideoId());
        if (null == videoInfo) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }

        if (userId != null && !videoInfo.getUserId().equals(userId) && !comment.getUserId().equals(userId)) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        videoCommentMappers.deleteByCommentId(commentId);
        if (comment.getPCommentId() == 0) {
            videoInfoMappers.updateCountInfo(videoInfo.getVideoId(), UserActionTypeEnum.VIDEO_COMMENT.getField(), -1);
            //删除二级评论
            VideoCommentQuery videoCommentQuery = new VideoCommentQuery();
            videoCommentQuery.setPCommentId(commentId);
            videoCommentMappers.deleteByParam(videoCommentQuery);
        }
    }
}