package com.Bibibi.service;

import com.Bibibi.entity.dto.UserMessageCountDto;
import com.Bibibi.entity.po.UserMessage;
import com.Bibibi.entity.query.UserMessageQuery;
import com.Bibibi.entity.vo.PaginationResultVO;
import com.Bibibi.enums.MessageTypeEnum;
import com.Bibibi.exception.BusinessException;

import java.util.List;
/**
 * @Description:用户消息表Service
 * @date:2024-12-04
 * @author: liujun
 */
public interface UserMessageService { 

	/**
	 * 根据条件查询列表
	 */
	List<UserMessage> findListByParam(UserMessageQuery query);

	/**
	 * 根据条件查询数量
	 */
	Integer findCountByParam(UserMessageQuery query);

	/**
	 * 分页查询
	 */
	PaginationResultVO<UserMessage> findByPage(UserMessageQuery query);

	/**
	 * 新增
	 */
	Integer add(UserMessage bean);

	/**
	 * 批量新增
	 */
	Integer addBatch(List<UserMessage> listBean);

	/**
	 * 批量新增或更新
	 */
	Integer addOrUpdateBatch(List<UserMessage> listBean);


	/**
	 * 根据MessageId查询
	 */
	UserMessage getByMessageId(Integer messageId);

	/**
	 * 根据MessageId更新
	 */
	Integer updateByMessageId(UserMessage bean, Integer messageId);

	/**
	 * 根据MessageId删除
	 */
	Integer deleteByMessageId(Integer messageId);

	Integer updateByParam(UserMessage bean, UserMessageQuery param) throws BusinessException;

	void saveUserMessage(String videoId, String userId, MessageTypeEnum messageTypeEnum, String content, Integer replyCommentId);

	List<UserMessageCountDto> getMessageTypeNoReadCount(String userId);
}