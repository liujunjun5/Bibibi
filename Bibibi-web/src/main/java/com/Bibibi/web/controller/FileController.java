package com.Bibibi.web.controller;

import com.Bibibi.web.annotation.GlobalInterceptor;
import com.Bibibi.component.RedisComponent;
import com.Bibibi.entity.config.AppConfig;
import com.Bibibi.entity.constants.Constants;
import com.Bibibi.entity.dto.SysSettingDto;
import com.Bibibi.entity.dto.TokenUserInfoDto;
import com.Bibibi.entity.dto.UploadingFileDto;
import com.Bibibi.entity.dto.VideoPlayInfoDto;
import com.Bibibi.entity.po.VideoInfoFile;
import com.Bibibi.entity.vo.ResponseVO;
import com.Bibibi.enums.DateTimePatternEnum;
import com.Bibibi.enums.ResponseCodeEnum;
import com.Bibibi.exception.BusinessException;
import com.Bibibi.service.VideoInfoFileService;
import com.Bibibi.utils.DateUtils;
import com.Bibibi.utils.FFmpegUtils;
import com.Bibibi.utils.StringTools;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
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
import java.util.Date;

@RequestMapping("/file")
@RestController
@Validated
@Slf4j
public class FileController extends ABaseController {

    @Resource
    private AppConfig appConfig;

    @Resource
    private RedisComponent redisComponent;

    @Resource
    private VideoInfoFileService videoInfoFileService;

    @Resource
    private FFmpegUtils fFmpegUtils;

    @RequestMapping("/getResource")
    public void getResource(HttpServletResponse response, @NotNull String sourceName) throws IOException, BusinessException {
        if (!StringTools.pathIsOk(sourceName)) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        String suffix = StringTools.getFileSuffix(sourceName);
        response.setContentType("image/" + suffix.replace(".", ""));
        response.setHeader("Cache-Control", "max-age=2592000");
        readFile(response, sourceName);
    }

    protected void readFile(HttpServletResponse response, String filePath) {
        File file = new File(appConfig.getProjectFolder() + Constants.FILE_FOLDER + filePath);
        if (!file.exists()) {
            return;
        }
        try (OutputStream out = response.getOutputStream(); FileInputStream in = new FileInputStream(file)) {
            byte[] byteData = new byte[1024];
            int len = 0;
            while ((len = in.read(byteData)) != -1) {
                out.write(byteData, 0, len);
            }
            out.flush();
        } catch (Exception e) {
            log.error("读取文件异常", e);
        }
    }

    @GlobalInterceptor(checkLogin = true)
    @RequestMapping("/preUploadVideo")
    public ResponseVO preUploadVideo(@NotEmpty String fileName, @NotNull Integer chunks) {
        TokenUserInfoDto tokenUserInfoDto = getTokenUserInfoDto();
        String uploadId = redisComponent.savePreVideoFileInfo(tokenUserInfoDto.getUserId(), fileName, chunks);
        return getSuccessResponseVO(uploadId);
    }

    /**
     * @param chunkFile  上传的文件
     * @param chunkIndex 文件的索引
     * @param uploadId   上传的id
     * @return
     * @throws BusinessException
     */
    @GlobalInterceptor(checkLogin = true)
    @RequestMapping("/uploadVideo")
    public ResponseVO uploadVideo(@NotNull MultipartFile chunkFile, @NotNull Integer chunkIndex, @NotEmpty String uploadId) throws BusinessException, IOException {
        TokenUserInfoDto tokenUserInfoDto = getTokenUserInfoDto();
        UploadingFileDto fileDto = redisComponent.getUploadVideoFile(tokenUserInfoDto.getUserId(), uploadId);
        if (fileDto == null) {
            throw new BusinessException("文件不存在，请重新上传");
        }
        SysSettingDto sysSettingDto = redisComponent.getSysSettingDto();
        if (fileDto.getFileSize() > sysSettingDto.getVideoSize() * Constants.MB_SIZE) {
            throw new BusinessException("文件超过大小限制");
        }
        //判断分片,
        if (chunkIndex - 1 > fileDto.getChunkIndex() || chunkIndex > fileDto.getChunks()) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        String folder = appConfig.getProjectFolder() + Constants.FILE_FOLDER + Constants.FILE_FOLDER_TMP + fileDto.getFilePath();
        File targetFile = new File(folder + "/" + chunkIndex);
        log.info("文件存储至{}", folder);
        chunkFile.transferTo(targetFile);
        fileDto.setChunkIndex(chunkIndex);
        fileDto.setFileSize(fileDto.getFileSize() + chunkFile.getSize());
        redisComponent.updateVideoFileInfo(tokenUserInfoDto.getUserId(), fileDto);
        return getSuccessResponseVO(null);
    }

