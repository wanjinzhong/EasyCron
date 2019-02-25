package com.neil.easycron.service.impl;
import javax.transaction.Transactional;

import com.neil.easycron.bo.user.LoginRequestBo;
import com.neil.easycron.config.EasyCronToken;
import com.neil.easycron.dao.entity.User;
import com.neil.easycron.dao.repository.UserRepository;
import com.neil.easycron.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User findByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user != null && user.getId() == 1) {
            return null;
        } else {
            return user;
        }
    }

    @Override
    public String login(LoginRequestBo requestBo) {
        EasyCronToken token = new EasyCronToken(requestBo);
        try {
            SecurityUtils.getSubject().login(token);
            return "OK";
        } catch (IncorrectCredentialsException e) {
            throw new AuthenticationException("密码错误", e);
        }
    }
}
