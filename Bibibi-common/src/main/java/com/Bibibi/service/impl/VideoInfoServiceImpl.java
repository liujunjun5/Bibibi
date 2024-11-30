package com.Bibibi.service.impl;

import com.Bibibi.component.RedisComponent;
import com.Bibibi.entity.config.AppConfig;
import com.Bibibi.entity.po.*;
import com.Bibibi.entity.query.*;
import com.Bibibi.entity.vo.PaginationResultVO;
import com.Bibibi.enums.PageSize;
import com.Bibibi.enums.ResponseCodeEnum;
import com.Bibibi.exception.BusinessException;
import com.Bibibi.mappers.*;
import com.Bibibi.service.VideoInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Description: 视频信息Service
 * @date: 2024-11-11
 * @author: liujun
 */

@Slf4j
@Service("VideoInfoService")
public class VideoInfoServiceImpl implements VideoInfoService {

	private static ExecutorService executorService = Executors.newFixedThreadPool(10);

	@Resource
	private AppConfig appConfig;

	@Resource
	private VideoInfoFilePostMappers<VideoInfoFilePost, VideoInfoFilePostQuery> videoInfoFilePostMappers;

	@Resource
	private VideoDanmuMappers<VideoDanmu, VideoDanmuQuery> videoDanmuMappers;

	@Resource
	private VideoCommentMappers<VideoComment, VideoCommentQuery> videoCommentMappers;

	@Resource
	private VideoInfoMappers<VideoInfo, VideoInfoQuery> videoInfoMappers;

	@Resource
	private VideoInfoPostMappers<VideoInfoPost, VideoInfoPostQuery> videoInfoPostMappers;

	@Resource
	private UserInfoMappers<UserInfo, UserInfoQuery> userInfoMappers;

	@Resource
	private RedisComponent redisComponent;

	@Resource
	private VideoInfoFileMappers videoInfoFileMappers;

	/**
	 * 根据条件查询列表
	 */
	public List<VideoInfo> findListByParam(VideoInfoQuery query) {
		return this.videoInfoMappers.selectList(query);
	}

	/**
	 * 根据条件查询数量
	 */
	public Integer findCountByParam(VideoInfoQuery query) {
		return this.videoInfoMappers.selectCount(query);
	}

	/**
	 * 分页查询
	 */
	public PaginationResultVO<VideoInfo> findByPage(VideoInfoQuery query) {
		Integer count = this.findCountByParam(query);
		Integer pageSize = query.getPageSize()==null?PageSize.SIZE15.getSize():query.getPageSize();
		SimplePage page = new SimplePage(query.getPageNo(), count, pageSize);
		query.setSimplePage(page);
		List<VideoInfo> list = this.findListByParam(query);
		PaginationResultVO<VideoInfo> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
		return result;
	}

	/**
	 * 新增
	 */
	public Integer add(VideoInfo bean) {
		return this.videoInfoMappers.insert(bean);
	}

	/**
	 * 批量新增
	 */
	public Integer addBatch(List<VideoInfo> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.videoInfoMappers.insertBatch(listBean);
	}

	/**
	 * 批量新增或更新
	 */
	public Integer addOrUpdateBatch(List<VideoInfo> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.videoInfoMappers.insertOrUpdateBatch(listBean);
	}


	/**
	 * 根据VideoId查询
	 */
	public VideoInfo getByVideoId(String videoId) {
		return this.videoInfoMappers.selectByVideoId(videoId);
	}

	/**
	 * 根据VideoId更新
	 */
	public Integer updateByVideoId(VideoInfo bean, String videoId) {
		return this.videoInfoMappers.updateByVideoId(bean, videoId);
	}

	/**
	 * 根据VideoId删除
	 */
	public Integer deleteByVideoId(String videoId) {
		return this.videoInfoMappers.deleteByVideoId(videoId);
	}

	@Override
	public void changeInteraction(String videoId, String userId, String interaction) {
		VideoInfo videoInfo = new VideoInfo();
		videoInfo.setInteraction(interaction);
		VideoInfoQuery videoInfoQuery = new VideoInfoQuery();
		videoInfoQuery.setUserId(userId);
		videoInfoQuery.setVideoId(videoId);
		videoInfoMappers.updateByParam(videoInfo, videoInfoQuery);
		VideoInfoPost videoInfoPost = new VideoInfoPost();
		videoInfoPost.setInteraction(interaction);
		VideoInfoPostQuery videoInfoPostQuery = new VideoInfoPostQuery();
		videoInfoPostQuery.setVideoId(videoId);
		videoInfoPostQuery.setUserId(userId);
		videoInfoPostMappers.updateByParam(videoInfoPost, videoInfoPostQuery);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void deleteVideo(String videoId, String userId) throws BusinessException {
		VideoInfo videoInfo = this.videoInfoMappers.selectByVideoId(videoId);
		if (videoInfo == null || userId != null && !userId.equals(videoInfo.getUserId())) {
			throw new BusinessException(ResponseCodeEnum.CODE_404);
		}
		this.videoInfoMappers.deleteByVideoId(videoId);
		this.videoInfoPostMappers.deleteByVideoId(videoId);
//		SysSettingDto sysSettingDto = redisComponent.getSysSettingDto();
		//TODO 減去用戶加硬幣
		//TODO 刪除es信息
		executorService.execute(() -> {

			VideoInfoFileQuery videoInfoFileQuery = new VideoInfoFileQuery();
			videoInfoFileQuery.setVideoId(videoId);
			//查询分P
			List<VideoInfoFile> videoInfoFileList = this.videoInfoFileMappers.selectList(videoInfoFileQuery);

			//删除分P
			videoInfoFileMappers.deleteByParam(videoInfoFileQuery);

			VideoInfoFilePostQuery videoInfoFilePostQuery = new VideoInfoFilePostQuery();
			videoInfoFilePostQuery.setVideoId(videoId);
			videoInfoFilePostMappers.deleteByParam(videoInfoFilePostQuery);

			//删除弹幕
			VideoDanmuQuery videoDanmuQuery = new VideoDanmuQuery();
			videoDanmuQuery.setVideoId(videoId);
			videoDanmuMappers.deleteByParam(videoDanmuQuery);

			//删除评论
			VideoCommentQuery videoCommentQuery = new VideoCommentQuery();
			videoCommentQuery.setVideoId(videoId);
			videoCommentMappers.deleteByParam(videoCommentQuery);

			//删除文件
			for (VideoInfoFile item : videoInfoFileList) {
				try {
					FileUtils.deleteDirectory(new File(appConfig.getProjectFolder() + item.getFilePath()));
				} catch (IOException e) {
					log.error("删除文件失败，文件路径:{}", item.getFilePath());
				}
			}
		});
	}
}