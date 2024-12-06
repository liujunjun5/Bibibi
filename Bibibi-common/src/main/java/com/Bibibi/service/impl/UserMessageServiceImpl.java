package com.Bibibi.service.impl;

import com.Bibibi.entity.dto.UserMessageCountDto;
import com.Bibibi.entity.dto.UserMessageExtendDto;
import com.Bibibi.entity.po.UserMessage;
import com.Bibibi.entity.po.VideoComment;
import com.Bibibi.entity.po.VideoInfo;
import com.Bibibi.entity.po.VideoInfoPost;
import com.Bibibi.entity.query.*;
import com.Bibibi.entity.vo.PaginationResultVO;
import com.Bibibi.enums.MessageReadTypeEnum;
import com.Bibibi.enums.MessageTypeEnum;
import com.Bibibi.enums.PageSize;
import com.Bibibi.exception.BusinessException;
import com.Bibibi.mappers.*;
import com.Bibibi.service.UserMessageService;
import com.Bibibi.utils.JsonUtils;
import com.Bibibi.utils.StringTools;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @Description:用户消息表Service
 * @date:2024-12-04
 * @author: liujun
 */
@Service("UserMessageService")
public class UserMessageServiceImpl implements UserMessageService {

	@Resource
	private UserMessageMappers<UserMessage, UserMessageQuery> userMessageMappers;

	@Resource
	private VideoInfoMappers<VideoInfo, VideoInfoQuery> videoInfoMappers;

	@Resource
	private VideoCommentMappers<VideoComment, VideoCommentQuery> videoCommentMappers;

	@Resource
	private VideoInfoPostMappers<VideoInfoPost, VideoInfoPostQuery> videoInfoPostMappers;

	/**
	 * 根据条件查询列表
	 */
	public List<UserMessage> findListByParam(UserMessageQuery query) {
		return this.userMessageMappers.selectList(query);
	}

	/**
	 * 根据条件查询数量
	 */
	public Integer findCountByParam(UserMessageQuery query) {
		return this.userMessageMappers.selectCount(query);
	}

	/**
	 * 分页查询
	 */
	public PaginationResultVO<UserMessage> findByPage(UserMessageQuery query) {
		Integer count = this.findCountByParam(query);
		Integer pageSize = query.getPageSize()==null?PageSize.SIZE15.getSize():query.getPageSize();
		SimplePage page = new SimplePage(query.getPageNo(), count, pageSize);
		query.setSimplePage(page);
		List<UserMessage> list = this.findListByParam(query);
		PaginationResultVO<UserMessage> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
		return result;
	}

	/**
	 * 新增
	 */
	public Integer add(UserMessage bean) {
		return this.userMessageMappers.insert(bean);
	}

	/**
	 * 批量新增
	 */
	public Integer addBatch(List<UserMessage> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.userMessageMappers.insertBatch(listBean);
	}

	/**
	 * 批量新增或更新
	 */
	public Integer addOrUpdateBatch(List<UserMessage> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.userMessageMappers.insertOrUpdateBatch(listBean);
	}


	/**
	 * 根据MessageId查询
	 */
	public UserMessage getByMessageId(Integer messageId) {
		return this.userMessageMappers.selectByMessageId(messageId);
	}

	/**
	 * 根据MessageId更新
	 */
	public Integer updateByMessageId(UserMessage bean, Integer messageId) {
		return this.userMessageMappers.updateByMessageId(bean, messageId);
	}

	/**
	 * 根据MessageId删除
	 */
	public Integer deleteByMessageId(Integer messageId) {
		return this.userMessageMappers.deleteByMessageId(messageId);
	}


	/**
	 * 多条件更新
	 */
	@Override
	public Integer updateByParam(UserMessage bean, UserMessageQuery param) throws BusinessException {
		StringTools.checkParam(param);
		return this.userMessageMappers.updateByParam(bean, param);
	}

	/**
	 * @param videoId
	 * @param sendUserId
	 * @param messageTypeEnum
	 * @param content
	 * @param replyCommentId
	 */
	@Override
	@Async
	public void saveUserMessage(String videoId, String sendUserId, MessageTypeEnum messageTypeEnum, String content, Integer replyCommentId) {
		VideoInfo videoInfo = videoInfoMappers.selectByVideoId(videoId);
		if (videoInfo==null) {
			return;
		}
		UserMessageExtendDto extendDto = new UserMessageExtendDto();
		extendDto.setMessageContent(content);
		String userId = videoInfo.getUserId();
		//收藏，点赞，已经记录的不再记录
		if (ArrayUtils.contains(new Integer[]{MessageTypeEnum.LIKE.getType(),MessageTypeEnum.COLLECTION.getType()}, messageTypeEnum)) {
			UserMessageQuery userMessageQuery = new UserMessageQuery();
			userMessageQuery.setUserId(userId);
			userMessageQuery.setVideoId(videoId);
			userMessageQuery.setMessageType(messageTypeEnum.getType());
			Integer count = userMessageMappers.selectCount(userMessageQuery);
			if (count>0) {
				return;
			}
		}
		UserMessage userMessage = new UserMessage();
		userMessage.setUserId(userId);
		userMessage.setVideoId(videoId);
		userMessage.setCreateTime(new Date());
		userMessage.setReadType(MessageReadTypeEnum.NO_READ.getType());
		userMessage.setMessageType(messageTypeEnum.getType());
		userMessage.setSendUserId(sendUserId);
		//评论特殊处理
		if (replyCommentId!=null) {
			VideoComment comment = videoCommentMappers.selectByCommentId(replyCommentId);
			if (comment!=null) {
				userId = comment.getUserId();
				extendDto.setMessageContentReply(comment.getContent());
			}
		}
		if (userId.equals(sendUserId)) {
			return;
		}

		//系统消息特殊处理看
		if (MessageTypeEnum.SYS ==  messageTypeEnum) {
			VideoInfoPost videoInfoPost = videoInfoPostMappers.selectByVideoId(videoId);
			extendDto.setAuditStatus(videoInfoPost.getStatus());
		}

		userMessage.setUserId(userId);
		userMessage.setExtendJson(JsonUtils.convertObj2Json(extendDto));
		this.userMessageMappers.insert(userMessage);
	}

	/**
	 * @param userId
	 * @return
	 */
	@Override
	public List<UserMessageCountDto> getMessageTypeNoReadCount(String userId) {

		return this.userMessageMappers.getMessageTypeNoReadCount(userId);
	}
}