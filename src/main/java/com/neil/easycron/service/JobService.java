package com.neil.easycron.service;
import java.util.List;

import com.neil.easycron.bo.NewJobBo;
import com.neil.easycron.bo.PageResult;
import com.neil.easycron.bo.config.ConfigBo;
import com.neil.easycron.bo.config.ConfigGroupBo;
import com.neil.easycron.bo.job.JobBo;
import com.neil.easycron.bo.job.JobSearchRequest;

public interface JobService {
    void createNewJob(NewJobBo newJobBo);

    List<ConfigGroupBo> getConfigs(Integer jobId);

    void saveConfigs(Integer jobId, ConfigBo configData);

    PageResult<JobBo> searchJobs(JobSearchRequest request);

    void deleteJob(Integer jobId);
}
