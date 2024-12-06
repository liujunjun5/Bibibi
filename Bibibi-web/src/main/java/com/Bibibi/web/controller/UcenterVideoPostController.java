package com.Bibibi.web.controller;

import com.Bibibi.web.annotation.GlobalInterceptor;
import com.Bibibi.entity.dto.TokenUserInfoDto;
import com.Bibibi.entity.po.VideoInfoFilePost;
import com.Bibibi.entity.po.VideoInfoPost;
import com.Bibibi.entity.query.VideoInfoFilePostQuery;
import com.Bibibi.entity.query.VideoInfoPostQuery;
import com.Bibibi.entity.vo.PaginationResultVO;
import com.Bibibi.entity.vo.ResponseVO;
import com.Bibibi.entity.vo.VideoPostEditInfoVo;
import com.Bibibi.entity.vo.VideoStatusCountInfoVO;
import com.Bibibi.enums.ResponseCodeEnum;
import com.Bibibi.enums.VideoStatusEnum;
import com.Bibibi.exception.BusinessException;
import com.Bibibi.service.VideoInfoFilePostService;
import com.Bibibi.service.VideoInfoPostService;
import com.Bibibi.service.VideoInfoService;
import com.Bibibi.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@RestController
@RequestMapping("/ucenter")
@Validated
@Slf4j
public class UcenterVideoPostController extends ABaseController {

    @Resource
    private VideoInfoService videoInfoService;

    @Resource
    private VideoInfoFilePostService videoInfoFilePostService;

    @Resource
    private VideoInfoPostService videoInfoPostService;

    /**
     * @param videoId        视频id
     * @param videoCover     视频封面
     * @param videoName      标题
     * @param pCategoryId    一级分类id
     * @param categoryId     二级分类id
     * @param postType       类型
     * @param introduction   简介
     * @param tags           标签
     * @param interaction    互动设置
     * @param uploadFileList 文件集合
     * @return
     */
    @GlobalInterceptor(checkLogin = true)
    @RequestMapping("/postVideo")
    public ResponseVO postVideo(String videoId, @NotEmpty String videoCover, @NotEmpty @Size(max = 100) String videoName, @NotNull Integer pCategoryId, Integer categoryId, @NotNull Integer postType, @Size(max = 2000) String introduction, @NotEmpty @Size(max = 300) String tags, @Size(max = 3) String interaction, @NotEmpty String uploadFileList) throws BusinessException {
        TokenUserInfoDto tokenUserInfoDto = getTokenUserInfoDto();
        List<VideoInfoFilePost> filePostList = JsonUtils.convertJsonArray2List(uploadFileList, VideoInfoFilePost.class);

        VideoInfoPost videoInfo = new VideoInfoPost();
        videoInfo.setVideoId(videoId);
        videoInfo.setVideoName(videoName);
        videoInfo.setVideoCover(videoCover);
        videoInfo.setPCategoryId(pCategoryId);
        videoInfo.setCategoryId(categoryId);
        videoInfo.setPostType(postType);
        videoInfo.setTags(tags);
        videoInfo.setIntroduction(introduction);
        videoInfo.setInteraction(interaction);
        videoInfo.setUserId(tokenUserInfoDto.getUserId());

        videoInfoPostService.saveVideoInfo(videoInfo, filePostList);
        return getSuccessResponseVO(null);
    }

    @GlobalInterceptor(checkLogin = true)
    @RequestMapping("/loadVideoList")
    public ResponseVO loadVideoPost(Integer status, Integer pageNo, String videoNameFuzzy) {
        TokenUserInfoDto tokenUserInfoDto = getTokenUserInfoDto();

        VideoInfoPostQuery videoInfoQuery = new VideoInfoPostQuery();
        videoInfoQuery.setUserId(tokenUserInfoDto.getUserId());
        videoInfoQuery.setPageNo(pageNo);
        videoInfoQuery.setOrderBy("create_time desc");

        if (status != null) {
            if (status == -1) {
                videoInfoQuery.setExcludeStatusArray(new Integer[]{VideoStatusEnum.STATUS3.getStatus(), VideoStatusEnum.STATUS4.getStatus()});
            } else {
                videoInfoQuery.setStatus(status);
            }
        }
        videoInfoQuery.setVideoNameFuzzy(videoNameFuzzy);
        videoInfoQuery.setQueryCountInfo(true);
        PaginationResultVO resultVO = videoInfoPostService.findByPage(videoInfoQuery);

        return getSuccessResponseVO(resultVO);
    }

