package com.github.wanjinzhong.easycron.service;
import java.util.Calendar;
import java.util.Map;

import com.github.wanjinzhong.easycron.dao.repository.JobRepository;
import com.github.wanjinzhong.easycron.constant.Constant;
import com.github.wanjinzhong.easycron.utils.JobPluginUtil;
import com.github.wanjinzhong.easycronbase.bo.JobRunningResult;
import com.github.wanjinzhong.easycronbase.service.EasyJobService;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class CronJob implements Job {

    @Autowired
    private JobLogService jobLogService;

    @Autowired
    private JobService jobService;

    @Autowired
    private JobRepository jobRepository;

    private Logger logger = LoggerFactory.getLogger(CronJob.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap dataMap = context.getJobDetail().getJobDataMap();
        com.github.wanjinzhong.easycron.dao.entity.Job job = (com.github.wanjinzhong.easycron.dao.entity.Job) dataMap.get(Constant.JobParam.JOB);
        Integer userId = (Integer) dataMap.get(Constant.JobParam.ENTRY_USER);
        EasyJobService easyJobService = JobPluginUtil.getJobService(job.getPlugin());
        Map<String, Object> configs = jobService.getConfigMap(job.getId());
        Calendar start = Calendar.getInstance();
        JobRunningResult result = easyJobService.call(configs);
        logger.info(context.getJobDetail().getKey().toString() + " is running");
        jobLogService.writeJobLog(job.getId(), start, java.util.Calendar.getInstance(), result, userId);
        job.setRunningCount(job.getRunningCount() + 1);
        jobRepository.save(job);
    }
}
