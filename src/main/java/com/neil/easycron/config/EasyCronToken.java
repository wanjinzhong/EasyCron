package com.neil.easycron.config;

import com.neil.easycron.bo.user.LoginRequestBo;
import com.neil.easycron.exception.BizException;
import org.apache.shiro.authc.AuthenticationToken;

public class EasyCronToken implements AuthenticationToken {
    private LoginRequestBo loginBo;
    public EasyCronToken(LoginRequestBo loginBo) {
        if (loginBo == null) {
            throw new BizException("登陆信息不能为空");
        }
        this.loginBo = loginBo;
    }
    @Override
    public Object getPrincipal() {
        return loginBo.getEmail();
    }

    @Override
    public Object getCredentials() {
        return loginBo.getPassword();
    }
}
