package com.Bibibi.service.impl;

import com.Bibibi.component.RedisComponent;
import com.Bibibi.entity.constants.Constants;
import com.Bibibi.entity.dto.CountInfoDto;
import com.Bibibi.entity.dto.SysSettingDto;
import com.Bibibi.entity.dto.TokenUserInfoDto;
import com.Bibibi.entity.dto.UserCountInfoDto;
import com.Bibibi.entity.po.UserFocus;
import com.Bibibi.entity.po.UserInfo;
import com.Bibibi.entity.po.VideoInfo;
import com.Bibibi.entity.query.SimplePage;
import com.Bibibi.entity.query.UserFocusQuery;
import com.Bibibi.entity.query.UserInfoQuery;
import com.Bibibi.entity.query.VideoInfoQuery;
import com.Bibibi.entity.vo.PaginationResultVO;
import com.Bibibi.enums.PageSize;
import com.Bibibi.enums.ResponseCodeEnum;
import com.Bibibi.enums.UserSexEnum;
import com.Bibibi.enums.UserStatusEnum;
import com.Bibibi.exception.BusinessException;
import com.Bibibi.mappers.UserFocusMappers;
import com.Bibibi.mappers.UserInfoMappers;
import com.Bibibi.mappers.VideoInfoMappers;
import com.Bibibi.service.UserInfoService;
import com.Bibibi.utils.CopyTools;
import com.Bibibi.utils.StringTools;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * @Description:用户信息Service
 * @date:2024-11-03
 * @author: liujun
 */
@Service("InfoService")
public class UserInfoServiceImpl implements UserInfoService {

    @Resource
    private UserInfoMappers<UserInfo, UserInfoQuery> userInfoMappers;

    @Resource
    private RedisComponent RedisComponent;

    @Resource
    private RedisComponent redisComponent;

    @Resource
    private UserFocusMappers<UserFocus, UserFocusQuery> userFocusMappers;

    @Resource
    private VideoInfoMappers<VideoInfo, VideoInfoQuery> videoInfoMappers;

    /**
     * 根据条件查询列表
     */
    public List<UserInfo> findListByParam(UserInfoQuery query) {
        return this.userInfoMappers.selectList(query);
    }

    /**
     * 根据条件查询数量
     */
    public Integer findCountByParam(UserInfoQuery query) {
        return this.userInfoMappers.selectCount(query);
    }

    /**
     * 分页查询
     */
    public PaginationResultVO<UserInfo> findByPage(UserInfoQuery query) {
        Integer count = this.findCountByParam(query);
        Integer pageSize = query.getPageSize() == null ? PageSize.SIZE15.getSize() : query.getPageSize();
        SimplePage page = new SimplePage(query.getPageNo(), count, pageSize);
        query.setSimplePage(page);
        List<UserInfo> list = this.findListByParam(query);
        PaginationResultVO<UserInfo> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
        return result;
    }

    /**
     * 新增
     */
    public Integer add(UserInfo bean) {
        return this.userInfoMappers.insert(bean);
    }

    /**
     * 批量新增
     */
    public Integer addBatch(List<UserInfo> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return this.userInfoMappers.insertBatch(listBean);
    }

    /**
     * 批量新增或更新
     */
    public Integer addOrUpdateBatch(List<UserInfo> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return this.userInfoMappers.insertOrUpdateBatch(listBean);
    }


    /**
     * 根据UserId查询
     */
    public UserInfo getByUserId(String userId) {
        return this.userInfoMappers.selectByUserId(userId);
    }

    /**
     * 根据UserId更新
     */
    public Integer updateByUserId(UserInfo bean, String userId) {
        return this.userInfoMappers.updateByUserId(bean, userId);
    }

    /**
     * 根据UserId删除
     */
    public Integer deleteByUserId(String userId) {
        return this.userInfoMappers.deleteByUserId(userId);
    }


    /**
     * 根据Email查询
     */
    public UserInfo getByEmail(String email) {
        return this.userInfoMappers.selectByEmail(email);
    }

    /**
     * 根据Email更新
     */
    public Integer updateByEmail(UserInfo bean, String email) {
        return this.userInfoMappers.updateByEmail(bean, email);
    }

    /**
     * 根据Email删除
     */
    public Integer deleteByEmail(String email) {
        return this.userInfoMappers.deleteByEmail(email);
    }


    /**
     * 根据NickName查询
     */
    public UserInfo getByNickName(String nickName) {
        return this.userInfoMappers.selectByNickName(nickName);
    }

    /**
     * 根据NickName更新
     */
    public Integer updateByNickName(UserInfo bean, String nickName) {
        return this.userInfoMappers.updateByNickName(bean, nickName);
    }

    /**
     * 根据NickName删除
     */
    public Integer deleteByNickName(String nickName) {
        return this.userInfoMappers.deleteByNickName(nickName);
    }

