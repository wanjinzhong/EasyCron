package com.neil.easycron.service;
import java.util.List;

import com.neil.easycron.bo.response.JsonEntity;
import com.neil.easycron.bo.role.RoleInfo;
import com.neil.easycron.bo.user.LoginRequestBo;
import com.neil.easycron.bo.user.RegisterRequestBo;
import com.neil.easycron.bo.user.RegisterResultBo;
import com.neil.easycron.bo.user.UserInfo;
import com.neil.easycron.constant.enums.UserStatus;
import com.neil.easycron.dao.entity.User;

public interface UserService {
    User findByEmail(String email);

    String login(LoginRequestBo requestBo);

    RegisterResultBo regist(RegisterRequestBo registerRequestBo);

    UserInfo getUserInfo();

    List<UserInfo> getUsersAsUserView();

    List<RoleInfo> getUsersAsRoleView();

    void updateUserStatus(Integer userId, UserStatus status);

}
