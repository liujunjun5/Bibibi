package com.Bibibi.mappers;

import org.apache.ibatis.annotations.Param;

/**
 * @Description:视频文件信息Mapper
 * @date:2024-11-11
 * @author: liujun
 */
public interface VideoInfoFileMappers<T, P> extends BaseMapper {

	/**
	 * 根据FileId查询
	 */
	T selectByFileId(@Param("fileId") String fileId);

	/**
	 * 根据FileId更新
	 */
	Integer updateByFileId(@Param("bean") T t, @Param("fileId") String fileId);

	/**
	 * 根据FileId删除
	 */
	Integer deleteByFileId(@Param("fileId") String fileId);

}