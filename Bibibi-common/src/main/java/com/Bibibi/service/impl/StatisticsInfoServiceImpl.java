package com.Bibibi.service.impl;

import com.Bibibi.component.RedisComponent;
import com.Bibibi.entity.constants.Constants;
import com.Bibibi.entity.po.StatisticsInfo;
import com.Bibibi.entity.po.VideoInfo;
import com.Bibibi.entity.query.SimplePage;
import com.Bibibi.entity.query.StatisticsInfoQuery;
import com.Bibibi.entity.query.UserInfoQuery;
import com.Bibibi.entity.query.VideoInfoQuery;
import com.Bibibi.entity.vo.PaginationResultVO;
import com.Bibibi.enums.PageSize;
import com.Bibibi.enums.StatisticsTypeEnum;
import com.Bibibi.enums.UserActionTypeEnum;
import com.Bibibi.mappers.StatisticsInfoMappers;
import com.Bibibi.mappers.UserFocusMappers;
import com.Bibibi.mappers.UserInfoMappers;
import com.Bibibi.mappers.VideoInfoMappers;
import com.Bibibi.service.StatisticsInfoService;
import com.Bibibi.utils.DateUtils;
import com.Bibibi.utils.StringTools;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description:数据统计Service
 * @date:2024-12-04
 * @author: liujun
 */
@Service("StatisticsInfoService")
public class StatisticsInfoServiceImpl implements StatisticsInfoService {

	@Resource
	private StatisticsInfoMappers<StatisticsInfo, StatisticsInfoQuery> statisticsInfoMappers;

	@Resource
	private RedisComponent redisComponent;

	@Resource
	private VideoInfoMappers videoInfoMappers;

	@Resource
	private UserFocusMappers userFocusMappers;

	@Resource
	private UserInfoMappers userInfoMappers;

	/**
	 * 根据条件查询列表
	 */
	public List<StatisticsInfo> findListByParam(StatisticsInfoQuery query) {
		return this.statisticsInfoMappers.selectList(query);
	}

	/**
	 * 根据条件查询数量
	 */
	public Integer findCountByParam(StatisticsInfoQuery query) {
		return this.statisticsInfoMappers.selectCount(query);
	}

	/**
	 * 分页查询
	 */
	public PaginationResultVO<StatisticsInfo> findByPage(StatisticsInfoQuery query) {
		Integer count = this.findCountByParam(query);
		Integer pageSize = query.getPageSize()==null?PageSize.SIZE15.getSize():query.getPageSize();
		SimplePage page = new SimplePage(query.getPageNo(), count, pageSize);
		query.setSimplePage(page);
		List<StatisticsInfo> list = this.findListByParam(query);
		PaginationResultVO<StatisticsInfo> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
		return result;
	}

	/**
	 * 新增
	 */
	public Integer add(StatisticsInfo bean) {
		return this.statisticsInfoMappers.insert(bean);
	}

	/**
	 * 批量新增
	 */
	public Integer addBatch(List<StatisticsInfo> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.statisticsInfoMappers.insertBatch(listBean);
	}

	/**
	 * 批量新增或更新
	 */
	public Integer addOrUpdateBatch(List<StatisticsInfo> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.statisticsInfoMappers.insertOrUpdateBatch(listBean);
	}


	/**
	 * 根据StatisticsDateAndUserIdAndDataType查询
	 */
	public StatisticsInfo getByStatisticsDateAndUserIdAndDataType(String statisticsDate, String userId, Integer dataType) {
		return this.statisticsInfoMappers.selectByStatisticsDateAndUserIdAndDataType(statisticsDate, userId, dataType);
	}

	/**
	 * 根据StatisticsDateAndUserIdAndDataType更新
	 */
	public Integer updateByStatisticsDateAndUserIdAndDataType(StatisticsInfo bean, String statisticsDate, String userId, Integer dataType) {
		return this.statisticsInfoMappers.updateByStatisticsDateAndUserIdAndDataType(bean, statisticsDate, userId, dataType);
	}

	/**
	 * 根据StatisticsDateAndUserIdAndDataType删除
	 */
	public Integer deleteByStatisticsDateAndUserIdAndDataType(String statisticsDate, String userId, Integer dataType) {
		return this.statisticsInfoMappers.deleteByStatisticsDateAndUserIdAndDataType(statisticsDate, userId, dataType);
	}

