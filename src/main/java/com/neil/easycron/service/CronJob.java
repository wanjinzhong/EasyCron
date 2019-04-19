package com.neil.easycron.service;
import java.util.Calendar;
import java.util.Map;

import com.neil.easycron.constant.Constant;
import com.neil.easycron.dao.repository.JobRepository;
import com.neil.easycron.plugin.bo.JobRunningResult;
import com.neil.easycron.plugin.service.EasyJobService;
import com.neil.easycron.utils.JobPluginUtil;
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
        com.neil.easycron.dao.entity.Job job = (com.neil.easycron.dao.entity.Job) dataMap.get(Constant.JobParam.JOB);
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