    @GlobalInterceptor(checkLogin = true)
    @RequestMapping("/getVideoCountInfo")
    public ResponseVO getVideoCountInfo() {
        TokenUserInfoDto tokenUserInfoDto = getTokenUserInfoDto();

        VideoInfoPostQuery videoInfoPostQuery = new VideoInfoPostQuery();
        videoInfoPostQuery.setUserId(tokenUserInfoDto.getUserId());

        videoInfoPostQuery.setStatus(VideoStatusEnum.STATUS3.getStatus());
        Integer auditPassCount = videoInfoPostService.findCountByParam(videoInfoPostQuery);

        videoInfoPostQuery.setStatus(VideoStatusEnum.STATUS4.getStatus());
        Integer auditFailCount = videoInfoPostService.findCountByParam(videoInfoPostQuery);

        videoInfoPostQuery.setStatus(null);
        videoInfoPostQuery.setExcludeStatusArray(new Integer[]{VideoStatusEnum.STATUS3.getStatus(), VideoStatusEnum.STATUS4.getStatus()});
        Integer inProgress = videoInfoPostService.findCountByParam(videoInfoPostQuery);

        VideoStatusCountInfoVO countInfoVO =  new VideoStatusCountInfoVO();
        countInfoVO.setAuditPassCount(auditPassCount);
        countInfoVO.setAuditFailCount(auditFailCount);
        countInfoVO.setInProgress(inProgress);

        return getSuccessResponseVO(countInfoVO);
    }

    @GlobalInterceptor(checkLogin = true)
    @RequestMapping("/getVideoByVideoId")
    public ResponseVO getVideoByVideoId(@NotEmpty String videoId) throws BusinessException {
        TokenUserInfoDto tokenUserInfoDto = getTokenUserInfoDto();

        VideoInfoPost videoInfoPost = this.videoInfoPostService.getByVideoId(videoId);
        if (videoInfoPost==null||!videoInfoPost.getUserId().equals(tokenUserInfoDto.getUserId())) {
            throw new BusinessException(ResponseCodeEnum.CODE_404);
        }
        VideoInfoFilePostQuery videoInfoFilePostQuery = new VideoInfoFilePostQuery();
        videoInfoFilePostQuery.setVideoId(videoId);
        videoInfoFilePostQuery.setOrderBy("file_index asc");
        List<VideoInfoFilePost> videoInfoFilePostList = this.videoInfoFilePostService.findListByParam(videoInfoFilePostQuery);
        VideoPostEditInfoVo vo = new VideoPostEditInfoVo();
        vo.setVideoInfo(videoInfoPost);
        vo.setVideoInfoFileList(videoInfoFilePostList);
        return getSuccessResponseVO(vo);
    }

    @GlobalInterceptor(checkLogin = true)
    @RequestMapping("/saveVideoInteraction")
    public ResponseVO saveVideoInteraction(@NotEmpty String videoId, String interaction) {
        TokenUserInfoDto tokenUserInfoDto = getTokenUserInfoDto();
        videoInfoService.changeInteraction(videoId, tokenUserInfoDto.getUserId(), interaction);
        return getSuccessResponseVO(null);
    }

    @GlobalInterceptor(checkLogin = true)
    @RequestMapping("/deleteVideo")
    public ResponseVO deleteVideo(@NotEmpty String videoId) throws BusinessException {
        TokenUserInfoDto tokenUserInfoDto = getTokenUserInfoDto();
        videoInfoService.deleteVideo(videoId, tokenUserInfoDto.getUserId());
        return getSuccessResponseVO(null);
    }
}
