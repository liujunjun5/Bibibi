package com.Bibibi.service;

import com.Bibibi.entity.dto.TokenUserInfoDto;
import com.Bibibi.entity.po.UserInfo;
import com.Bibibi.entity.query.UserInfoQuery;
import com.Bibibi.entity.vo.PaginationResultVO;
import com.Bibibi.exception.BusinessException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Description:用户信息Service
 * @date:2024-11-03
 * @author: liujun
 */
public interface UserInfoService {

    /**
     * 根据条件查询列表
     */
    List<UserInfo> findListByParam(UserInfoQuery query);

    /**
     * 根据条件查询数量
     */
    Integer findCountByParam(UserInfoQuery query);

    /**
     * 分页查询
     */
    PaginationResultVO<UserInfo> findByPage(UserInfoQuery query);

    /**
     * 新增
     */
    Integer add(UserInfo bean);

    /**
     * 批量新增
     */
    Integer addBatch(List<UserInfo> listBean);

    /**
     * 批量新增或更新
     */
    Integer addOrUpdateBatch(List<UserInfo> listBean);


    /**
     * 根据UserId查询
     */
    UserInfo getByUserId(String userId);

    /**
     * 根据UserId更新
     */
    Integer updateByUserId(UserInfo bean, String userId);

    /**
     * 根据UserId删除
     */
    Integer deleteByUserId(String userId);


    /**
     * 根据Email查询
     */
    UserInfo getByEmail(String email);

    /**
     * 根据Email更新
     */
    Integer updateByEmail(UserInfo bean, String email);

    /**
     * 根据Email删除
     */
    Integer deleteByEmail(String email);


    /**
     * 根据NickName查询
     */
    UserInfo getByNickName(String nickName);

    /**
     * 根据NickName更新
     */
    Integer updateByNickName(UserInfo bean, String nickName);

    /**
     * 根据NickName删除
     */
    Integer deleteByNickName(String nickName);

    /**
     * 用户注册
     */
    void register(String email, String nickName, String registerPassword) throws BusinessException;

    /**
     * 用户登录
     *
     * @return
     */
    TokenUserInfoDto login(HttpServletRequest request, String email, String password, String ip) throws BusinessException;
}