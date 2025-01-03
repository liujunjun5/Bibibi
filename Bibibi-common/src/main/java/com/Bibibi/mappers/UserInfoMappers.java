package com.Bibibi.mappers;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description:用户信息Mapper
 * @date:2024-11-03
 * @author: liujun
 */
@Mapper
public interface UserInfoMappers<T, P> extends BaseMapper {

    /**
     * 根据UserId查询
     */
    T selectByUserId(@Param("userId") String userId);

    /**
     * 根据UserId更新
     */
    Integer updateByUserId(@Param("bean") T t, @Param("userId") String userId);

    /**
     * 根据UserId删除
     */
    Integer deleteByUserId(@Param("userId") String userId);

    /**
     * 根据Email查询
     */
    T selectByEmail(@Param("email") String email);

    /**
     * 根据Email更新
     */
    Integer updateByEmail(@Param("bean") T t, @Param("email") String email);

    /**
     * 根据Email删除
     */
    Integer deleteByEmail(@Param("email") String email);

    /**
     * 根据NickName查询
     */
    T selectByNickName(@Param("nickName") String nickName);

    /**
     * 根据NickName更新
     */
    Integer updateByNickName(@Param("bean") T t, @Param("nickName") String nickName);

    /**
     * 根据NickName删除
     */
    Integer deleteByNickName(@Param("nickName") String nickName);

    Integer updateCoinCountInfo(String userId, Integer changeCount);
}