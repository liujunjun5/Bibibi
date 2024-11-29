package com.Bibibi.service.impl;

import com.Bibibi.entity.constants.Constants;
import com.Bibibi.entity.po.UserAction;
import com.Bibibi.entity.po.VideoInfo;
import com.Bibibi.entity.query.SimplePage;
import com.Bibibi.entity.query.UserActionQuery;
import com.Bibibi.entity.query.UserInfoQuery;
import com.Bibibi.entity.vo.PaginationResultVO;
import com.Bibibi.enums.PageSize;
import com.Bibibi.enums.ResponseCodeEnum;
import com.Bibibi.enums.UserActionTypeEnum;
import com.Bibibi.exception.BusinessException;
import com.Bibibi.mappers.UserActionMappers;
import com.Bibibi.mappers.UserInfoMappers;
import com.Bibibi.mappers.VideoInfoMappers;
import com.Bibibi.service.UserActionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @Description: 用户行为 点赞、评论Service
 * @date: 2024-11-20
 * @author: liujun
 */
@Service("UserActionService")
public class UserActionServiceImpl implements UserActionService {

    @Resource
    private UserInfoMappers userInfoMappers;

    @Resource
    private UserActionMappers<UserAction, UserActionQuery> userActionMappers;

    @Resource
    private VideoInfoMappers<VideoInfo, UserInfoQuery> videoInfoMappers;

    /**
     * 根据条件查询列表
     */
    public List<UserAction> findListByParam(UserActionQuery query) {
        return this.userActionMappers.selectList(query);
    }

    /**
     * 根据条件查询数量
     */
    public Integer findCountByParam(UserActionQuery query) {
        return this.userActionMappers.selectCount(query);
    }

    /**
     * 分页查询
     */
    public PaginationResultVO<UserAction> findByPage(UserActionQuery query) {
        Integer count = this.findCountByParam(query);
        Integer pageSize = query.getPageSize() == null ? PageSize.SIZE15.getSize() : query.getPageSize();
        SimplePage page = new SimplePage(query.getPageNo(), count, pageSize);
        query.setSimplePage(page);
        List<UserAction> list = this.findListByParam(query);
        PaginationResultVO<UserAction> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
        return result;
    }

    /**
     * 新增
     */
    public Integer add(UserAction bean) {
        return this.userActionMappers.insert(bean);
    }

    /**
     * 批量新增
     */
    public Integer addBatch(List<UserAction> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return this.userActionMappers.insertBatch(listBean);
    }

    /**
     * 批量新增或更新
     */
    public Integer addOrUpdateBatch(List<UserAction> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return this.userActionMappers.insertOrUpdateBatch(listBean);
    }


    /**
     * 根据ActionId查询
     */
    public UserAction getByActionId(Integer actionId) {
        return this.userActionMappers.selectByActionId(actionId);
    }

    /**
     * 根据ActionId更新
     */
    public Integer updateByActionId(UserAction bean, Integer actionId) {
        return this.userActionMappers.updateByActionId(bean, actionId);
    }

    /**
     * 根据ActionId删除
     */
    public Integer deleteByActionId(Integer actionId) {
        return this.userActionMappers.deleteByActionId(actionId);
    }


    /**
     * 根据VideoIdAndCommentIdAndActionTypeAndUserId查询
     */
    public UserAction getByVideoIdAndCommentIdAndActionTypeAndUserId(String videoId, Integer commentId, Integer actionType, String userId) {
        return this.userActionMappers.selectByVideoIdAndCommentIdAndActionTypeAndUserId(videoId, commentId, actionType, userId);
    }

    /**
     * 根据VideoIdAndCommentIdAndActionTypeAndUserId更新
     */
    public Integer updateByVideoIdAndCommentIdAndActionTypeAndUserId(UserAction bean, String videoId, Integer commentId, Integer actionType, String userId) {
        return this.userActionMappers.updateByVideoIdAndCommentIdAndActionTypeAndUserId(bean, videoId, commentId, actionType, userId);
    }

    /**
     * 根据VideoIdAndCommentIdAndActionTypeAndUserId删除
     */
    public Integer deleteByVideoIdAndCommentIdAndActionTypeAndUserId(String videoId, Integer commentId, Integer actionType, String userId) {
        return this.userActionMappers.deleteByVideoIdAndCommentIdAndActionTypeAndUserId(videoId, commentId, actionType, userId);
    }

    /**
     * @param bean
     */
    @Override
    @Transactional
    public void saveAction(UserAction bean) throws BusinessException {
        VideoInfo videoInfo = videoInfoMappers.selectByVideoId(bean.getVideoId());
        if (videoInfo == null) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        bean.setVideoUserId(videoInfo.getUserId());

        UserActionTypeEnum actionTypeEnum = UserActionTypeEnum.getByType(bean.getActionType());
        if (actionTypeEnum == null) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }

        String userId = bean.getUserId();
        Integer actionType = bean.getActionType();
        Integer commentId = bean.getCommentId();
        String videoId = bean.getVideoId();

        UserAction dbAction = userActionMappers.selectByVideoIdAndCommentIdAndActionTypeAndUserId(videoId, commentId, actionType, userId);

        bean.setActionTime(new Date());

        switch (actionTypeEnum) {
            case VIDEO_LIKE:
            case VIDEO_COLLECT:
                if (dbAction != null) {
                    userActionMappers.deleteByActionId(dbAction.getActionId());
                } else {
                    userActionMappers.insert(bean);
                }
                Integer changeCount = dbAction == null ? Constants.ONE : -Constants.ONE;
                videoInfoMappers.updateCountInfo(bean.getVideoId(), actionTypeEnum.getField(), changeCount);
                if (actionTypeEnum == UserActionTypeEnum.VIDEO_COLLECT) {
                    //TODO 更新ES的收藏数量
                }
                break;
            case VIDEO_COIN:
                if (videoInfo.getUserId().equals(bean.getUserId())) {
                    throw new BusinessException("不能给自己投币");
                }
                if (dbAction != null) {
                    throw new BusinessException("重复投币");
                }
                //减少自己的币
                Integer updateCount = userInfoMappers.updateCoinCountInfo(bean.getUserId(), -bean.getActionCount());
                if (updateCount == 0) {
                    throw new BusinessException("币不够");
                }
                //给UP主加币
                updateCount = userInfoMappers.updateCoinCountInfo(videoInfo.getUserId(), bean.getActionCount());
                if (updateCount == 0) {
                    throw new BusinessException("投币失败");
                }
                userActionMappers.insert(bean);

                videoInfoMappers.updateCountInfo(bean.getVideoId(), actionTypeEnum.getField(), bean.getActionCount());
                break;
        }
    }
}