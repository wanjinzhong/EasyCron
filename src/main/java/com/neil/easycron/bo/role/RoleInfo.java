package com.neil.easycron.bo.role;
import java.util.List;

import com.neil.easycron.bo.user.BasicUserBo;

public class RoleInfo extends BasicRoleBo{

    private boolean userDeletable;

    private boolean userAddable;

    private List<BasicUserBo> users;

    public boolean isUserDeletable() {
        return userDeletable;
    }

    public void setUserDeletable(boolean userDeletable) {
        this.userDeletable = userDeletable;
    }

    public List<BasicUserBo> getUsers() {
        return users;
    }

    public void setUsers(List<BasicUserBo> users) {
        this.users = users;
    }

    public boolean isUserAddable() {
        return userAddable;
    }

    public void setUserAddable(boolean userAddable) {
        this.userAddable = userAddable;
    }
}
