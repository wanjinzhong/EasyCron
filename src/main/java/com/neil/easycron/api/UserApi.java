package com.neil.easycron.api;
import java.util.List;
import java.util.UUID;

import com.neil.easycron.bo.response.JsonEntity;
import com.neil.easycron.bo.role.RoleInfo;
import com.neil.easycron.bo.user.LoginRequestBo;
import com.neil.easycron.bo.user.LoginResultBo;
import com.neil.easycron.bo.user.RegisterRequestBo;
import com.neil.easycron.bo.user.RegisterResultBo;
import com.neil.easycron.bo.user.UserInfo;
import com.neil.easycron.constant.enums.UserStatus;
import com.neil.easycron.service.UserService;
import com.neil.easycron.utils.ResponseHelper;
import io.swagger.annotations.Api;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public/api")
@CrossOrigin
@Api
public class UserApi {

    @Autowired
    private UserService userService;

    @PostMapping("login")
    public JsonEntity<LoginResultBo> login(@RequestBody LoginRequestBo loginRequestBo) {
        userService.login(loginRequestBo);
        return ResponseHelper.createInstance(new LoginResultBo(UUID.randomUUID().toString().replace("-","")));
    }

    @PostMapping("user")
    @RequiresRoles(value = "USER_MANAGER")
    public JsonEntity<RegisterResultBo> register(@RequestBody RegisterRequestBo registerRequestBo) {
        RegisterResultBo res = userService.regist(registerRequestBo);
        return ResponseHelper.createInstance(res);
    }

    @GetMapping("userInfo")
    @RequiresAuthentication
    public JsonEntity<UserInfo> getUserInfo() {
        return ResponseHelper.createInstance(userService.getUserInfo());
    }

    @GetMapping("users/userView")
    public JsonEntity<List<UserInfo>> getUsersAsUserView() {
        return ResponseHelper.createInstance(userService.getUsersAsUserView());
    }

    @GetMapping("users/roleView")
    public JsonEntity<List<RoleInfo>> getUsersAsRoleView() {
        return ResponseHelper.createInstance(userService.getUsersAsRoleView());
    }

    @PutMapping("user/{userId}/disable")
    @RequiresRoles(value = "USER_MANAGER")
    public JsonEntity disableUser(@PathVariable("userId") Integer userId) {
        userService.updateUserStatus(userId, UserStatus.DISABLED);
        return ResponseHelper.ofNothing();
    }

    @PutMapping("user/{userId}/enable")
    @RequiresRoles(value = "USER_MANAGER")
    public JsonEntity enableUser(@PathVariable("userId") Integer userId) {
        userService.updateUserStatus(userId, UserStatus.NORMAL);
        return ResponseHelper.ofNothing();
    }
}
