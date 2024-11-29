package com.Bibibi.mappers;

import org.apache.ibatis.annotations.Param;

/**
 * @Description:视频弹幕Mapper
 * @date:2024-11-20
 * @author: liujun
 */
public interface VideoDanmuMappers<T, P> extends BaseMapper {

    /**
     * 根据DanmuId查询
     */
    T selectByDanmuId(@Param("danmuId") Integer danmuId);

    /**
     * 根据DanmuId更新
     */
    Integer updateByDanmuId(@Param("bean") T t, @Param("danmuId") Integer danmuId);

    /**
     * 根据DanmuId删除
     */
    Integer deleteByDanmuId(@Param("danmuId") Integer danmuId);

}