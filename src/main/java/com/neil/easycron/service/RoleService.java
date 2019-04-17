package com.neil.easycron.service;
import java.util.List;

import com.neil.easycron.bo.role.BasicRoleBo;

public interface RoleService {
    List<BasicRoleBo> getRoles();

    void deleteUserRole(Integer userId, Integer roleId);

    void addUserRole(Integer userId, Integer roleId);
}
