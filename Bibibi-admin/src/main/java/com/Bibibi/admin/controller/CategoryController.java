package com.Bibibi.admin.controller;

import com.Bibibi.component.RedisComponent;
import com.Bibibi.entity.vo.ResponseVO;
import com.wf.captcha.ArithmeticCaptcha;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController extends ABaseController{
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
        log.info("发送验证码成功");
        return getSuccessResponseVO(result);
    }
}
