package com.lyzw.springdemo.quartz;

import com.alibaba.fastjson.JSON;
import mobi.birdgame.common.util.RedisUtil;
import mobi.birdgame.mgmt.constants.MgmtConstants;
import mobi.birdgame.mgmt.persistent.domain.Notice;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhouwei on 2016/9/13.
 */
@Component
public class NoticeTask implements Job {
    Logger logger = LogManager.getLogger(NoticeTask.class);

    @Autowired
    RedisTemplate redisTemplate;
    public void execute(JobExecutionContext context) throws JobExecutionException {

        JobDataMap dataMap = context.getMergedJobDataMap();
        Jedis jedis = RedisUtil.getJedis();
        Notice data = (Notice)dataMap.get("data");
        Map map = new HashMap();
        logger.info("{}通知写入redis" ,data.getTitle());
        map.put("title",data.getTitle());
        map.put("content",data.getContent());
        map.put("datetime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        jedis.lpush(MgmtConstants.REDIS_NOTICE_LIST_KEY, JSON.toJSONString(map));
        RedisUtil.returnResource(jedis);
    }
}
