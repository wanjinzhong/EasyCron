package com.neil.easycron.service;
import java.util.Map;

import com.neil.easycron.bo.NewJobBo;
import com.neil.easycron.bo.config.ConfigFileBo;

public interface JobService {
    void createNewJob(NewJobBo newJobBo);

    ConfigFileBo getConfigs(Integer jobId);

    void saveConfigs(Integer jobId, Map<String, Object> configData);
}
