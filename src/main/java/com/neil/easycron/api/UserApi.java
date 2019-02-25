package com.neil.easycron.api;
import com.google.common.collect.Lists;
import com.neil.easycron.bo.response.JsonEntity;
import com.neil.easycron.bo.user.LoginRequestBo;
import com.neil.easycron.bo.user.UserInfo;
import com.neil.easycron.service.UserService;
import com.neil.easycron.utils.ResponseHelper;
import io.swagger.annotations.Api;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
    public JsonEntity login(@RequestBody LoginRequestBo loginRequestBo) {
        userService.login(loginRequestBo);
        return ResponseHelper.ofNothing();
    }

    @GetMapping("userInfo")
    @RequiresAuthentication
    public JsonEntity<UserInfo> getUserInfo() {
        UserInfo userInfo = new UserInfo();
        userInfo.setAvatar("http://img4.duitang.com/uploads/item/201412/24/20141224224554_SuYth.thumb.700_0.jpeg");
        userInfo.setAccess(Lists.newArrayList("a","b"));
        userInfo.setEmail("1051750377@qq.com");
        userInfo.setName("Neil Wan");
        return ResponseHelper.createInstance(userInfo);
    }
}
