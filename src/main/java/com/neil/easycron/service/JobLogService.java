package com.neil.easycron.service;
import java.util.Calendar;

import com.neil.easycron.bo.PageResult;
import com.neil.easycron.bo.job.JobLogBasicBo;
import com.neil.easycron.bo.job.JobLogBo;
import com.neil.easycron.bo.job.JobLogRequest;
import com.neil.easycron.plugin.bo.JobRunningResult;

public interface JobLogService {
    PageResult<JobLogBasicBo> getJobLogs(JobLogRequest request);

    void writeJobLog(Integer jobId, Calendar start, Calendar end, JobRunningResult runningResult, Integer userId);

    JobLogBo getJobLogDetail(Long logId);

    void cleanLog(Integer jobId);
}
