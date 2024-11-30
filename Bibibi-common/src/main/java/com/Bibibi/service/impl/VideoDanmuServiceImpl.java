package com.Bibibi.service.impl;

import com.Bibibi.entity.constants.Constants;
import com.Bibibi.entity.po.VideoDanmu;
import com.Bibibi.entity.po.VideoInfo;
import com.Bibibi.entity.query.SimplePage;
import com.Bibibi.entity.query.VideoDanmuQuery;
import com.Bibibi.entity.query.VideoInfoQuery;
import com.Bibibi.entity.vo.PaginationResultVO;
import com.Bibibi.enums.PageSize;
import com.Bibibi.enums.ResponseCodeEnum;
import com.Bibibi.enums.UserActionTypeEnum;
import com.Bibibi.exception.BusinessException;
import com.Bibibi.mappers.VideoDanmuMappers;
import com.Bibibi.mappers.VideoInfoMappers;
import com.Bibibi.service.VideoDanmuService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description:视频弹幕Service
 * @date:2024-11-20
 * @author: liujun
 */
@Service("VideoDanmuService")
public class VideoDanmuServiceImpl implements VideoDanmuService {

    @Resource
    private VideoInfoMappers<VideoInfo, VideoInfoQuery> videoInfoMapper;

    @Resource
    private VideoDanmuMappers<VideoDanmu, VideoDanmuQuery> videoDanmuMappers;

    /**
     * 根据条件查询列表
     */
    public List<VideoDanmu> findListByParam(VideoDanmuQuery query) {
        return this.videoDanmuMappers.selectList(query);
    }

    /**
     * 根据条件查询数量
     */
    public Integer findCountByParam(VideoDanmuQuery query) {
        return this.videoDanmuMappers.selectCount(query);
    }

    /**
     * 分页查询
     */
    public PaginationResultVO<VideoDanmu> findByPage(VideoDanmuQuery query) {
        Integer count = this.findCountByParam(query);
        Integer pageSize = query.getPageSize() == null ? PageSize.SIZE15.getSize() : query.getPageSize();
        SimplePage page = new SimplePage(query.getPageNo(), count, pageSize);
        query.setSimplePage(page);
        List<VideoDanmu> list = this.findListByParam(query);
        PaginationResultVO<VideoDanmu> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
        return result;
    }

    /**
     * 新增
     */
    public Integer add(VideoDanmu bean) {
        return this.videoDanmuMappers.insert(bean);
    }

    /**
     * 批量新增
     */
    public Integer addBatch(List<VideoDanmu> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return this.videoDanmuMappers.insertBatch(listBean);
    }

    /**
     * 批量新增或更新
     */
    public Integer addOrUpdateBatch(List<VideoDanmu> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return this.videoDanmuMappers.insertOrUpdateBatch(listBean);
    }


    /**
     * 根据DanmuId查询
     */
    public VideoDanmu getByDanmuId(Integer danmuId) {
        return this.videoDanmuMappers.selectByDanmuId(danmuId);
    }

    /**
     * 根据DanmuId更新
     */
    public Integer updateByDanmuId(VideoDanmu bean, Integer danmuId) {
        return this.videoDanmuMappers.updateByDanmuId(bean, danmuId);
    }

    /**
     * 根据DanmuId删除
     */
    public Integer deleteByDanmuId(Integer danmuId) {
        return this.videoDanmuMappers.deleteByDanmuId(danmuId);
    }

    @Override
    public void saveVideoDanmu(VideoDanmu bean) throws BusinessException {
        VideoInfo videoInfo = videoInfoMapper.selectByVideoId(bean.getVideoId());
        if (videoInfo == null) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        //是否关闭弹幕
        if (videoInfo.getInteraction() != null && videoInfo.getInteraction().contains(Constants.ONE.toString())) {
            throw new BusinessException("UP主已关闭弹幕");
        }
        this.videoDanmuMappers.insert(bean);
        this.videoInfoMapper.updateCountInfo(bean.getVideoId(), UserActionTypeEnum.VIDEO_DANMU.getField(), 1);

        //TODO 更新es 弹幕数量

    }


    /**
     * @param danmuId
     * @param userId
     */
    @Override
    public void deleteDanmu(Integer danmuId, String userId) throws BusinessException {
        VideoDanmu videoDanmu = videoDanmuMappers.selectByDanmuId(danmuId);
        if (videoDanmu == null) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        VideoInfo videoInfo = videoInfoMapper.selectByVideoId(videoDanmu.getVideoId());
        if (videoInfo == null) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        videoDanmuMappers.deleteByDanmuId(danmuId);
    }
}