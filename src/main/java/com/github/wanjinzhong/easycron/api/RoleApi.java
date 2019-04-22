package com.github.wanjinzhong.easycron.api;

import java.util.List;

import com.github.wanjinzhong.easycron.bo.response.JsonEntity;
import com.github.wanjinzhong.easycron.bo.role.BasicRoleBo;
import com.github.wanjinzhong.easycron.service.RoleService;
import com.github.wanjinzhong.easycron.utils.ResponseHelper;
import io.swagger.annotations.Api;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("public/api")
@Api
@RequiresAuthentication
public class RoleApi {

    @Autowired
    private RoleService roleService;

    @GetMapping("roles")
    public JsonEntity<List<BasicRoleBo>> getRoles() {
        return ResponseHelper.createInstance(roleService.getRoles());
    }

    @DeleteMapping("user/{userId}/role/{roleId}")
    @RequiresRoles(value = "USER_MANAGER")
    public JsonEntity deleteUserRole(@PathVariable("userId") Integer userId, @PathVariable("roleId") Integer roleId) {
        roleService.deleteUserRole(userId, roleId);
        return ResponseHelper.ofNothing();
    }

    @PostMapping("user/{userId}/role/{roleId}")
    @RequiresRoles(value = "USER_MANAGER")
    public JsonEntity addUserRole(@PathVariable("userId") Integer userId, @PathVariable("roleId") Integer roleId) {
        roleService.addUserRole(userId, roleId);
        return ResponseHelper.ofNothing();
    }
}
