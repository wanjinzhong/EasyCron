package com.neil.easycron.service;
import java.util.List;
import java.util.Map;

import com.neil.easycron.bo.job.NewJobBo;
import com.neil.easycron.bo.PageResult;
import com.neil.easycron.bo.config.ConfigBo;
import com.neil.easycron.bo.config.ConfigGroupBo;
import com.neil.easycron.bo.job.JobBo;
import com.neil.easycron.bo.job.JobSearchRequest;

public interface JobService {
    void createNewJob(NewJobBo newJobBo);

    Map<String, Object> getConfigMap(Integer jobId);

    List<ConfigGroupBo> getConfigs(Integer jobId);

    void saveConfigs(Integer jobId, ConfigBo configData);

    void restartJob(Integer jobId);

    PageResult<JobBo> searchJobs(JobSearchRequest request);

    void deleteJob(Integer jobId);

    void runJob(Integer jobId);

    void stopJob(Integer jobId);
}
