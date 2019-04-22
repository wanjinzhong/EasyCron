package com.github.wanjinzhong.easycron.service;
import java.util.List;

import com.github.wanjinzhong.easycron.bo.user.ChangePwdBo;
import com.github.wanjinzhong.easycron.bo.user.LoginRequestBo;
import com.github.wanjinzhong.easycron.bo.user.RegisterRequestBo;
import com.github.wanjinzhong.easycron.bo.user.UserInfo;
import com.github.wanjinzhong.easycron.constant.enums.UserStatus;
import com.github.wanjinzhong.easycron.dao.entity.User;
import com.github.wanjinzhong.easycron.bo.role.RoleInfo;
import com.github.wanjinzhong.easycron.bo.user.RegisterResultBo;

public interface UserService {
    User findByEmail(String email);

    String login(LoginRequestBo requestBo);

    RegisterResultBo regist(RegisterRequestBo registerRequestBo);

    UserInfo getUserInfo();

    List<UserInfo> getUsersAsUserView();

    List<RoleInfo> getUsersAsRoleView();

    void updateUserStatus(Integer userId, UserStatus status);

    void updateName(String name);

    void getValCode();

    void logout();

    void changePwd(ChangePwdBo changePwdBo);
}
