package com.github.wanjinzhong.easycron.bo.user;
import java.util.List;

import com.github.wanjinzhong.easycron.bo.role.BasicRoleBo;

public class UserInfo extends BasicUserBo{

    private boolean roleAddable;

    private List<BasicRoleBo> roles;

    public boolean isRoleAddable() {
        return roleAddable;
    }

    public void setRoleAddable(boolean roleAddable) {
        this.roleAddable = roleAddable;
    }

    public List<BasicRoleBo> getRoles() {
        return roles;
    }

    public void setRoles(List<BasicRoleBo> roles) {
        this.roles = roles;
    }
}
