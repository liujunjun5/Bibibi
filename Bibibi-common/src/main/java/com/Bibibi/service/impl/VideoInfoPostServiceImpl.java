package com.Bibibi.service.impl;

import com.Bibibi.component.EsSearchComponent;
import com.Bibibi.component.RedisComponent;
import com.Bibibi.entity.config.AppConfig;
import com.Bibibi.entity.constants.Constants;
import com.Bibibi.entity.dto.SysSettingDto;
import com.Bibibi.entity.dto.UploadingFileDto;
import com.Bibibi.entity.po.*;
import com.Bibibi.entity.query.*;
import com.Bibibi.entity.vo.PaginationResultVO;
import com.Bibibi.enums.*;
import com.Bibibi.exception.BusinessException;
import com.Bibibi.mappers.*;
import com.Bibibi.service.VideoInfoPostService;
import com.Bibibi.utils.CopyTools;
import com.Bibibi.utils.FFmpegUtils;
import com.Bibibi.utils.StringTools;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Description: 视频信息Service
 * @date: 2024-11-11
 * @author: liujun
 */
@Slf4j
@Service("VideoInfoPostService")
public class VideoInfoPostServiceImpl implements VideoInfoPostService {

	@Resource
	private VideoInfoPostMappers<VideoInfoPost, VideoInfoPostQuery> videoInfoPostMappers;

	@Resource
	private VideoInfoFilePostMappers<VideoInfoFilePost, VideoInfoFilePostQuery> videoInfoFilePostMappers;

	@Resource
	private VideoInfoMappers<VideoInfo, VideoInfoQuery> videoInfoMappers;

	@Resource
	private VideoInfoFileMappers<VideoInfoFile, VideoInfoFileQuery> videoInfoFileMappers;

	@Resource
	private UserInfoMappers<UserInfo, UserInfoQuery> userInfoMappers;

	@Resource
	private EsSearchComponent esSearchComponent;

	@Resource
	private AppConfig appConfig;

	@Resource
	private FFmpegUtils fFmpegUtils;

	@Resource
	private RedisComponent redisComponent;

	/**
	 * 根据条件查询列表
	 */
	public List<VideoInfoPost> findListByParam(VideoInfoPostQuery query) {
		return this.videoInfoPostMappers.selectList(query);
	}

	/**
	 * 根据条件查询数量
	 */
	public Integer findCountByParam(VideoInfoPostQuery query) {
		return this.videoInfoPostMappers.selectCount(query);
	}

	/**
	 * 分页查询
	 */
	public PaginationResultVO<VideoInfoPost> findByPage(VideoInfoPostQuery query) {
		Integer count = this.findCountByParam(query);
		Integer pageSize = query.getPageSize()==null?PageSize.SIZE15.getSize():query.getPageSize();

		SimplePage page = new SimplePage(query.getPageNo(), count, pageSize);
		query.setSimplePage(page);
		List<VideoInfoPost> list = this.findListByParam(query);
		PaginationResultVO<VideoInfoPost> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
		return result;
	}

	/**
	 * 新增
	 */
	public Integer add(VideoInfoPost bean) {
		return this.videoInfoPostMappers.insert(bean);
	}

	/**
	 * 批量新增
	 */
	public Integer addBatch(List<VideoInfoPost> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.videoInfoPostMappers.insertBatch(listBean);
	}

	/**
	 * 批量新增或更新
	 */
	public Integer addOrUpdateBatch(List<VideoInfoPost> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.videoInfoPostMappers.insertOrUpdateBatch(listBean);
	}


	/**
	 * 根据VideoId查询
	 */
	public VideoInfoPost getByVideoId(String videoId) {
		return this.videoInfoPostMappers.selectByVideoId(videoId);
	}

	/**
	 * 根据VideoId更新
	 */
	public Integer updateByVideoId(VideoInfoPost bean, String videoId) {
		return this.videoInfoPostMappers.updateByVideoId(bean, videoId);
	}

	/**
	 * 根据VideoId删除
	 */
	public Integer deleteByVideoId(String videoId) {
		return this.videoInfoPostMappers.deleteByVideoId(videoId);
	}

