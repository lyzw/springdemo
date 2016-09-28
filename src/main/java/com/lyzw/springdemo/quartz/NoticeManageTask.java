package com.lyzw.springdemo.quartz;

import com.alibaba.fastjson.JSON;
import mobi.birdgame.mgmt.constants.MgmtConstants;
import mobi.birdgame.mgmt.persistent.domain.Notice;
import mobi.birdgame.mgmt.persistent.mapper.NoticeMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.*;

/**
 * 通知任务管理
 * Created by zhouwei on 201/9/13.
 */
public class NoticeManageTask {

    Logger logger = LogManager.getLogger(NoticeManageTask.class);

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    NoticeMapper noticeMapper;

    @Autowired
    @Qualifier("scheduler")
    Scheduler scheduler;

    public void execute() throws JobExecutionException {
        System.out.println("==================begin to execute");
        logger.error("==================begin to execute ");
        List<Notice> list = noticeMapper.selectBySelective(null);
        Map<String,Notice> tasks = transTasks(list);
        logger.debug("the notice map is :[{}]", JSON.toJSONString(tasks));
        Set<String> names = tasks.keySet();
        List<String> existsNames = new ArrayList<String>();
        try {
            Set<JobKey> jobKeys = scheduler.getJobKeys(GroupMatcher.jobGroupEquals(MgmtConstants.QUARTZ_NOTICE_GROUP));
            List<String> jobGroupNames  = scheduler.getJobGroupNames();
            List<JobExecutionContext> executingJobs = scheduler.getCurrentlyExecutingJobs();
            logger.debug("jobGroupNames :[{}]", JSON.toJSONString(jobGroupNames));
            logger.debug("current executingJobs size :[{}]", executingJobs.size());

            for(JobKey jobKey : jobKeys){
                String name = jobKey.getName();
                logger.debug("处理正在运行的定时任务【{}】",name);
                //删除多余的job
                if (name.startsWith(MgmtConstants.QUARTZ_NOTICE_PREFIX) && !names.contains(name)){
                    logger.debug("删除已经失效的定时任务【{}】",name);
                    scheduler.deleteJob(jobKey);
                }else if(name.startsWith(MgmtConstants.QUARTZ_NOTICE_PREFIX) && names.contains(name)){//更新或者发布key
                    logger.debug("定时任务【{}】有效且是通知任务，等待刷新",name);
                    existsNames.add(name);
                }
            }
            List<String> allNameList = new ArrayList<String>();
            allNameList.addAll(names);
            allNameList.removeAll(existsNames);
            for (String item : allNameList){
                logger.debug("broad new task :[{}]", item);
                Notice notice = tasks.get(item);
                QuartzUtil.broadNewTask(scheduler,item,notice);
            }
            for (String item : existsNames){
                logger.debug("refresh exists task :[{}]", item);
                Notice notice = tasks.get(item);
                logger.debug("tasks :[{}]", JSON.toJSONString(tasks));
                logger.debug("tasks keys :[{}]", JSON.toJSONString(tasks.keySet()));
                logger.debug("refresh notice :[{}]", JSON.toJSONString(notice));

                QuartzUtil.refreshExistsTask(scheduler,item,notice);
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    private Map<String, Notice> transTasks(List<Notice> list) {
        Map data = new HashMap();

        for (Notice notice : list){
            data.put(MgmtConstants.QUARTZ_NOTICE_PREFIX + notice.getId(),notice);
        }
        return data;
    }

    private List<String> getJobName(List<Notice> list) {
        return null;
    }
}
