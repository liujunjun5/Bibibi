package com.Bibibi.web.task;

import com.Bibibi.component.EsSearchComponent;
import com.Bibibi.component.RedisComponent;
import com.Bibibi.entity.constants.Constants;
import com.Bibibi.entity.dto.VideoPlayInfoDto;
import com.Bibibi.entity.po.VideoInfoFilePost;
import com.Bibibi.enums.SearchOrderTypeEnum;
import com.Bibibi.redis.RedisUtils;
import com.Bibibi.service.VideoInfoPostService;
import com.Bibibi.service.VideoPlayHistoryService;
import com.Bibibi.utils.StringTools;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
@Slf4j
public class ExecuteQueueTask {
    private ExecutorService executorService = Executors.newFixedThreadPool(Constants.TWO);

    @Resource
    private RedisComponent redisComponent;

    @Resource
    private RedisUtils redisUtils;

    @Resource
    private VideoPlayHistoryService videoPlayHistoryService;

    @Resource
    private VideoInfoPostService videoInfoPostService;

    @Resource
    private EsSearchComponent esSearchComponent;

    @PostConstruct
    public void consumeTransferFileQueue() {
        executorService.execute(() -> {
            while (true) {
                try {
                    VideoInfoFilePost videoInfoFile = (VideoInfoFilePost) redisUtils.rpop(Constants.REDIS_KEY_QUEUE_TRANSFER);
                    if (videoInfoFile == null) {
                        Thread.sleep(1500);
                        continue;
                    }
                    videoInfoPostService.transferVideoFile(videoInfoFile);
                } catch (Exception e) {
                    log.error("获取转码文件队列失败", e);
                }
            }
        });
    }


    @PostConstruct
    public void consumeVideoPlayQueue() {
        executorService.execute(() -> {
            while (true) {
                try {
                    VideoPlayInfoDto videoPlayInfoDto = redisComponent.getVideoPlayFromVideoPlayQueue();
                    if (videoPlayInfoDto == null) {
                        Thread.sleep(1500);
                        continue;
                    }
                    // 更新播放数量
                    videoInfoPostService.addReadCount(videoPlayInfoDto.getVideoId());
                    if (!StringTools.isEmpty(videoPlayInfoDto.getUserId())) {
                        videoPlayHistoryService.saveHistory(videoPlayInfoDto.getUserId(), videoPlayInfoDto.getVideoId(), videoPlayInfoDto.getFileIndex());
                    }
                    //按天记录视频播放数
                    redisComponent.recordVideoPlayCount(videoPlayInfoDto.getVideoId());

                    //更新es播放数量
                    esSearchComponent.updateDocCount(videoPlayInfoDto.getVideoId(), SearchOrderTypeEnum.VIDEO_PLAY.getField(), 1);
                } catch (Exception e) {
                    log.error("获取视频播放文件队列消息失败", e);
                }
            }
        });
    }
}
