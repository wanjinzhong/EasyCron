package com.github.wanjinzhong.easycron.api;

import java.util.List;

import com.github.wanjinzhong.easycron.bo.job.JobStatusBo;
import com.github.wanjinzhong.easycron.bo.response.JsonEntity;
import com.github.wanjinzhong.easycron.service.ListBoxService;
import com.github.wanjinzhong.easycron.utils.ResponseHelper;
import io.swagger.annotations.Api;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("public/api")
@Api
@RequiresAuthentication
public class ListBoxApi {

    @Autowired
    private ListBoxService listBoxService;

    @RequestMapping("jobStatus")
    public JsonEntity<List<JobStatusBo>> getJobStatus() {
        return ResponseHelper.createInstance(listBoxService.getJobStatus());
    }
}
