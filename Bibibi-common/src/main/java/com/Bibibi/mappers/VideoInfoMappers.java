package com.Bibibi.mappers;

import com.Bibibi.entity.dto.CountInfoDto;
import org.apache.ibatis.annotations.Param;

/**
 * @Description:视频信息Mapper
 * @date:2024-11-11
 * @author: liujun
 */
public interface VideoInfoMappers<T, P> extends BaseMapper {

	/**
	 * 根据VideoId查询
	 */
	T selectByVideoId(@Param("videoId") String videoId);

	/**
	 * 根据VideoId更新
	 */
	Integer updateByVideoId(@Param("bean") T t, @Param("videoId") String videoId);

	/**
	 * 根据VideoId删除
	 */
	Integer deleteByVideoId(@Param("videoId") String videoId);

    /**
     * 更新各种数量信息
     *
     * @param videoId     视频id
     * @param field       字段
     * @param changeCount 数量
     */
    void updateCountInfo(@Param("videoId") String videoId, @Param("field") String field, @Param("changeCount") Integer changeCount);

    CountInfoDto selectSumCountInfo(@Param("userId") String userId);
}