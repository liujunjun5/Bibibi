package com.Bibibi.utils;

import com.Bibibi.entity.config.AppConfig;
import com.Bibibi.entity.constants.Constants;
import com.Bibibi.exception.BusinessException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class FFmpegUtils {
    @Resource
    private AppConfig appConfig;

    public void createImageThumbnail(String filePath) throws BusinessException {
        String CMD = "ffmpeg -i \"%s\" -vf scale=200:-1 \"%\"";
        CMD = String.format(CMD, filePath, filePath + Constants.IMAGE_Thumbnail_SUFFIX);
        ProcessUtils.executeCommand(CMD, appConfig.getShowFFmpegLog());

    }
}
