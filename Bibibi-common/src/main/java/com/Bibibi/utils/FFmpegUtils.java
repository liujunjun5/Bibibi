package com.Bibibi.utils;

import com.Bibibi.entity.config.AppConfig;
import com.Bibibi.entity.constants.Constants;
import com.Bibibi.exception.BusinessException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.math.BigDecimal;

@Component
public class FFmpegUtils {

    @Resource
    private AppConfig appConfig;

    /**
     * 生成指定路径下图片的缩略图
     *
     * @param filePath 文件路径
     * @throws BusinessException
     */
    public void createImageThumbnail(String filePath) throws BusinessException {
        String CMD = "ffmpeg -i \"%s\" -vf scale=200:-1 \"%\"";
        CMD = String.format(CMD, filePath, filePath + Constants.IMAGE_Thumbnail_SUFFIX);
        ProcessUtils.executeCommand(CMD, appConfig.getShowFFmpegLog());
    }

    /**
     * 获取指定视频的时长
     *
     * @param completeVideo
     * @return
     * @throws BusinessException
     */
    public Integer getVideoInfoDuration(String completeVideo) throws BusinessException {
        final String CMD_GET_CODE = "ffprobe -v error -show_entries format=duration -of default=noprint_wrappers=1:nokey=1 \"%s\"";
        String CMD = String.format(CMD_GET_CODE, completeVideo);
        String result = ProcessUtils.executeCommand(CMD, appConfig.getShowFFmpegLog());
        if (StringTools.isEmpty(result)) {
            return 0;
        }
        result = result.replace("\n", "");
        return new BigDecimal(result).intValue();
    }

    /**
     *
     * @param videoFilePath
     * @return
     */
    public String getVideoCodec(String videoFilePath) throws BusinessException {
        final String CMD_GET_CODE = "ffprobe -v error -select_streams v:0 -show_entries stream=codec_name \"%s\"";
        String cmd = String.format(CMD_GET_CODE, videoFilePath);
        String result = ProcessUtils.executeCommand(cmd, appConfig.getShowFFmpegLog());
        result = result.replace("\n", "");
        result = result.substring(result.indexOf("=") + 1);
        String codec = result.substring(0, result.indexOf("["));
        return codec;
    }

    /**
     * 将编码方式从Hevc转为H264
     * @param newFileName
     * @param videoFilePath
     * @throws BusinessException
     */
//    public void convertHevc2Mp4(String newFileName, String videoFilePath) throws BusinessException {
//        String CMD_HEVC_264 = "ffmpeg -i %s -c:v libx264 -crf 20 %s";
//        String cmd = String.format(CMD_HEVC_264, newFileName, videoFilePath);
//        ProcessUtils.executeCommand(cmd, appConfig.getShowFFmpegLog());
//    }

    public void convertHevc2Mp4(String newFileName, String videoFilePath) throws BusinessException {
        String CMD_HEVC_264 = "ffmpeg -i %s -c:v libx264 -crf 20 %s";
        String cmd = String.format(CMD_HEVC_264, newFileName, videoFilePath);
        ProcessUtils.executeCommand(cmd, appConfig.getShowFFmpegLog());
    }

    /**
     * 将文件格式从MP4转为TS-->
     * @param tsFolder
     * @param videoFilePath
     */
    public void convertVideo2Ts(File tsFolder, String videoFilePath) throws BusinessException {
        final String CMD_TRANSFER_2TS = "ffmpeg -y -i \"%s\"  -vcodec copy -acodec copy -bsf:v h264_mp4toannexb \"%s\"";
        final String CMD_CUT_TS = "ffmpeg -i \"%s\" -c copy -map 0 -f segment -segment_list \"%s\" -segment_time 10 %s/%%4d.ts";
        String tsPath = tsFolder + "/" + Constants.TS_NAME;
        //生成.ts
        String cmd = String.format(CMD_TRANSFER_2TS, videoFilePath, tsPath);
        ProcessUtils.executeCommand(cmd, appConfig.getShowFFmpegLog());
        //生成索引文件.m3u8 和切片.ts
        cmd = String.format(CMD_CUT_TS, tsPath, tsFolder.getPath() + "/" + Constants.M3U8_NAME, tsFolder.getPath());
        ProcessUtils.executeCommand(cmd, appConfig.getShowFFmpegLog());
        //删除index.ts
        new File(tsPath).delete();
    }
}
