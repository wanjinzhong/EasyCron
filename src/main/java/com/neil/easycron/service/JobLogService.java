package com.neil.easycron.service;
import com.neil.easycron.bo.PageResult;
import com.neil.easycron.bo.job.JobLogBo;
import com.neil.easycron.bo.job.JobLogRequest;

public interface JobLogService {
    PageResult<JobLogBo> getJobLogs(JobLogRequest request);
}
