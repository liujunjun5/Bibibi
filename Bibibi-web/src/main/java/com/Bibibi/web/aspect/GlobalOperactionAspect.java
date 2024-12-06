package com.Bibibi.web.aspect;

import com.Bibibi.web.annotation.GlobalInterceptor;
import com.Bibibi.entity.constants.Constants;
import com.Bibibi.entity.dto.TokenUserInfoDto;
import com.Bibibi.enums.ResponseCodeEnum;
import com.Bibibi.exception.BusinessException;
import com.Bibibi.redis.RedisUtils;
import com.Bibibi.utils.StringTools;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

@Aspect
@Component
@Slf4j
public class GlobalOperactionAspect {

    @Resource
    private RedisUtils redisUtils;

    @Before("@annotation(com.Bibibi.web.annotation.GlobalInterceptor)")
    public void interceptDo(JoinPoint point) throws BusinessException {
        Method method = ((MethodSignature) point.getSignature()).getMethod();
        GlobalInterceptor interceptor = method.getAnnotation(GlobalInterceptor.class);
        if (null == interceptor) {
            return;
        }
        if (interceptor.checkLogin()) {
            checkLogin();
        }
    }

    private void checkLogin() throws BusinessException {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = request.getHeader(Constants.TOKEN_KEY);
        if (StringTools.isEmpty(token)) {
            throw new BusinessException(ResponseCodeEnum.CODE_901);
        }
        TokenUserInfoDto tokenUserInfoDto = (TokenUserInfoDto) redisUtils.get(Constants.REDIS_KEY_TOKEN_WEB + token);
        if (tokenUserInfoDto == null) {
            throw new BusinessException(ResponseCodeEnum.CODE_901);
        }
    }
}