	/**
	 * 保存视频信息
	 *
	 * @param videoInfoPost  视频上传信息
	 * @param uploadFileList 文件信息（包括文件id，文件名称）
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveVideoInfo(VideoInfoPost videoInfoPost, List<VideoInfoFilePost> uploadFileList) throws BusinessException {
		if (uploadFileList.size() > redisComponent.getSysSettingDto().getVideoPCount()) {
			throw new BusinessException(ResponseCodeEnum.CODE_600);
		}

		if (!StringTools.isEmpty(videoInfoPost.getVideoId())) {
			VideoInfoPost videoInfoPostDb = this.videoInfoPostMappers.selectByVideoId(videoInfoPost.getVideoId());
			if (videoInfoPostDb == null) {
				throw new BusinessException(ResponseCodeEnum.CODE_600);
			}
			if (ArrayUtils.contains(new Integer[]{VideoStatusEnum.STATUS0.getStatus(), VideoStatusEnum.STATUS2.getStatus()}, videoInfoPostDb.getStatus())) {
				throw new BusinessException(ResponseCodeEnum.CODE_600);
			}
		}

		Date curDate = new Date();
		String videoId = videoInfoPost.getVideoId();
		List<VideoInfoFilePost> deleteFileList = new ArrayList();
		List<VideoInfoFilePost> addFileList = uploadFileList;

		if (StringTools.isEmpty(videoId)) {
			videoId = StringTools.getRandomString(Constants.LENGTH_10);
			videoInfoPost.setVideoId(videoId);
			videoInfoPost.setCreateTime(curDate);
			videoInfoPost.setLastUpdateTime(curDate);
			videoInfoPost.setStatus(VideoStatusEnum.STATUS0.getStatus());
			this.videoInfoPostMappers.insert(videoInfoPost);
		} else {
			//查询已经存在的视频
			VideoInfoFilePostQuery fileQuery = new VideoInfoFilePostQuery();
			fileQuery.setVideoId(videoId);
			fileQuery.setUserId(videoInfoPost.getUserId());
			List<VideoInfoFilePost> dbInfoFileList = this.videoInfoFilePostMappers.selectList(fileQuery);
			Map<String, VideoInfoFilePost> uploadFileMap = uploadFileList.stream().collect(Collectors.toMap(item -> item.getUploadId(), Function.identity(), (data1,
																																							  data2) -> data2));
			//删除的文件 -> 数据库中有，uploadFileList没有
			Boolean updateFileName = false;
			for (VideoInfoFilePost fileInfo : dbInfoFileList) {
				VideoInfoFilePost updateFile = uploadFileMap.get(fileInfo.getUploadId());
				if (updateFile == null) {
					deleteFileList.add(fileInfo);
				} else if (!updateFile.getFileName().equals(fileInfo.getFileName())) {
					updateFileName = true;
				}
			}
			//新增的文件  没有fileId就是新增的文件
			addFileList = uploadFileList.stream().filter(item -> item.getFileId() == null).collect(Collectors.toList());
			videoInfoPost.setLastUpdateTime(curDate);

			//判断视频信息是否有更改
			Boolean changeVideoInfo = this.changeVideoInfo(videoInfoPost);
			if (!addFileList.isEmpty()) {
				videoInfoPost.setStatus(VideoStatusEnum.STATUS0.getStatus());
			} else if (changeVideoInfo || updateFileName) {
				videoInfoPost.setStatus(VideoStatusEnum.STATUS2.getStatus());
			}
			this.videoInfoPostMappers.updateByVideoId(videoInfoPost, videoInfoPost.getVideoId());
		}

		//清除已经删除的数据
		if (!deleteFileList.isEmpty()) {
			List<String> delFileIdList = deleteFileList.stream().map(VideoInfoFilePost::getFileId).collect(Collectors.toList());
			this.videoInfoFilePostMappers.deleteBatchByFileId(delFileIdList, videoInfoPost.getUserId());
			//将要删除的视频加入消息队列
			List<String> delFilePathList = deleteFileList.stream().map(VideoInfoFilePost::getFilePath).collect(Collectors.toList());
			redisComponent.addFile2DelQueue(videoId, delFilePathList);
		}

		//更新视频信息
		Integer index = 1;
		for (VideoInfoFilePost videoInfoFile : uploadFileList) {
			videoInfoFile.setFileIndex(index++);
			videoInfoFile.setVideoId(videoId);
			videoInfoFile.setUserId(videoInfoPost.getUserId());
			if (videoInfoFile.getFileId() == null) {
				videoInfoFile.setFileId(StringTools.getRandomString(Constants.LENGTH_20));
				videoInfoFile.setUpdateType(VideoFileUpdateTypeEnum.UPDATE.getStatus());
				videoInfoFile.setTransferResult(VideoFileTransferResultEnum.TRANSFER.getStatus());
			}
		}
		this.videoInfoFilePostMappers.insertOrUpdateBatch(uploadFileList);


		//将需要转码的视频加入队列
		if (!addFileList.isEmpty()) {
			for (VideoInfoFilePost file : addFileList) {
				file.setUserId(videoInfoPost.getUserId());
				file.setVideoId(videoId);
			}
			redisComponent.addFile2TransferQueue(addFileList);
		}
	}

	/**
	 * 解码视频
	 *
	 * @param videoInfoFilePost
	 */
	@Override
	public void transferVideoFile(VideoInfoFilePost videoInfoFilePost) {
		VideoInfoFilePost updateFilePost = new VideoInfoFilePost();
		try {
			UploadingFileDto fileDto = redisComponent.getUploadVideoFile(videoInfoFilePost.getUserId(), videoInfoFilePost.getUploadId());
			//我会加油的，阿公阿婆，我一直想你们
			//把视频从临时文件拷贝到新的目录，准备做合并，解码操作
			String tempFilePath = appConfig.getProjectFolder() + Constants.FILE_FOLDER + Constants.FILE_FOLDER_TMP + fileDto.getFilePath();
			File tempFile = new File(tempFilePath);
			String targetFilePath = appConfig.getProjectFolder() + Constants.FILE_FOLDER + Constants.FILE_VIDEO + fileDto.getFilePath();
			File targetFile = new File(targetFilePath);
			if (!targetFile.exists()) {
				targetFile.mkdirs();
			}
			FileUtils.copyDirectory(tempFile, targetFile);

			//删除临时目录
			FileUtils.deleteDirectory(tempFile);
			redisComponent.delVideoFileInfo(videoInfoFilePost.getUserId(), videoInfoFilePost.getUploadId());

			//合并文件
			String completeVideo = targetFilePath + Constants.TEMP_VIDEO_NAME;
			this.union(targetFilePath, completeVideo, true);

			//获取播放时长
			Integer duration = fFmpegUtils.getVideoInfoDuration(completeVideo);
			updateFilePost.setDuration(duration);
			updateFilePost.setFileSize(new File(completeVideo).length());
			updateFilePost.setFilePath(Constants.FILE_VIDEO + fileDto.getFilePath());
			updateFilePost.setTransferResult(VideoFileTransferResultEnum.SUCCESS.getStatus());

			this.convertVideo2Ts(completeVideo);
		} catch (Exception e) {
			log.error("文件转码失败", e);
			//出现问题设置转码失败
			updateFilePost.setTransferResult(VideoFileTransferResultEnum.FAIL.getStatus());
		} finally {
			videoInfoFilePostMappers.updateByUploadIdAndUserId(updateFilePost, videoInfoFilePost.getUploadId(), videoInfoFilePost.getUserId());

			//检查是否有转码失败的视频文件
			VideoInfoFilePostQuery filePostQuery = new VideoInfoFilePostQuery();

			filePostQuery.setVideoId(videoInfoFilePost.getVideoId());
			filePostQuery.setTransferResult(VideoFileTransferResultEnum.FAIL.getStatus());
			Integer failCount = videoInfoFilePostMappers.selectCount(filePostQuery);
			//有的话，反馈给视频记录
			if (failCount > 0) {
				VideoInfoPost videoUpdate = new VideoInfoPost();
				videoUpdate.setStatus(VideoStatusEnum.STATUS1.getStatus());
				videoInfoPostMappers.updateByVideoId(videoUpdate, videoInfoFilePost.getVideoId());
				return;
			}

			//检查是否全部转码成功
			filePostQuery.setTransferResult(VideoFileTransferResultEnum.TRANSFER.getStatus());
			Integer transferCount = videoInfoFilePostMappers.selectCount(filePostQuery);
			if (transferCount == 0) {
				Integer duration = videoInfoFilePostMappers.sumDuration(videoInfoFilePost.getVideoId());
				VideoInfoPost videoUpdate = new VideoInfoPost();
				videoUpdate.setStatus(VideoStatusEnum.STATUS2.getStatus());
				videoUpdate.setDuration(duration);
				videoInfoPostMappers.updateByVideoId(videoUpdate, videoInfoFilePost.getVideoId());
			}
		}
	}

