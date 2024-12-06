package com.Bibibi.mappers;

import com.Bibibi.entity.dto.UserMessageCountDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description:用户消息表Mapper
 * @date:2024-12-04
 * @author: liujun
 */
public interface UserMessageMappers<T, P> extends BaseMapper {

	/**
	 * 根据MessageId查询
	 */
	T selectByMessageId(@Param("messageId") Integer messageId);

	/**
	 * 根据MessageId更新
	 */
	Integer updateByMessageId(@Param("bean") T t, @Param("messageId") Integer messageId);

	/**
	 * 根据MessageId删除
	 */
	Integer deleteByMessageId(@Param("messageId") Integer messageId);

	List<UserMessageCountDto> getMessageTypeNoReadCount(@Param("userId")String userId);
}