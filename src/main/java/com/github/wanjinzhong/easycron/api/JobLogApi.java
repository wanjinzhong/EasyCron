package com.github.wanjinzhong.easycron.api;
import com.github.wanjinzhong.easycron.bo.job.JobLogBo;
import com.github.wanjinzhong.easycron.bo.job.JobLogRequest;
import com.github.wanjinzhong.easycron.bo.response.JsonEntity;
import com.github.wanjinzhong.easycron.bo.PageResult;
import com.github.wanjinzhong.easycron.bo.job.JobLogBasicBo;
import com.github.wanjinzhong.easycron.service.JobLogService;
import com.github.wanjinzhong.easycron.utils.ResponseHelper;
import io.swagger.annotations.Api;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("public/api")
@Api
@RequiresAuthentication
public class JobLogApi {

    @Autowired
    private JobLogService jobLogService;

    @PostMapping("logs")
    public JsonEntity<PageResult<JobLogBasicBo>> getJobLogs(@RequestBody JobLogRequest request) {
        return ResponseHelper.createInstance(jobLogService.getJobLogs(request));
    }

    @GetMapping("log/{logId}/detail")
    public JsonEntity<JobLogBo> getJobDetails(@PathVariable("logId") Long logId) {
        return ResponseHelper.createInstance(jobLogService.getJobLogDetail(logId));
    }

    @DeleteMapping("log/job/{jobId}")
    public JsonEntity cleanLog(@PathVariable("jobId") Integer jobId) {
        jobLogService.cleanLog(jobId);
        return ResponseHelper.ofNothing();
    }
}
