package com.github.wanjinzhong.easycron.api;

import java.util.List;

import com.github.wanjinzhong.easycron.bo.response.JsonEntity;
import com.github.wanjinzhong.easycron.bo.PluginBo;
import com.github.wanjinzhong.easycron.service.PluginService;
import com.github.wanjinzhong.easycron.utils.ResponseHelper;
import io.swagger.annotations.Api;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("public/api")
@RequiresAuthentication
@Api
public class PluginApi {

    @Autowired
    private PluginService pluginService;

    @GetMapping("plugins")
    public JsonEntity<List<PluginBo>> getPlugins() {
        return ResponseHelper.createInstance(pluginService.getAllPlugins());
    }
}
