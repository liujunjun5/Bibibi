package com.Bibibi.service.impl;

import com.Bibibi.component.RedisComponent;
import com.Bibibi.entity.config.AppConfig;
import com.Bibibi.entity.constants.Constants;
import com.Bibibi.entity.dto.UploadingFileDto;
import com.Bibibi.entity.po.VideoInfoFilePost;
import com.Bibibi.entity.po.VideoInfoPost;
import com.Bibibi.entity.query.SimplePage;
import com.Bibibi.entity.query.VideoInfoFilePostQuery;
import com.Bibibi.entity.query.VideoInfoPostQuery;
import com.Bibibi.entity.vo.PaginationResultVO;
import com.Bibibi.enums.*;
import com.Bibibi.exception.BusinessException;
import com.Bibibi.mappers.VideoInfoFilePostMappers;
import com.Bibibi.mappers.VideoInfoPostMappers;
import com.Bibibi.service.VideoInfoPostService;
import com.Bibibi.utils.FFmpegUtils;
import com.Bibibi.utils.StringTools;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.File;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Description:视频信息Service
 * @date:2024-11-11
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
	private AppConfig appConfig;

	@Resource
	private FFmpegUtils fFmpegUtils;

	@Autowired
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
	 * @throws BusinessException
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveVideoInfo(VideoInfoPost videoInfoPost, List<VideoInfoFilePost> uploadFileList) throws BusinessException {
		//客户端传来的文件大于系统设置中的最大值
		if (uploadFileList.size() > redisComponent.getSysSettingDto().getVideoPCount()) {
			throw new BusinessException(ResponseCodeEnum.CODE_600);
		}
		//已经有了视频id，准备做修改
		if (!StringTools.isEmpty(videoInfoPost.getVideoId())) {
			//获取数据库中的数据
			VideoInfoPost videoInfoPostDb = this.videoInfoPostMappers.selectByVideoId(videoInfoPost.getVideoId());
			//客户端传来有视频id，数据库中却不能找到-->错误传参
			if (videoInfoPostDb == null) {
				throw new BusinessException(ResponseCodeEnum.CODE_600);
			}
			//将要修改的视频信息如果有未审核或者未解码，不支持修改
			if (ArrayUtils.contains(new Integer[]{VideoStatusEnum.STATUS0.getStatus(), VideoStatusEnum.STATUS2.getStatus()}, videoInfoPostDb.getStatus())) {
				throw new BusinessException(ResponseCodeEnum.CODE_600);
			}
		}

		Date curDate = new Date();
		String videoId = videoInfoPost.getVideoId();
		List<VideoInfoFilePost> deleteFileList = new ArrayList<>();
		List<VideoInfoFilePost> addFileList = uploadFileList;

		//没有视频id，准备增加视频
		if (StringTools.isEmpty(videoId)) {
			videoId = StringTools.getRandomString(Constants.LENGTH_10);
			videoInfoPost.setVideoId(videoId);
			videoInfoPost.setCreateTime(curDate);
			videoInfoPost.setLastUpdateTime(curDate);
			videoInfoPost.setStatus(VideoStatusEnum.STATUS0.getStatus());
			this.videoInfoPostMappers.insert(videoInfoPost);
		} else {//有了视频id，准备修改视频信息
			VideoInfoFilePostQuery fileQuery = new VideoInfoFilePostQuery();
			//利用双重保险，防止重复，增加安全性
			fileQuery.setVideoId(videoId);
			fileQuery.setUserId(videoInfoPost.getUserId());
			List<VideoInfoFilePost> dbInfoFileList = this.videoInfoFilePostMappers.selectList(fileQuery);

			Map<String, VideoInfoFilePost> uploadFileMap = uploadFileList.stream().collect(Collectors.toMap(item -> item.getUploadId(), Function.identity(), (date1, date2) -> date2));

			Boolean updateFileName = false;
			for (VideoInfoFilePost fileInfo : dbInfoFileList) {
				VideoInfoFilePost updateFile = uploadFileMap.get(fileInfo.getUploadId());
				if (updateFile == null) {
					deleteFileList.add(fileInfo);
				} else if (!updateFile.getFileName().equals(fileInfo.getFileName())) {
					updateFileName = true;
				}
			}

			addFileList = uploadFileList.stream().filter(item -> item.getFileId() == null).collect(Collectors.toList());

			videoInfoPost.setLastUpdateTime(curDate);

			//标记是否存在修改，标题、封面、标签、简介任意一项发生改变即存在修改
			Boolean changeVideoInfo = changeVideoInfo(videoInfoPost);
			if (addFileList != null && !addFileList.isEmpty()) {
				//将新增的视频状态设置为未解码
				videoInfoPost.setStatus(VideoStatusEnum.STATUS0.getStatus());
			} else if (changeVideoInfo || updateFileName) {
				//有修改，把状态改为待审核
				videoInfoPost.setStatus(VideoStatusEnum.STATUS2.getStatus());
			}
			this.videoInfoPostMappers.updateByVideoId(videoInfoPost, videoInfoPost.getVideoId());
		}

		if (!deleteFileList.isEmpty()) {
			List<String> delFileList = deleteFileList.stream().map(item -> item.getFileId()).collect(Collectors.toList());
			this.videoInfoFilePostMappers.deleteBatchByFileId(delFileList, videoInfoPost.getUserId());
			//TODO 优化点：删除视频文件使用消息队列完成
			List<String> delFilePathList = deleteFileList.stream().map(item -> item.getFilePath()).collect(Collectors.toList());
			redisComponent.addFile2DelQueue(videoId, delFilePathList);
		}

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
		if (addFileList != null && !addFileList.isEmpty()) {
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
			updateFilePost.setFilePath(Constants.FILE_COVER + fileDto.getFilePath());
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
	 * @throws BusinessException
	 */
	private void union(String dirPath, String toFilePath, Boolean delSource) throws BusinessException {
		File dir = new File(dirPath);
		if (!dir.exists()) {
			throw new BusinessException("目录不存在");
		}
		//获取目录下的所有文件，放在fileList数组中
		File fileList[] = dir.listFiles();
		//表示合并后的文件
		File targetFile = new File(toFilePath);
		try (RandomAccessFile writeFile = new RandomAccessFile(targetFile, "rw")) {
			byte[] b = new byte[1024 * 10];
			for (int i = 0; i < fileList.length; i++) {
				int len = -1;
				//创建读块文件的对象
				File chunkFile = new File(dirPath + File.separator + i);
				RandomAccessFile readFile = null;
				try {
					readFile = new RandomAccessFile(chunkFile, "r");
					while ((len = readFile.read(b)) != -1) {
						writeFile.write(b, 0, len);
					}
				} catch (Exception e) {
					log.error("合并分片失败", e);
					throw new BusinessException("合并文件失败");
				} finally {
					readFile.close();
				}
			}
		} catch (Exception e) {
			throw new BusinessException("合并文件" + dirPath + "出错了");
		} finally {
			if (delSource) {
				for (int i = 0; i < fileList.length; i++) {
					fileList[i].delete();
				}
			}
		}
	}

	private Boolean changeVideoInfo(VideoInfoPost videoInfoPost) {
		VideoInfoPost dbInfo = this.videoInfoPostMappers.selectByVideoId(videoInfoPost.getVideoId());
		//标题、封面、标签、简介
		if (!videoInfoPost.getVideoCover().equals(dbInfo.getVideoCover())
				|| !videoInfoPost.getVideoName().equals(dbInfo.getVideoName())
				|| !videoInfoPost.getTags().equals(dbInfo.getTags())
				|| !videoInfoPost.getIntroduction().equals(dbInfo.getIntroduction())) {
			return true;
		} else {
			return false;
		}
	}
}