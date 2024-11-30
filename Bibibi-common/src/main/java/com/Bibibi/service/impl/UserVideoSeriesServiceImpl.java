package com.Bibibi.service.impl;

import com.Bibibi.entity.po.UserVideoSeries;
import com.Bibibi.entity.po.UserVideoSeriesVideo;
import com.Bibibi.entity.po.VideoInfo;
import com.Bibibi.entity.query.SimplePage;
import com.Bibibi.entity.query.UserVideoSeriesQuery;
import com.Bibibi.entity.query.UserVideoSeriesVideoQuery;
import com.Bibibi.entity.query.VideoInfoQuery;
import com.Bibibi.entity.vo.PaginationResultVO;
import com.Bibibi.enums.PageSize;
import com.Bibibi.enums.ResponseCodeEnum;
import com.Bibibi.exception.BusinessException;
import com.Bibibi.mappers.UserVideoSeriesMappers;
import com.Bibibi.mappers.UserVideoSeriesVideoMappers;
import com.Bibibi.mappers.VideoInfoMappers;
import com.Bibibi.service.UserVideoSeriesService;
import com.Bibibi.utils.StringTools;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description:用户视频序列归档Service
 * @date:2024-11-29
 * @author: liujun
 */
@Service("UserVideoSeriesService")
public class UserVideoSeriesServiceImpl implements UserVideoSeriesService {

	@Resource
	private UserVideoSeriesMappers<UserVideoSeries, UserVideoSeriesQuery> userVideoSeriesMappers;

	@Resource
	private VideoInfoMappers<VideoInfo, VideoInfoQuery> videoInfoMappers;

	@Resource
	private UserVideoSeriesVideoMappers<UserVideoSeriesVideo, UserVideoSeriesVideoQuery> userVideoSeriesVideoMappers;

	/**
	 * 根据条件查询列表
	 */
	public List<UserVideoSeries> findListByParam(UserVideoSeriesQuery query) {
		return this.userVideoSeriesMappers.selectList(query);
	}

	/**
	 * 根据条件查询数量
	 */
	public Integer findCountByParam(UserVideoSeriesQuery query) {
		return this.userVideoSeriesMappers.selectCount(query);
	}

	/**
	 * 分页查询
	 */
	public PaginationResultVO<UserVideoSeries> findByPage(UserVideoSeriesQuery query) {
		Integer count = this.findCountByParam(query);
		Integer pageSize = query.getPageSize()==null?PageSize.SIZE15.getSize():query.getPageSize();
		SimplePage page = new SimplePage(query.getPageNo(), count, pageSize);
		query.setSimplePage(page);
		List<UserVideoSeries> list = this.findListByParam(query);
		PaginationResultVO<UserVideoSeries> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
		return result;
	}

	/**
	 * 新增
	 */
	public Integer add(UserVideoSeries bean) {
		return this.userVideoSeriesMappers.insert(bean);
	}

	/**
	 * 批量新增
	 */
	public Integer addBatch(List<UserVideoSeries> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.userVideoSeriesMappers.insertBatch(listBean);
	}

	/**
	 * 批量新增或更新
	 */
	public Integer addOrUpdateBatch(List<UserVideoSeries> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.userVideoSeriesMappers.insertOrUpdateBatch(listBean);
	}


	/**
	 * 根据SeriesId查询
	 */
	public UserVideoSeries getBySeriesId(Integer seriesId) {
		return this.userVideoSeriesMappers.selectBySeriesId(seriesId);
	}

	/**
	 * 根据SeriesId更新
	 */
	public Integer updateBySeriesId(UserVideoSeries bean, Integer seriesId) {
		return this.userVideoSeriesMappers.updateBySeriesId(bean, seriesId);
	}

	/**
	 * 根据SeriesId删除
	 */
	public Integer deleteBySeriesId(Integer seriesId) {
		return this.userVideoSeriesMappers.deleteBySeriesId(seriesId);
	}

	/**
	 * @param userId
	 * @return
	 */
	@Override
	public List<UserVideoSeries> getUserAllSeries(String userId) {
		return this.userVideoSeriesMappers.selectUserAllSeries(userId);
	}

	/**
	 * @param bean
	 * @param videoIds
	 */
	@Transactional(rollbackFor = Exception.class)
	@Override
	public void saveUserAllSeries(UserVideoSeries bean, String videoIds) throws BusinessException {
		if (bean.getSeriesId() == null && StringTools.isEmpty(videoIds)) {
			throw new BusinessException(ResponseCodeEnum.CODE_600);
		}
		if (bean.getSeriesId() == null) {
			checkVideoIds(bean.getUserId(), videoIds);
			bean.setUpdateTime(new Date());
			bean.setSort(this.userVideoSeriesMappers.selectMaxSort(bean.getUserId()));
			this.userVideoSeriesMappers.insert(bean);
			this.saveSeriesVideo(bean.getUserId(), bean.getSeriesId(), videoIds);
		} else {
			UserVideoSeriesQuery seriesVideoQuery = new UserVideoSeriesQuery();
			seriesVideoQuery.setUserId(bean.getUserId());
			seriesVideoQuery.setSeriesId(bean.getSeriesId());

			this.userVideoSeriesMappers.updateByParam(bean, seriesVideoQuery);
		}
	}