    /**
     * 注册接口
     *
     * @param email
     * @param nickName
     * @param registerPassword
     */
    @Override
    public void register(String email, String nickName, String registerPassword) throws BusinessException {
        UserInfo userInfo = this.userInfoMappers.selectByEmail(email);
        if (userInfo != null) {
            throw new BusinessException("邮箱已存在");
        }
        UserInfo nickNameUser = this.userInfoMappers.selectByNickName(nickName);
        if (nickNameUser != null) {
            throw new BusinessException("昵称已存在");
        }
        userInfo = new UserInfo();
        String userId = StringTools.getRandomNumber(Constants.LENGTH_10);
        userInfo.setUserId(userId);
        userInfo.setNickName(nickName);
        userInfo.setPassword(StringTools.encodeByMD5(registerPassword));
        userInfo.setEmail(email);
        userInfo.setJoinTime(new Date());
        userInfo.setStatus(UserStatusEnum.ENABLE.getStatus());
        userInfo.setSex(UserSexEnum.SECRECY.getType());
        userInfo.setTheme(Constants.ONE);
        //初始化 用户的硬币
        SysSettingDto sysSettingDto = redisComponent.getSysSettingDto();
        userInfo.setCurrentCoinCount(sysSettingDto.getRegisterCoinCount());
        userInfo.setTotalCoinCount(sysSettingDto.getRegisterCoinCount());
        this.userInfoMappers.insert(userInfo);
    }

    /**
     * 登录
     *
     * @param email
     * @param password
     * @param ip
     * @return
     */
    public TokenUserInfoDto login(HttpServletRequest request, String email, String password, String ip) throws BusinessException {
        UserInfo userInfo = this.userInfoMappers.selectByEmail(email);
        if (userInfo == null || !userInfo.getPassword().equals(password)) {
            throw new BusinessException("账号或密码错误");
        }
        if (userInfo.getStatus().equals(UserStatusEnum.DISABLE.getStatus())) {
            throw new BusinessException("账号已禁用");
        }
        UserInfo updateUser = new UserInfo();
        updateUser.setLastLoginIp(ip);
        updateUser.setLastLoginTime(new Date());
        this.userInfoMappers.updateByUserId(updateUser, userInfo.getUserId());

        TokenUserInfoDto tokenUserInfoDto = CopyTools.copy(userInfo, TokenUserInfoDto.class);
        redisComponent.saveTokenInfo(tokenUserInfoDto);

        return tokenUserInfoDto;
    }

    /**
     * @param currentUserId
     * @param userId
     * @return
     */
    @Override
    public UserInfo getUserDetailInfo(String currentUserId, String userId) throws BusinessException {
        UserInfo userInfo = getByUserId(userId);
        if (userInfo==null) {
            throw new BusinessException(ResponseCodeEnum.CODE_404);
        }
        CountInfoDto countInfoDto = videoInfoMappers.selectSumCountInfo(userId);

        CopyTools.copyProperties(countInfoDto, userInfo);

        Integer focusCount = userFocusMappers.selectFocusCount(userId);
        Integer fansCount = userFocusMappers.selectFansCount(userId);
        userInfo.setFocusCount(focusCount);
        userInfo.setFansCount(fansCount);
        if (currentUserId == null) {
            userInfo.setHaveFocus(false);
        } else {
            UserFocus userFocus = userFocusMappers.selectByUserIdAndFocusUserId(currentUserId, userId);
            userInfo.setHaveFocus(userFocus == null ? false : true);
        }
        return userInfo;
    }

    /**
     * @param userInfo
     * @param tokenUserInfoDto
     */
    @Override
    public void updateUserInfo(UserInfo userInfo, TokenUserInfoDto tokenUserInfoDto) throws BusinessException {
        UserInfo dbInfo = this.userInfoMappers.selectByUserId(userInfo.getUserId());
        if (!dbInfo.getNickName().equals(userInfo.getNickName())&&dbInfo.getCurrentCoinCount()<Constants.UPDATE_NICK_NAME_COIN) {
            throw new BusinessException("硬幣不足，無法修改昵稱");
        }
        if (!dbInfo.getNickName().equals(userInfo.getNickName())){
            Integer count = this.userInfoMappers.updateCoinCountInfo(userInfo.getUserId(), -Constants.UPDATE_NICK_NAME_COIN);
            if (count==0) {
                throw new BusinessException("硬幣不足，無法修改昵稱");
            }
        }
        this.userInfoMappers.updateByUserId(userInfo, userInfo.getUserId());

        Boolean updateTokenInfo = false;
        if (!userInfo.getAvatar().equals(tokenUserInfoDto.getAvatar())) {
            tokenUserInfoDto.setAvatar(userInfo.getAvatar());
            updateTokenInfo = true;
        }
        if (!tokenUserInfoDto.getNickName().equals(userInfo.getNickName())) {
            tokenUserInfoDto.setNickName(userInfo.getNickName());
            updateTokenInfo = true;
        }
        if (updateTokenInfo) {
            redisComponent.updateTokenInfo(tokenUserInfoDto);
        }
    }

    /**
     * @param userId
     * @return
     */
    @Override
    public UserCountInfoDto getUserCountInfo(String userId) {
        UserInfo userInfo = getByUserId(userId);
        Integer fansCount = userFocusMappers.selectFansCount(userId);
        Integer focusCount = userFocusMappers.selectFocusCount(userId);
        UserCountInfoDto userCountInfoDto = new UserCountInfoDto();
        userCountInfoDto.setFansCount(fansCount);
        userCountInfoDto.setFocusCount(focusCount);
        userCountInfoDto.setCurrentCoinCount(userInfo.getCurrentCoinCount());
        return userCountInfoDto;
    }
}