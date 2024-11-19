package com.Bibibi.web.controller;

import com.Bibibi.component.RedisComponent;
import com.Bibibi.entity.constants.Constants;
import com.Bibibi.entity.dto.TokenUserInfoDto;
import com.Bibibi.entity.vo.ResponseVO;
import com.Bibibi.exception.BusinessException;
import com.Bibibi.service.UserInfoService;
import com.Bibibi.utils.StringTools;
import com.wf.captcha.ArithmeticCaptcha;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 用户信息Controller
 * @date: 2024-11-03
 * @author: liujun
 */
@Validated
@Slf4j
@RestController("accountController")
@RequestMapping("/account")
public class AccountController extends ABaseController {

    @Resource
    private UserInfoService userInfoService;

    @Resource
    private RedisComponent redisComponent;

    @RequestMapping("/checkCode")
    public ResponseVO checkCode() {
        ArithmeticCaptcha captcha = new ArithmeticCaptcha(100, 42);
        String code = captcha.text();
        String checkCodeKey = redisComponent.saveCheckCode(code);
        String checkCodeBase64 = captcha.toBase64();
        Map<String, String> result = new HashMap<>();
        result.put("checkCode", checkCodeBase64);
        result.put("checkCodeKey", checkCodeKey);
        log.info("发送验证码成功,验证码为{}", code);
        return getSuccessResponseVO(result);
    }

    //    @GlobalInterceptor
    @RequestMapping("/register")
    public ResponseVO register(@NotEmpty @Email @Size(max = 150) String email, @NotEmpty @Size(max = 20) String nickName, @NotEmpty @Pattern(regexp =
            Constants.REGEX_PASSWORD) String registerPassword, @NotEmpty String checkCodeKey, @NotEmpty String checkCode) throws BusinessException {
        try {
            if (!checkCode.equalsIgnoreCase(redisComponent.getCode(checkCodeKey))) {
                throw new BusinessException("图片验证码错误");
            }
            userInfoService.register(email, nickName, registerPassword);
            log.info("注册成功");
            return getSuccessResponseVO(null);
        } finally {
            redisComponent.cleanCheckCode(checkCodeKey);
        }
    }

    @RequestMapping("/login")
    public ResponseVO login(HttpServletRequest request,
                            HttpServletResponse response,
                            @NotEmpty @Email String email,
                            @NotEmpty String password,
                            @NotEmpty String checkCodeKey,
                            @NotEmpty String checkCode) throws BusinessException {
        try {
            if (!checkCode.equalsIgnoreCase(redisComponent.getCode(checkCodeKey))) {
                throw new BusinessException("图片验证码错误");
            }
            String ip = getIpAddr();
            TokenUserInfoDto tokenUserInfoDto = userInfoService.login(request, email, password, ip);
            saveToken2Cookie(response, tokenUserInfoDto.getToken());
            //TODO 设置 粉丝数，关注数，硬币数
            log.info("登陆成功");
            return getSuccessResponseVO(tokenUserInfoDto);
        } finally {
            redisComponent.cleanCheckCode(checkCodeKey);

            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                String token = null;
                for (Cookie cookie : cookies) {
                    if (Constants.TOKEN_KEY.equals(cookie.getName())) {
                        token = cookie.getValue();
                    }
                }
                if (!StringTools.isEmpty(token)) {
                    redisComponent.cleanCheckCode(token);
                }
            }
        }
    }

    @RequestMapping("/autoLogin")
    public ResponseVO autoLogin(HttpServletResponse response) {
        TokenUserInfoDto tokenUserInfoDto = getTokenUserInfoDto();
        if (tokenUserInfoDto == null) {
            return getSuccessResponseVO(null);
        }
        if (tokenUserInfoDto.getExpireAt() - System.currentTimeMillis() < Constants.REDIS_KEY_EXPIRES_ONE_DAY * 7) {
            redisComponent.saveTokenInfo(tokenUserInfoDto);
            saveToken2Cookie(response, tokenUserInfoDto.getToken());
        }
        //TODO 设置 粉丝数，关注数，硬币数
        return getSuccessResponseVO(tokenUserInfoDto);
    }

    @RequestMapping("/logout")
    public ResponseVO logout(HttpServletResponse response) {
        cleanCookie(response);
        log.info("退出登录");
        return getSuccessResponseVO(null);
    }
}