	private void checkVideoIds(String userId, String videoIds) throws BusinessException {
		String[] videoIdArray = videoIds.split(",");
		VideoInfoQuery videoInfoQuery = new VideoInfoQuery();
		videoInfoQuery.setVideoIdArray(videoIdArray);
		videoInfoQuery.setUserId(userId);
		Integer count = videoInfoMappers.selectCount(videoInfoQuery);
		if (videoIdArray.length != count) {
			throw new BusinessException(ResponseCodeEnum.CODE_600);
		}
	}

	/**
	 * @param userId
	 * @param seriesId
	 * @param videoIds
	 */
	@Override
	public void saveSeriesVideo(String userId, Integer seriesId, String videoIds) throws BusinessException {
		UserVideoSeries userVideoSeries = getBySeriesId(seriesId);
		if (!userVideoSeries.getUserId().equals(userId)) {
			throw new BusinessException(ResponseCodeEnum.CODE_600);
		}
		this.checkVideoIds(userId, videoIds);
		String[] videoIdArray = videoIds.split(",");
		Integer sort = this.userVideoSeriesVideoMappers.selectMaxSort(seriesId);

		List<UserVideoSeriesVideo> seriesVideoList = new ArrayList<>();
		for (String videoId : videoIdArray) {
			UserVideoSeriesVideo userVideoSeriesVideo = new UserVideoSeriesVideo();
			userVideoSeriesVideo.setVideoId(videoId);
			userVideoSeriesVideo.setSort(++sort);
			userVideoSeriesVideo.setSeriesId(seriesId);
			userVideoSeriesVideo.setUserId(userId);
			seriesVideoList.add(userVideoSeriesVideo);
		}
		this.userVideoSeriesVideoMappers.insertOrUpdateBatch(seriesVideoList);
	}

	/**
	 * @param userId
	 * @param seriesId
	 * @param videoId
	 */
	@Override
	public void delSeriesVideo(String userId, Integer seriesId, String videoId) {
		UserVideoSeriesVideoQuery videoSeriesVideoQuery = new UserVideoSeriesVideoQuery();
		videoSeriesVideoQuery.setUserId(userId);
		videoSeriesVideoQuery.setSeriesId(seriesId);
		videoSeriesVideoQuery.setVideoId(videoId);
		this.userVideoSeriesVideoMappers.deleteByParam(videoSeriesVideoQuery);
	}

	/**
	 * @param userId
	 * @param seriesId
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delVideoSeries(String userId, Integer seriesId) throws BusinessException {
		UserVideoSeriesQuery seriesQuery = new UserVideoSeriesQuery();
		seriesQuery.setUserId(userId);
		seriesQuery.setSeriesId(seriesId);
		Integer count = userVideoSeriesMappers.deleteByParam(seriesQuery);
		if (count == 0) {
			throw new BusinessException(ResponseCodeEnum.CODE_600);
		}
		UserVideoSeriesVideoQuery seriesVideoQuery = new UserVideoSeriesVideoQuery();
		seriesVideoQuery.setUserId(userId);
		seriesVideoQuery.setSeriesId(seriesId);
		userVideoSeriesVideoMappers.deleteByParam(seriesVideoQuery);
	}

	/**
	 * @param userId
	 * @param seriesIds
	 */
	@Override
	public void changeVideoSeriesSort(String userId, String seriesIds) {
		String[] seriesIdArray = seriesIds.split(",");
		List<UserVideoSeries> videoSeriesList = new ArrayList<>();
		Integer sort = 0;
		for (String seriesId : seriesIdArray) {
			UserVideoSeries videoSeries = new UserVideoSeries();
			videoSeries.setUserId(userId);
			videoSeries.setSeriesId(Integer.parseInt(seriesId));
			videoSeries.setSort(sort++);
			videoSeriesList.add(videoSeries);
		}
		userVideoSeriesMappers.changeSort(videoSeriesList);
	}

	/**
	 * @param seriesQuery
	 * @return
	 */
	@Override
	public List<UserVideoSeries> findListWithVideoList(UserVideoSeriesQuery seriesQuery) {
		return userVideoSeriesMappers.selectListWithVideoList(seriesQuery);
	}
}