    @GlobalInterceptor(checkLogin = true)
    @RequestMapping("/delUploadVideo")
    public ResponseVO delUploadVideo(@NotEmpty String uploadId) throws BusinessException, IOException {
        TokenUserInfoDto tokenUserInfoDto = getTokenUserInfoDto();
        UploadingFileDto fileDto = redisComponent.getUploadVideoFile(tokenUserInfoDto.getUserId(), uploadId);
        if (fileDto == null) {
            throw new BusinessException("文件不存在，请重新上传");
        }
        //删除redis缓存
        redisComponent.delVideoFileInfo(tokenUserInfoDto.getUserId(), uploadId);
        //删除服务端的文件
        FileUtils.deleteDirectory(new File(appConfig.getProjectFolder() + Constants.FILE_FOLDER + Constants.FILE_FOLDER_TMP + fileDto.getFilePath()));
        log.info("文件删除完毕");
        return getSuccessResponseVO(uploadId);
    }

    @GlobalInterceptor(checkLogin = true)
    @RequestMapping("/uploadImage")
    public ResponseVO uploadImage(@NotNull MultipartFile file, @NotNull Boolean createThumbnail) throws IOException, BusinessException {
        String day = DateUtils.format(new Date(), DateTimePatternEnum.YYYYMMDD.getPattern());
        String folder = appConfig.getProjectFolder() + Constants.FILE_FOLDER + Constants.FILE_COVER + day;
        File folderFile = new File(folder);
        if (!folderFile.exists()) {
            folderFile.mkdirs() ;
        }
        String fileName = file.getOriginalFilename();
        String fileSuffix = StringTools.getFileSuffix(fileName);
        String realFileName = StringTools.getRandomString(Constants.LENGTH_30) + fileSuffix;
        String filePath = folder + "/" + realFileName;
        file.transferTo(new File(filePath));
        if (createThumbnail != null && createThumbnail) {
            fFmpegUtils.createImageThumbnail(filePath);
        }
        return getSuccessResponseVO(Constants.FILE_COVER + day + "/" + realFileName);
    }

    @RequestMapping("/videoResource/{fileId}")
    public void videoResource(HttpServletResponse response, @PathVariable @NotEmpty String fileId) {
        VideoInfoFile videoInfoFile = videoInfoFileService.getByFileId(fileId);
        String filePath = videoInfoFile.getFilePath();
        readFile(response, filePath + "/" + Constants.M3U8_NAME);

        VideoPlayInfoDto videoPlayInfoDto = new VideoPlayInfoDto();
        videoPlayInfoDto.setVideoId(videoInfoFile.getVideoId());
        videoPlayInfoDto.setFileIndex(videoInfoFile.getFileIndex());

        TokenUserInfoDto tokenUserInfoDto = getTokenInfoFromCookie();
        if (tokenUserInfoDto != null) {
            videoPlayInfoDto.setUserId(tokenUserInfoDto.getUserId());
        }
        redisComponent.addVideoPlay(videoPlayInfoDto);
    }

    @RequestMapping("/videoResource/{fileId}/{ts}")
    public void videoResourceTs(HttpServletResponse response, @PathVariable @NotEmpty String fileId, @PathVariable @NotEmpty String ts) {
        VideoInfoFile videoInfoFile = videoInfoFileService.getByFileId(fileId);
        String filePath = videoInfoFile.getFilePath();
        readFile(response, filePath + "/" + ts);
    }
}