	/**
	 *
	 */
	@Override
	public void statisticsData() {
		List<StatisticsInfo> statisticsInfoList = new ArrayList<>();

		final String beforeDayDate = DateUtils.getBeforeDayDate(Constants.ONE);

		//统计播放量
		Map<String, Integer> videoPlayCountMap = redisComponent.getVideoPlayCount(beforeDayDate);

		List<String> playVideoKeys = new ArrayList<>(videoPlayCountMap.keySet());
		playVideoKeys = playVideoKeys.stream().map(item -> item.substring(item.lastIndexOf(":") + 1)).collect(Collectors.toList());

		VideoInfoQuery videoInfoQuery = new VideoInfoQuery();
		videoInfoQuery.setVideoIdArray(playVideoKeys.toArray(new String[playVideoKeys.size()]));
		List<VideoInfo> videoInfoList = videoInfoMappers.selectList(videoInfoQuery);

		videoInfoList.stream().collect(Collectors.groupingBy(VideoInfo::getUserId, Collectors.summingInt(item -> videoPlayCountMap.get(Constants.REDIS_KEY_VIDEO_PLAY_COUNT + beforeDayDate + ":" + item.getVideoId()))));

		videoPlayCountMap.forEach((k, v) -> {
			StatisticsInfo statisticsInfo = new StatisticsInfo();
			statisticsInfo.setStatisticsDate(beforeDayDate);
			statisticsInfo.setUserId(k);
			statisticsInfo.setDataType(StatisticsTypeEnum.PLAY.getType());
			statisticsInfo.setStatisticsCount(v);
			statisticsInfoList.add(statisticsInfo);
		});

		//统计粉丝数
		List<StatisticsInfo> fansDateList = this.statisticsInfoMappers.selectStatisticsFans(beforeDayDate);
		for (StatisticsInfo statisticsInfo : fansDateList) {
			statisticsInfo.setStatisticsDate(beforeDayDate);
			statisticsInfo.setDataType(StatisticsTypeEnum.FANS.getType());
		}
		statisticsInfoList.addAll(fansDateList);

		//统计评论
		List<StatisticsInfo> commentDataList = this.statisticsInfoMappers.selectStatisticsComment(beforeDayDate);
		for (StatisticsInfo statisticsInfo : commentDataList) {
			statisticsInfo.setStatisticsDate(beforeDayDate);
			statisticsInfo.setDataType(StatisticsTypeEnum.COMMENT.getType());
		}
		statisticsInfoList.addAll(commentDataList);

		//统计 弹幕、点赞、收藏、投币
		List<StatisticsInfo> statisticsInfoOthers = this.statisticsInfoMappers.selectStatisticsInfo(beforeDayDate,
				new Integer[]{UserActionTypeEnum.VIDEO_LIKE.getType(), UserActionTypeEnum.VIDEO_COIN.getType(), UserActionTypeEnum.VIDEO_COLLECT.getType()});

		for (StatisticsInfo statisticsInfo : statisticsInfoOthers) {
			statisticsInfo.setStatisticsDate(beforeDayDate);
			if (UserActionTypeEnum.VIDEO_LIKE.getType().equals(statisticsInfo.getDataType())) {
				statisticsInfo.setDataType(StatisticsTypeEnum.LIKE.getType());
			} else if (UserActionTypeEnum.VIDEO_COLLECT.getType().equals(statisticsInfo.getDataType())) {
				statisticsInfo.setDataType(StatisticsTypeEnum.COLLECTION.getType());
			} else if (UserActionTypeEnum.VIDEO_COIN.getType().equals(statisticsInfo.getDataType())) {
				statisticsInfo.setDataType(StatisticsTypeEnum.COIN.getType());
			}
		}
		statisticsInfoList.addAll(statisticsInfoOthers);
		this.statisticsInfoMappers.insertBatch(statisticsInfoList);
	}

	@Override
	public Map<String, Integer> getStatisticsInfoActualTime(String userId) {
		Map<String, Integer> result = statisticsInfoMappers.selectTotalCountInfo(userId);
		if (!StringTools.isEmpty(userId)) {
			//查询粉丝数
			result.put("userCount", userFocusMappers.selectFansCount(userId));
		} else {
			result.put("userCount", userInfoMappers.selectCount(new UserInfoQuery()));
		}
		return result;
	}

	@Override
	public List<StatisticsInfo> findListTotalInfoByParam(StatisticsInfoQuery param) {
		return statisticsInfoMappers.selectListTotalInfoByParam(param);
	}

	@Override
	public List<StatisticsInfo> findUserCountTotalInfoByParam(StatisticsInfoQuery param) {
		return statisticsInfoMappers.selectUserCountTotalInfoByParam(param);
	}
}