package com.neil.easycron.service;
import com.neil.easycron.bo.user.LoginRequestBo;
import com.neil.easycron.dao.entity.User;

public interface UserService {
    User findByEmail(String email);

    String login(LoginRequestBo requestBo);
}