	/**
	 * 审核视频
	 * @param videoId 视频id
	 * @param status  审核结果
	 * @param reason  未通过理由
	 * @throws BusinessException
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void auditVideo(String videoId, Integer status, String reason) throws BusinessException {
		VideoStatusEnum videoStatusEnum = VideoStatusEnum.getByStatus(status);
		if (videoStatusEnum==null) {
			throw new BusinessException(ResponseCodeEnum.CODE_600);
		}
		VideoInfoPost videoInfoPost = new VideoInfoPost();
		videoInfoPost.setStatus(status);

		//将待审核的视频状态改变？审核通过否？
		VideoInfoPostQuery videoInfoPostQuery = new VideoInfoPostQuery();
		videoInfoPostQuery.setStatus(VideoStatusEnum.STATUS2.getStatus());
		videoInfoPostQuery.setVideoId(videoId);
		Integer audioCount = this.videoInfoPostMappers.updateByParam(videoInfoPost, videoInfoPostQuery);
		if (audioCount== 0) {
			throw new BusinessException("审核失败，请稍后重试");
		}
		if (status == 3) {
			/**
			 * 更新视频状态
			 */
			VideoInfoFilePost videoInfoFilePost = new VideoInfoFilePost();
			videoInfoFilePost.setUpdateType(VideoFileUpdateTypeEnum.NO_UPDATE.getStatus());

