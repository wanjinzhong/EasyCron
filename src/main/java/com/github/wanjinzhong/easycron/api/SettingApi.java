package com.github.wanjinzhong.easycron.api;

import com.github.wanjinzhong.easycron.bo.SettingBo;
import com.github.wanjinzhong.easycron.bo.response.JsonEntity;
import com.github.wanjinzhong.easycron.service.UserProfileService;
import com.github.wanjinzhong.easycron.utils.ResponseHelper;
import io.swagger.annotations.Api;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("public/api")
@Api
@RequiresAuthentication
@RequiresRoles("SYSTEM_MANAGER")
public class SettingApi {

    @Autowired
    private UserProfileService userProfileService;

    @GetMapping("settings")
    public JsonEntity<SettingBo> getSettings() {
        return ResponseHelper.createInstance(userProfileService.getSettings());
    }

    @PostMapping("settings")
    public JsonEntity<Boolean> saveSettings(@RequestBody SettingBo settingBo) {
        return ResponseHelper.createInstance(userProfileService.saveSettings(settingBo));
    }
}
