package com.neil.easycron.api;
import com.neil.easycron.bo.PageResult;
import com.neil.easycron.bo.job.JobLogBasicBo;
import com.neil.easycron.bo.job.JobLogBo;
import com.neil.easycron.bo.job.JobLogRequest;
import com.neil.easycron.bo.response.JsonEntity;
import com.neil.easycron.service.JobLogService;
import com.neil.easycron.utils.ResponseHelper;
import io.swagger.annotations.Api;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
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
}
