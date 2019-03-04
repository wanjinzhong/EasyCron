package com.neil.easycron.api;

import java.util.Map;

import com.neil.easycron.bo.NewJobBo;
import com.neil.easycron.bo.PageResult;
import com.neil.easycron.bo.config.ConfigFileBo;
import com.neil.easycron.bo.job.JobBo;
import com.neil.easycron.bo.job.JobLogBo;
import com.neil.easycron.bo.job.JobLogRequest;
import com.neil.easycron.bo.job.JobSearchRequest;
import com.neil.easycron.bo.response.JsonEntity;
import com.neil.easycron.service.JobLogService;
import com.neil.easycron.service.JobService;
import com.neil.easycron.utils.ResponseHelper;
import io.swagger.annotations.Api;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("public/api")
@Api
@RequiresAuthentication
public class JobApi {

    @Autowired
    private JobService jobService;

    @Autowired
    private JobLogService jobLogService;

    @PostMapping("job")
    @RequiresRoles("CRON_EDITOR")
    public JsonEntity createNewJob(@RequestBody NewJobBo newJobBo) {
        jobService.createNewJob(newJobBo);
        return ResponseHelper.ofNothing();
    }

    @GetMapping("configs/{jobId}")
    @RequiresRoles("CRON_EDITOR")
    public JsonEntity<ConfigFileBo> getConfigs(@PathVariable("jobId") Integer jobId) {
        return ResponseHelper.createInstance(jobService.getConfigs(jobId));
    }

    @PutMapping("configs/{jobId}")
    @RequiresRoles("CRON_EDITOR")
    public JsonEntity saveConfigs(@PathVariable("jobId") Integer jobId, @RequestBody Map<String, Object> configData) {
        jobService.saveConfigs(jobId, configData);
        return ResponseHelper.ofNothing();
    }

    @PostMapping("jobs")
    public JsonEntity<PageResult<JobBo>> getJobList(@RequestBody JobSearchRequest request) {
        return ResponseHelper.createInstance(jobService.searchJobs(request));
    }

    @PostMapping("logs")
    public JsonEntity<PageResult<JobLogBo>> getJobLogs(@RequestBody JobLogRequest request) {
        return ResponseHelper.createInstance(jobLogService.getJobLogs(request));
    }
}
