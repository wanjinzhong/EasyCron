package com.neil.easycron.service.impl;
import java.util.UUID;

import javax.transaction.Transactional;

import com.neil.easycron.bo.user.LoginRequestBo;
import com.neil.easycron.bo.user.RegisterRequestBo;
import com.neil.easycron.config.EasyCronToken;
import com.neil.easycron.constant.Constant;
import com.neil.easycron.dao.entity.User;
import com.neil.easycron.dao.repository.UserRepository;
import com.neil.easycron.exception.BizException;
import com.neil.easycron.service.UserService;
import com.neil.easycron.utils.ValidatorUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.crypto.hash.SimpleHash;
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

    @Override
    public void regist(RegisterRequestBo registerRequestBo) {
        if (StringUtils.isBlank(registerRequestBo.getEmail()) || StringUtils.isBlank(registerRequestBo.getPassword())) {
            throw new BizException("邮箱或密码不能为空");
        }
        if (StringUtils.isBlank(registerRequestBo.getUserName())) {
            throw new BizException("用户名不能为空");
        }
        if (registerRequestBo.getPassword().length() < 6) {
            throw new BizException("密码不能小于6位");
        }
        if (!ValidatorUtil.isEmail(registerRequestBo.getEmail())) {
            throw new BizException("邮箱格式不正确");
        }
        User user = userRepository.findByEmail(registerRequestBo.getEmail());
        if (user != null) {
            throw new BizException("用户已存在");
        }
        String salt = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 10);
        String securityPwd = new SimpleHash("MD5", registerRequestBo.getPassword(), salt, Constant.HASH_ITERATIONS).toHex();
        user = new User();
        user.setEmail(registerRequestBo.getEmail());
        user.setName(registerRequestBo.getUserName());
        user.setSalt(salt);
        user.setPassword(securityPwd);
        userRepository.save(user);
    }
}
