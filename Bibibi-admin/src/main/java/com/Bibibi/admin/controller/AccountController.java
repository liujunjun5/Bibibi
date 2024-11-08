package com.Bibibi.admin.controller;

import com.Bibibi.component.RedisComponent;
import com.Bibibi.entity.config.AppConfig;
import com.Bibibi.entity.constants.Constants;
import com.Bibibi.entity.vo.ResponseVO;
import com.Bibibi.exception.BusinessException;
import com.Bibibi.redis.RedisUtils;
import com.Bibibi.utils.StringTools;
import com.wf.captcha.ArithmeticCaptcha;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController("accountController")
@RequestMapping("/account")
@Validated
public class AccountController extends ABaseController {

    @Resource
    private AppConfig appConfig;

    @Resource
    private RedisUtils redisUtils;

    @Resource
    private RedisComponent redisComponent;

    /**
     * 验证码
     */
    @RequestMapping(value = "/checkCode")
    public ResponseVO checkCode() {
        ArithmeticCaptcha captcha = new ArithmeticCaptcha(100, 42);
        String code = captcha.text();
        String checkCodeKey = UUID.randomUUID().toString();
        redisUtils.setex(Constants.REDIS_KEY_CHECK_CODE + checkCodeKey, code, Constants.REDIS_KEY_EXPIRES_ONE_MIN * 10);
        String checkCodeBase64 = captcha.toBase64();
        Map<String, String> result = new HashMap<>();
        result.put("checkCode", checkCodeBase64);
        result.put("checkCodeKey", checkCodeKey);
        return getSuccessResponseVO(result);
    }

    @RequestMapping(value = "/login")
    public ResponseVO login(HttpServletRequest request,
                            HttpServletResponse response,
                            @NotEmpty String account,
                            @NotEmpty String password, @NotEmpty String checkCode,
                            @NotEmpty String checkCodeKey) throws BusinessException {
        try {
            if (!checkCode.equalsIgnoreCase((String) redisUtils.get(Constants.REDIS_KEY_CHECK_CODE + checkCodeKey))) {
                throw new BusinessException("图片验证码不正确");
            }
            if (!account.equals(appConfig.getAdminAccount()) || !password.equals(StringTools.encodeByMD5(appConfig.getAdminPassword()))) {
                throw new BusinessException("账号或者密码错误");
            }
            String token = redisComponent.saveTokenInfo4Admin(account);
            saveToken2Cookie(response, token);
            return getSuccessResponseVO(account);
        } finally {
            redisUtils.delete(Constants.REDIS_KEY_CHECK_CODE + checkCodeKey);
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                String token = null;
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals(Constants.TOKEN_ADMIN_KEY)) {
                        token = cookie.getValue();
                    }
                }
                if (!StringTools.isEmpty(token)) {
                    redisComponent.cleanToken4Admin(token);
                }
            }
        }
    }

    @RequestMapping(value = "/logout")
    public ResponseVO logout(HttpServletRequest request) {
        String token = request.getHeader(Constants.TOKEN_ADMIN_KEY);
        redisComponent.cleanToken4Admin(token);
        return getSuccessResponseVO(null);
    }
}