			VideoInfoFilePostQuery filePostQuery = new VideoInfoFilePostQuery();
			filePostQuery.setVideoId(videoId);
			this.videoInfoFilePostMappers.updateByParam(videoInfoFilePost, filePostQuery);

			VideoInfoPost infoPost = this.videoInfoPostMappers.selectByVideoId(videoId);

			/*
			  第一次发视频的话，增加积分
			 */
			VideoInfo dbVideoInfo = this.videoInfoMappers.selectByVideoId(videoId);
			if (dbVideoInfo == null) {
				SysSettingDto sysSettingDto = redisComponent.getSysSettingDto();
				userInfoMappers.updateCoinCountInfo(infoPost.getUserId(), sysSettingDto.getPostVideoCoinCount());
			}

			/*
			  将发布信息更新到正式表信息
			 */
			VideoInfo videoInfo = CopyTools.copy(infoPost, VideoInfo.class);
			this.videoInfoMappers.insertOrUpdate(videoInfo);

			/*
			  更新视频文件信息 --- 先删除原来的，变为最新的
			 */
			VideoInfoFileQuery videoInfoFileQuery = new VideoInfoFileQuery();
			videoInfoFileQuery.setVideoId(videoId);
			this.videoInfoFileMappers.deleteByParam(videoInfoFileQuery);

			VideoInfoFilePostQuery videoInfoFilePostQuery = new VideoInfoFilePostQuery();
			videoInfoFilePostQuery.setVideoId(videoId);
			List<VideoInfoFilePost> videoInfoFilePostList = this.videoInfoFilePostMappers.selectList(videoInfoFilePostQuery);

			List<VideoInfoFile> videoInfoFileList = CopyTools.copyList(videoInfoFilePostList, VideoInfoFile.class);
			this.videoInfoFileMappers.insertBatch(videoInfoFileList);

