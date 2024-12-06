package com.Bibibi.service.impl;

import com.Bibibi.entity.po.VideoPlayHistory;
import com.Bibibi.entity.query.SimplePage;
import com.Bibibi.entity.query.VideoPlayHistoryQuery;
import com.Bibibi.entity.vo.PaginationResultVO;
import com.Bibibi.enums.PageSize;
import com.Bibibi.mappers.VideoPlayHistoryMappers;
import com.Bibibi.service.VideoPlayHistoryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @Description:视频播放历史Service
 * @date:2024-12-04
 * @author: liujun
 */
@Service("VideoPlayHistoryService")
public class VideoPlayHistoryServiceImpl implements VideoPlayHistoryService {

    @Resource
    private VideoPlayHistoryMappers<VideoPlayHistory, VideoPlayHistoryQuery> videoPlayHistoryMappers;

    /**
     * 根据条件查询列表
     */
    public List<VideoPlayHistory> findListByParam(VideoPlayHistoryQuery query) {
        return this.videoPlayHistoryMappers.selectList(query);
    }

    /**
     * 根据条件查询数量
     */
    public Integer findCountByParam(VideoPlayHistoryQuery query) {
        return this.videoPlayHistoryMappers.selectCount(query);
    }

    /**
     * 分页查询
     */
    public PaginationResultVO<VideoPlayHistory> findByPage(VideoPlayHistoryQuery query) {
        Integer count = this.findCountByParam(query);
        Integer pageSize = query.getPageSize() == null ? PageSize.SIZE15.getSize() : query.getPageSize();
        SimplePage page = new SimplePage(query.getPageNo(), count, pageSize);
        query.setSimplePage(page);
        List<VideoPlayHistory> list = this.findListByParam(query);
        PaginationResultVO<VideoPlayHistory> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
        return result;
    }

    /**
     * 新增
     */
    public Integer add(VideoPlayHistory bean) {
        return this.videoPlayHistoryMappers.insert(bean);
    }

    /**
     * 批量新增
     */
    public Integer addBatch(List<VideoPlayHistory> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return this.videoPlayHistoryMappers.insertBatch(listBean);
    }

    /**
     * 批量新增或更新
     */
    public Integer addOrUpdateBatch(List<VideoPlayHistory> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return this.videoPlayHistoryMappers.insertOrUpdateBatch(listBean);
    }


    /**
     * 根据UserIdAndVideoId查询
     */
    public VideoPlayHistory getByUserIdAndVideoId(String userId, String videoId) {
        return this.videoPlayHistoryMappers.selectByUserIdAndVideoId(userId, videoId);
    }

    /**
     * 根据UserIdAndVideoId更新
     */
    public Integer updateByUserIdAndVideoId(VideoPlayHistory bean, String userId, String videoId) {
        return this.videoPlayHistoryMappers.updateByUserIdAndVideoId(bean, userId, videoId);
    }

    /**
     * 根据UserIdAndVideoId删除
     */
    public Integer deleteByUserIdAndVideoId(String userId, String videoId) {
        return this.videoPlayHistoryMappers.deleteByUserIdAndVideoId(userId, videoId);
    }

    /**
     * @param historyQuery
     */
    @Override
    public void deleteByParam(VideoPlayHistoryQuery historyQuery) {
        this.videoPlayHistoryMappers.deleteByParam(historyQuery);
    }

    /**
     * @param userId    用户id
     * @param videoId   视频id
     * @param fileIndex 第几分p
     */
    @Override
    public void saveHistory(String userId, String videoId, Integer fileIndex) {
		VideoPlayHistory videoPlayHistory = new VideoPlayHistory();
		videoPlayHistory.setVideoId(videoId);
		videoPlayHistory.setFileIndex(fileIndex);
		videoPlayHistory.setUserId(userId);
		videoPlayHistory.setLastUpdateTime(new Date());
		this.videoPlayHistoryMappers.insertOrUpdate(videoPlayHistory);
	}
}