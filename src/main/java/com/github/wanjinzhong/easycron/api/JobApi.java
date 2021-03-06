package com.github.wanjinzhong.easycron.api;

import com.github.wanjinzhong.easycron.bo.config.ConfigBo;
import com.github.wanjinzhong.easycron.bo.job.JobSearchRequest;
import com.github.wanjinzhong.easycron.bo.job.NewJobBo;
import com.github.wanjinzhong.easycron.bo.response.JsonEntity;
import com.github.wanjinzhong.easycron.bo.PageResult;
import com.github.wanjinzhong.easycron.bo.job.JobBo;
import com.github.wanjinzhong.easycron.service.JobService;
import com.github.wanjinzhong.easycron.utils.ResponseHelper;
import io.swagger.annotations.Api;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
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

    @PostMapping("job")
    @RequiresRoles("CRON_EDITOR")
    public JsonEntity createNewJob(@RequestBody NewJobBo newJobBo) {
        jobService.createNewJob(newJobBo);
        return ResponseHelper.ofNothing();
    }

    @DeleteMapping("job/{jobId}")
    @RequiresRoles("CRON_EDITOR")
    public JsonEntity deleteJob(@PathVariable("jobId") Integer JobId) {
        jobService.deleteJob(JobId);
        return ResponseHelper.ofNothing();
    }

    @GetMapping("configs/{jobId}")
    @RequiresRoles("CRON_EDITOR")
    public JsonEntity<ConfigBo> getConfigs(@PathVariable("jobId") Integer jobId) {
        return ResponseHelper.createInstance(new ConfigBo(jobService.getConfigs(jobId)));
    }

    @PutMapping("configs/{jobId}")
    @RequiresRoles("CRON_EDITOR")
    public JsonEntity saveConfigs(@PathVariable("jobId") Integer jobId, @RequestBody ConfigBo configData) {
        jobService.saveConfigs(jobId, configData);
        return ResponseHelper.ofNothing();
    }

    @PostMapping("jobs")
    public JsonEntity<PageResult<JobBo>> getJobList(@RequestBody JobSearchRequest request) {
        return ResponseHelper.createInstance(jobService.searchJobs(request));
    }


    @PostMapping("job/{jobId}/start")
    public JsonEntity startJob(@PathVariable("jobId") Integer jobId) {
        jobService.runJob(jobId);
        return ResponseHelper.ofNothing();
    }

    @PostMapping("job/{jobId}/stop")
    public JsonEntity stopJob(@PathVariable("jobId") Integer jobId) {
        jobService.stopJob(jobId);
        return ResponseHelper.ofNothing();
    }
}
