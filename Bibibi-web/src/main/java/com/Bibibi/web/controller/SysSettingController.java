package com.Bibibi.web.controller;

import com.Bibibi.component.RedisComponent;
import com.Bibibi.entity.config.AppConfig;
import com.Bibibi.entity.constants.Constants;
import com.Bibibi.entity.dto.SysSettingDto;
import com.Bibibi.entity.dto.TokenUserInfoDto;
import com.Bibibi.entity.dto.UploadingFileDto;
import com.Bibibi.entity.vo.ResponseVO;
import com.Bibibi.enums.ResponseCodeEnum;
import com.Bibibi.exception.BusinessException;
import com.Bibibi.utils.StringTools;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

@RequestMapping("/sysSetting")
@RestController
@Validated
@Slf4j
public class SysSettingController extends ABaseController {

    @Resource
    private AppConfig appConfig;

    @Resource
    private RedisComponent redisComponent;

    @RequestMapping("/getSetting")
    public ResponseVO getSetting()  {
        return getSuccessResponseVO(redisComponent.getSysSettingDto());
    }
}
