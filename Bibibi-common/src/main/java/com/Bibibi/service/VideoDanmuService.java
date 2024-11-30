package com.Bibibi.service;

import com.Bibibi.entity.po.VideoDanmu;
import com.Bibibi.entity.query.VideoDanmuQuery;
import com.Bibibi.entity.vo.PaginationResultVO;
import com.Bibibi.exception.BusinessException;

import java.util.List;

/**
 * @Description:视频弹幕Service
 * @date:2024-11-20
 * @author: liujun
 */
public interface VideoDanmuService {

    /**
     * 根据条件查询列表
     */
    List<VideoDanmu> findListByParam(VideoDanmuQuery query);

    /**
     * 根据条件查询数量
     */
    Integer findCountByParam(VideoDanmuQuery query);

    /**
     * 分页查询
     */
    PaginationResultVO<VideoDanmu> findByPage(VideoDanmuQuery query);

    /**
     * 新增
     */
    Integer add(VideoDanmu bean);

    /**
     * 批量新增
     */
    Integer addBatch(List<VideoDanmu> listBean);

    /**
     * 批量新增或更新
     */
    Integer addOrUpdateBatch(List<VideoDanmu> listBean);


    /**
     * 根据DanmuId查询
     */
    VideoDanmu getByDanmuId(Integer danmuId);

    /**
     * 根据DanmuId更新
     */
    Integer updateByDanmuId(VideoDanmu bean, Integer danmuId);

    /**
     * 根据DanmuId删除
     */
    Integer deleteByDanmuId(Integer danmuId);

    void saveVideoDanmu(VideoDanmu videoDanmu) throws BusinessException;

    void deleteDanmu(Integer danmuId, String userId) throws BusinessException;
}