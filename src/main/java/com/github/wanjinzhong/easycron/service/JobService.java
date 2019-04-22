package com.github.wanjinzhong.easycron.service;
import java.util.List;
import java.util.Map;

import com.github.wanjinzhong.easycron.bo.job.NewJobBo;
import com.github.wanjinzhong.easycron.bo.PageResult;
import com.github.wanjinzhong.easycron.bo.config.ConfigBo;
import com.github.wanjinzhong.easycron.bo.config.ConfigGroupBo;
import com.github.wanjinzhong.easycron.bo.job.JobBo;
import com.github.wanjinzhong.easycron.bo.job.JobSearchRequest;

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
