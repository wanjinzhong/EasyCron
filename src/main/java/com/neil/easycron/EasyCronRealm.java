package com.neil.easycron;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.collect.Sets;
import com.neil.easycron.config.EasyCronToken;
import com.neil.easycron.dao.entity.Role;
import com.neil.easycron.dao.entity.User;
import com.neil.easycron.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

public class EasyCronRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        String email = (String) SecurityUtils.getSubject().getPrincipal();
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        User user = userService.findByEmail(email);
        info.setRoles(user.getRoles().stream().map(Role::getName).collect(Collectors.toSet()));
        Set<String> permissions = new HashSet<>();
        user.getRoles().forEach(role -> {
            permissions.addAll(Sets.newHashSet(role.getPermissions().split(",")));
        });
        info.setStringPermissions(permissions);
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        EasyCronToken token = (EasyCronToken) authenticationToken;
        String email = (String) token.getPrincipal();
        User user = userService.findByEmail(email);
        if (user != null) {
            return new SimpleAuthenticationInfo(user.getEmail(), user.getPassword(), ByteSource.Util.bytes(user.getSalt()), getName());
        } else {
            throw new UnknownAccountException();
        }
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof EasyCronToken;
    }
}
