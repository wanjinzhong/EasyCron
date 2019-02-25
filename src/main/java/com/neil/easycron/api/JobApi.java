package com.neil.easycron.api;

import com.neil.easycron.bo.NewJobBo;
import com.neil.easycron.bo.config.ConfigFileBo;
import com.neil.easycron.bo.response.JsonEntity;
import com.neil.easycron.service.JobService;
import com.neil.easycron.utils.ResponseHelper;
import io.swagger.annotations.Api;
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
public class JobApi {

    @Autowired
    private JobService jobService;

    @PostMapping("job")
    public JsonEntity createNewJob(@RequestBody NewJobBo newJobBo) {
        jobService.createNewJob(newJobBo);
        return ResponseHelper.ofNothing();
    }

    @GetMapping("configs/{jobId}")
    public JsonEntity<ConfigFileBo> getConfigs(@PathVariable("jobId") Integer jobId) {
        return ResponseHelper.createInstance(jobService.getConfigs(jobId));
    }
}
