package com.Bibibi.web.task;

import com.Bibibi.component.RedisComponent;
import com.Bibibi.entity.constants.Constants;
import com.Bibibi.entity.po.VideoInfoFilePost;
import com.Bibibi.redis.RedisUtils;
import com.Bibibi.service.VideoInfoPostService;
import lombok.extern.slf4j.Slf4j;
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
    private VideoInfoPostService videoInfoPostService;

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
}
