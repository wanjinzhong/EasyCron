package com.github.wanjinzhong.easycron.service;
import java.util.Calendar;

import com.github.wanjinzhong.easycron.bo.PageResult;
import com.github.wanjinzhong.easycron.bo.job.JobLogBasicBo;
import com.github.wanjinzhong.easycron.bo.job.JobLogBo;
import com.github.wanjinzhong.easycron.bo.job.JobLogRequest;
import com.github.wanjinzhong.easycronbase.bo.JobRunningResult;

public interface JobLogService {
    PageResult<JobLogBasicBo> getJobLogs(JobLogRequest request);

    void writeJobLog(Integer jobId, Calendar start, Calendar end, JobRunningResult runningResult, Integer userId);

    JobLogBo getJobLogDetail(Long logId);

    void cleanLog(Integer jobId);
}
