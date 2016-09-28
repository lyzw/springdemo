package com.lyzw.springdemo.quartz;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.*;

/**
 * Created by zhouwei on 2016/9/13.
 */
public class QuartzUtil {

    private static Logger logger = LogManager.getLogger(QuartzUtil.class);

    /**
     *
     * 新增任务调度
     * @param scheduler 调度器
     * @param jobName   任务名称
     * @param object    通知信息
     * @param groupName 组名
     * @param interval  间隔时间
     * @param repeatCount   执行次数
     * @throws SchedulerException 调度异常
     */
    public static void broadNewTask(Scheduler scheduler, String jobName, Object object,
                                    String groupName,int interval,int repeatCount,Class clazz) throws SchedulerException {
        logger.debug("发布新的定时任务");
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(jobName, groupName);

            //获取trigger，即在spring配置文件中定义的 bean id="myTrigger"
            SimpleTrigger trigger = (SimpleTrigger) scheduler.getTrigger(triggerKey);
            if (trigger == null) {
                logger.debug("开始发布新的定时任务");

                JobDetail jobDetail = JobBuilder.newJob(clazz)
                        .withIdentity(jobName, groupName).build();
                jobDetail.getJobDataMap().put("data", object);
                SimpleScheduleBuilder scheduleBuilder = getScheduleBuilderByNotice(interval,repeatCount);

                trigger = TriggerBuilder.newTrigger().withIdentity(jobName, groupName).withSchedule(scheduleBuilder).build();
                scheduler.scheduleJob(jobDetail, trigger);
            } else {
                refreshExistsTask(scheduler, jobName, object,groupName,interval,repeatCount,clazz);
            }
            logger.debug("新的定时任务发布成功");

        } catch (SchedulerException e) {
            logger.error(e);
            throw e;
        }
    }


    /**
     * 刷新任务的信息
     * @param scheduler 调度器
     * @param jobName   任务名称
     * @param object    通知信息
     * @param groupName 组名
     * @param interval  间隔时间
     * @param repeatCount   执行次数
     * @param clazz 执行的类extends <see>org.quartz.Job</see>
     * @throws SchedulerException 调度异常
     */
    public static void refreshExistsTask(Scheduler scheduler, String jobName, Object object,String groupName,
                                         int interval,int repeatCount,Class clazz) throws SchedulerException {
        try {
            logger.debug("获取并刷新任务信息");

            //获取并刷新任务信息
            JobKey jobKey = JobKey.jobKey(jobName, groupName);

            //重新开始调度
            scheduler.deleteJob(jobKey);
            broadNewTask(scheduler, jobName, object,groupName,interval,repeatCount,clazz);
            logger.debug("获取并刷新任务信息成功");
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    /**
     * 获取定时器
     * @param interval
     * @param repeatCount
     * @return
     */
    private static SimpleScheduleBuilder getScheduleBuilderByNotice(int interval,int repeatCount) {
        SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(interval).withRepeatCount(repeatCount);
        return scheduleBuilder;
    }
}