			/*
			  保存信息到es
			 */
			esSearchComponent.saveDoc(videoInfo);
		}



		/*
		  删除消息队列中的文件，上一批次添加进来的，用户删除的
		 */
		List<String> filePathList = redisComponent.getDelFileList(videoId);
		if (filePathList != null) {
			for (String path : filePathList) {
				File file = new File(appConfig.getProjectFolder() + Constants.FILE_FOLDER + path);
				if (file.exists()) {
					try {
						FileUtils.deleteDirectory(file);
					} catch (IOException e) {
						log.error("删除文件失败", e);
					}
				}
			}
		}

		redisComponent.cleanDelFileList(videoId);

	}

	/**
	 * @param videoId
	 */
	@Override
	public void addReadCount(String videoId) {
		this.videoInfoMappers.updateCountInfo(videoId, UserActionTypeEnum.VIDEO_PLAY.getField(), 1);
	}

	/**
	 * @param videoId
	 */
	@Override
	public void recommendVideo(String videoId) throws BusinessException {
		VideoInfo videoInfo = videoInfoMappers.selectByVideoId(videoId);
		if (videoInfo == null) {
			throw new BusinessException(ResponseCodeEnum.CODE_600);
		}
		Integer recommendType = null;
		if (VideoRecommendTypeEnum.RECOMMEND.getType().equals(videoInfo.getRecommendType())) {
			recommendType = VideoRecommendTypeEnum.NO_RECOMMEND.getType();
		} else {
			recommendType = VideoRecommendTypeEnum.RECOMMEND.getType();
		}
		VideoInfo updateInfo = new VideoInfo();
		updateInfo.setRecommendType(recommendType);
		videoInfoMappers.updateByVideoId(updateInfo, videoId);
	}

	private void convertVideo2Ts(String completeVideo) throws BusinessException {
		File videoFile = new File(completeVideo);
		File tsFolder = videoFile.getParentFile();
		String codec = fFmpegUtils.getVideoCodec(completeVideo);
		if (Constants.VIDEO_CODE_HEVC.equals(codec)) {
			String tempFileName = completeVideo + Constants.VIDEO_CODE_TEMP_FILE_SUFFIX;
			new File(completeVideo).renameTo(new File(tempFileName));
			fFmpegUtils.convertHevc2Mp4(tempFileName, completeVideo);
			new File(tempFileName).delete();
		}

		//转为Ts加切片
		fFmpegUtils.convertVideo2Ts(tsFolder, completeVideo);

		videoFile.delete();
	}

	/**
	 * 将指定目录中的多个文件合并为一个文件
	 *
	 * @param dirPath    待合并的文件所在目录
	 * @param toFilePath 合并后文件所在的位置
	 * @param delSource  是否删除源文件
	 */
	private void union(String dirPath, String toFilePath, Boolean delSource) throws BusinessException {
		File dir = new File(dirPath);
		if (!dir.exists()) {
			throw new BusinessException("目录不存在");
		}
		//获取目录下的所有文件，放在fileList数组中
		File[] fileList = dir.listFiles();
		//表示合并后的文件
		File targetFile = new File(toFilePath);
		try (RandomAccessFile writeFile = new RandomAccessFile(targetFile, "rw")) {
			byte[] b = new byte[1024 * 10];
			for (int i = 0; i < fileList.length; i++) {
				int len = -1;
				//创建读块文件的对象
				File chunkFile = new File(dirPath + File.separator + i);
                try (RandomAccessFile readFile = new RandomAccessFile(chunkFile, "r")) {
                    while ((len = readFile.read(b)) != -1) {
                        writeFile.write(b, 0, len);
                    }
                } catch (Exception e) {
                    log.error("合并分片失败", e);
                    throw new BusinessException("合并文件失败");
                }
			}
		} catch (Exception e) {
			throw new BusinessException("合并文件" + dirPath + "出错了");
		} finally {
			if (delSource) {
				for (File file : fileList) {
					file.delete();
				}
			}
		}
	}

	private Boolean changeVideoInfo(VideoInfoPost videoInfoPost) {
		VideoInfoPost dbInfo = this.videoInfoPostMappers.selectByVideoId(videoInfoPost.getVideoId());
		//标题、封面、标签、简介
		return !videoInfoPost.getVideoCover().equals(dbInfo.getVideoCover())
				|| !videoInfoPost.getVideoName().equals(dbInfo.getVideoName())
				|| !videoInfoPost.getTags().equals(dbInfo.getTags())
				|| !videoInfoPost.getIntroduction().equals(dbInfo.getIntroduction() == null ? "" : dbInfo.getIntroduction());
	}
}