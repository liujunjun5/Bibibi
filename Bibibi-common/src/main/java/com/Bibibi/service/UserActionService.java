package com.Bibibi.service;

import com.Bibibi.entity.po.UserAction;
import com.Bibibi.entity.query.UserActionQuery;
import com.Bibibi.entity.vo.PaginationResultVO;
import com.Bibibi.exception.BusinessException;

import java.util.List;

/**
 * @Description:用户行为 点赞、评论Service
 * @date:2024-11-20
 * @author: liujun
 */
public interface UserActionService {

    /**
     * 根据条件查询列表
     */
    List<UserAction> findListByParam(UserActionQuery query);

    /**
     * 根据条件查询数量
     */
    Integer findCountByParam(UserActionQuery query);

    /**
     * 分页查询
     */
    PaginationResultVO<UserAction> findByPage(UserActionQuery query);

    /**
     * 新增
     */
    Integer add(UserAction bean);

    /**
     * 批量新增
     */
    Integer addBatch(List<UserAction> listBean);

    /**
     * 批量新增或更新
     */
    Integer addOrUpdateBatch(List<UserAction> listBean);


    /**
     * 根据ActionId查询
     */
    UserAction getByActionId(Integer actionId);

    /**
     * 根据ActionId更新
     */
    Integer updateByActionId(UserAction bean, Integer actionId);

    /**
     * 根据ActionId删除
     */
    Integer deleteByActionId(Integer actionId);


    /**
     * 根据VideoIdAndCommentIdAndActionTypeAndUserId查询
     */
    UserAction getByVideoIdAndCommentIdAndActionTypeAndUserId(String videoId, Integer commentId, Integer actionType, String userId);

    /**
     * 根据VideoIdAndCommentIdAndActionTypeAndUserId更新
     */
    Integer updateByVideoIdAndCommentIdAndActionTypeAndUserId(UserAction bean, String videoId, Integer commentId, Integer actionType, String userId);

    /**
     * 根据VideoIdAndCommentIdAndActionTypeAndUserId删除
     */
    Integer deleteByVideoIdAndCommentIdAndActionTypeAndUserId(String videoId, Integer commentId, Integer actionType, String userId);

    void saveAction(UserAction userAction) throws BusinessException;
}