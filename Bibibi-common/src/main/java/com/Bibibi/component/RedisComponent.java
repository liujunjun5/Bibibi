package com.Bibibi.component;

import com.Bibibi.entity.config.AppConfig;
import com.Bibibi.entity.constants.Constants;
import com.Bibibi.entity.dto.SysSettingDto;
import com.Bibibi.entity.dto.TokenUserInfoDto;
import com.Bibibi.entity.dto.UploadingFileDto;
import com.Bibibi.entity.po.CategoryInfo;
import com.Bibibi.entity.po.VideoInfoFilePost;
import com.Bibibi.enums.DateTimePatternEnum;
import com.Bibibi.redis.RedisUtils;
import com.Bibibi.utils.DateUtils;
import com.Bibibi.utils.StringTools;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.UUID;


@Component
public class RedisComponent {

    @Resource
    private RedisUtils redisUtils;

    @Resource
    private AppConfig appConfig;

//    public String saveCheckCode(String code) {
//        String checkCodeKey = UUID.randomUUID().toString();
//        redisUtils.setex(Constants.REDIS_KEY_CHECK_CODE + checkCodeKey, code, Constants.REDIS_KEY_EXPIRES_ONE_MIN * 10);
//        return checkCodeKey;
//    }
    public String saveCheckCode(String code) {
        String checkCodeKey = UUID.randomUUID().toString();
        redisUtils.setex(Constants.REDIS_KEY_CHECK_CODE + checkCodeKey, code, Constants.REDIS_KEY_EXPIRES_ONE_MIN * 10);
        return checkCodeKey;
    }

    public String getCode(String checkCodeKey) {
        return (String) redisUtils.get(Constants.REDIS_KEY_CHECK_CODE + checkCodeKey);
    }

    public void cleanCheckCode(String checkCodeKey) {
        redisUtils.delete(Constants.REDIS_KEY_CHECK_CODE + checkCodeKey);
    }

    public void cleanToken(String token) {
        redisUtils.delete(Constants.REDIS_KEY_TOKEN_WEB + token);
    }

    public void cleanToken4Admin(String token) {
        redisUtils.delete(Constants.REDIS_KEY_TOKEN_ADMIN + token);
    }

    public void saveTokenInfo(TokenUserInfoDto tokenUserInfoDto) {
        String token = UUID.randomUUID().toString();
        tokenUserInfoDto.setExpireAt(System.currentTimeMillis() + Constants.REDIS_KEY_EXPIRES_ONE_DAY * 7);
        tokenUserInfoDto.setToken(token);
        redisUtils.setex(Constants.REDIS_KEY_TOKEN_WEB + token, tokenUserInfoDto, System.currentTimeMillis() + Constants.REDIS_KEY_EXPIRES_ONE_DAY * 7);
    }

    public TokenUserInfoDto getTokenInfo(String token) {
        return (TokenUserInfoDto) redisUtils.get(Constants.REDIS_KEY_TOKEN_WEB + token);
    }

    public String getTokenInfo4Admin(String token) {
        return (String) redisUtils.get(Constants.REDIS_KEY_TOKEN_ADMIN + token);
    }

    public String saveTokenInfo4Admin(String account) {
        String token = UUID.randomUUID().toString();
        redisUtils.setex(Constants.REDIS_KEY_TOKEN_ADMIN + token, account, Constants.REDIS_KEY_EXPIRES_ONE_DAY);
        return token;
    }

    public void saveCategoryList(List<CategoryInfo> categoryInfoList) {
        redisUtils.set(Constants.REDIS_KEY_CATEGORY_LIST, categoryInfoList);
    }

    public List<CategoryInfo> getCategoryList() {
        return (List<CategoryInfo>) redisUtils.get(Constants.REDIS_KEY_CATEGORY_LIST);
    }

    public String savePreVideoFileInfo(String userId, String fileName, Integer chunks) {
        String uploadId = StringTools.getRandomString(Constants.LENGTH_15);
        UploadingFileDto fileDto = new UploadingFileDto();
        fileDto.setChunks(chunks);
        fileDto.setUploadId(uploadId);
        fileDto.setFileName(fileName);
        fileDto.setChunkIndex(0);
        String day = DateUtils.format(new Date(), DateTimePatternEnum.YYYYMMDD.getPattern());
        String filePath = day + "/" + userId + uploadId;
        String folder = appConfig.getProjectFolder() + Constants.FILE_FOLDER + Constants.FILE_FOLDER_TMP + filePath;
        File folderFile = new File(folder);
        if (!folderFile.exists()) {
            folderFile.mkdirs();
        }

        fileDto.setFilePath(filePath);
        redisUtils.setex(Constants.REDIS_KEY_UPLOADING_FILE + userId + uploadId, fileDto, Constants.REDIS_KEY_EXPIRES_ONE_DAY);
        return uploadId;
    }

    public UploadingFileDto getUploadVideoFile(String userId, String uploadId) {
        return (UploadingFileDto) redisUtils.get(Constants.REDIS_KEY_UPLOADING_FILE + userId + uploadId);
    }

    public SysSettingDto getSysSettingDto() {
        SysSettingDto sysSettingDto = (SysSettingDto) redisUtils.get(Constants.REDIS_KEY_SYS_SETTING);
        if (sysSettingDto == null) {
            sysSettingDto = new SysSettingDto();
        }
        return sysSettingDto;
    }

    public void updateVideoFileInfo(String userId, UploadingFileDto fileDto) {
        redisUtils.setex(Constants.REDIS_KEY_UPLOADING_FILE + userId + fileDto.getUploadId(), fileDto, Constants.REDIS_KEY_EXPIRES_ONE_DAY);
    }

    public void delVideoFileInfo(String userId, String uploadId) {
        redisUtils.delete(Constants.REDIS_KEY_UPLOADING_FILE + userId + uploadId);
    }

    //在消息队列放一天，准备删除
    public void addFile2DelQueue(String videoId, List<String> filePathList) {
        redisUtils.lpushAll(Constants.REDIS_KEY_FILE_DEL+videoId, filePathList, Constants.REDIS_KEY_EXPIRES_ONE_DAY);
    }

    public void addFile2TransferQueue(List<VideoInfoFilePost> addFileList) {
        redisUtils.lpushAll(Constants.REDIS_KEY_QUEUE_TRANSFER, addFileList, 0);
    }

    public VideoInfoFilePost getFileFromTransferQueue() {
        return (VideoInfoFilePost) redisUtils.rpop(Constants.REDIS_KEY_QUEUE_TRANSFER);
    }
}
