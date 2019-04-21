package com.neil.easycron.service.impl;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import com.neil.easycron.ValCodeCache;
import com.neil.easycron.bo.ValCodeBo;
import com.neil.easycron.bo.role.BasicRoleBo;
import com.neil.easycron.bo.role.RoleInfo;
import com.neil.easycron.bo.user.BasicUserBo;
import com.neil.easycron.bo.user.LoginRequestBo;
import com.neil.easycron.bo.user.RegisterRequestBo;
import com.neil.easycron.bo.user.RegisterResultBo;
import com.neil.easycron.bo.user.UserInfo;
import com.neil.easycron.config.EasyCronToken;
import com.neil.easycron.constant.Constant;
import com.neil.easycron.constant.enums.ListCatalog;
import com.neil.easycron.constant.enums.RoleCode;
import com.neil.easycron.constant.enums.UserStatus;
import com.neil.easycron.constant.enums.ValCodeType;
import com.neil.easycron.dao.entity.ListBox;
import com.neil.easycron.dao.entity.Role;
import com.neil.easycron.dao.entity.User;
import com.neil.easycron.dao.repository.ListBoxRepository;
import com.neil.easycron.dao.repository.RoleRepository;
import com.neil.easycron.dao.repository.UserRepository;
import com.neil.easycron.exception.BizException;
import com.neil.easycron.service.MailService;
import com.neil.easycron.service.UserService;
import com.neil.easycron.utils.UuidUtil;
import com.neil.easycron.utils.ValidatorUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ListBoxRepository listBoxRepository;

    @Autowired
    private MailService mailService;

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
    public RegisterResultBo regist(RegisterRequestBo registerRequestBo) {
        if (StringUtils.isBlank(registerRequestBo.getName())) {
            throw new BizException("用户名不能为空");
        }
        if (StringUtils.isBlank(registerRequestBo.getEmail())) {
            throw new BizException("邮箱不能为空");
        }
        if (!ValidatorUtil.isEmail(registerRequestBo.getEmail())) {
            throw new BizException("邮箱格式不正确");
        }
        User user = userRepository.findByEmail(registerRequestBo.getEmail());
        if (user != null) {
            throw new BizException("用户已存在");
        }
        String pwd = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 8);
        String salt = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 10);
        String securityPwd = new SimpleHash("MD5", pwd, salt, Constant.HASH_ITERATIONS).toHex();
        user = new User();
        user.setEmail(registerRequestBo.getEmail());
        user.setName(registerRequestBo.getName());
        user.setSalt(salt);
        user.setPassword(securityPwd);
        ListBox normal = listBoxRepository.findByCatalogAndCode(ListCatalog.USER_STATUS, UserStatus.NORMAL.name());
        user.setStatus(normal);
        Role role = roleRepository.findByCode(RoleCode.NORMAL_USER);
        user.getRoles().add(role);
        userRepository.save(user);
        RegisterResultBo resultBo = new RegisterResultBo();
        boolean status = mailService.sendNewUserEmail(user.getEmail(), pwd, user.getName());
        resultBo.setEmailSuccess(status);
        resultBo.setEmail(registerRequestBo.getEmail());
        if (!status) {
            resultBo.setPassword(pwd);
        }
        return resultBo;
    }

    @Override
    public UserInfo getUserInfo() {
        User user = userRepository.findByEmail((String) SecurityUtils.getSubject().getPrincipal());
        UserInfo userInfo = toUserInfo(user);
        return userInfo;
    }

    private UserInfo toUserInfo(User user) {
        UserInfo userInfo = new UserInfo();
        userInfo.setId(user.getId());
        userInfo.setName(user.getName());
        userInfo.setEmail(user.getEmail());
        userInfo.setStatus(user.getStatus().getDisplayName());
        userInfo.setStatusCode(user.getStatus().getCode());

        userInfo.setRoles(user.getRoles().stream().map(role -> {
            boolean deletable = !(RoleCode.NORMAL_USER.name().equals(role.getCode()) || RoleCode.USER_MANAGER.name().equals(role.getCode()) && role.getUsers().size() <= 1);
            return new BasicRoleBo(role.getId(), role.getCode(), role.getName(), role.getDesc(), deletable);
        }).collect(
            Collectors.toList()));
        userInfo.getRoles().sort(Comparator.comparing(BasicRoleBo::getId));
        return userInfo;
    }

    @Override
    public List<UserInfo> getUsersAsUserView() {
        List<User> users = userRepository.findAll();
        List<Role> roles = roleRepository.findAll();
        users = users.stream().filter(u -> u.getId() != 1).collect(Collectors.toList());
        List<UserInfo> userInfos = new ArrayList<>();
        for (User user : users) {
            UserInfo userInfo = toUserInfo(user);
            userInfo.setRoleAddable(user.getRoles().size() < roles.size() && UserStatus.NORMAL.name().equals(user.getStatus().getCode()));
            userInfos.add(userInfo);
        }
        return userInfos;
    }

    @Override
    public List<RoleInfo> getUsersAsRoleView() {
        List<Role> roles = roleRepository.findAll();
        List<Integer> allActiveUserId = userRepository.findActive().stream().map(User::getId).collect(Collectors.toList());
        List<RoleInfo> roleInfos = new ArrayList<>();
        for (Role role : roles) {
            List<Integer> roleUserIds = role.getUsers().stream().filter(u -> u.getStatus().getCode().equals(UserStatus.NORMAL.name())).map(User::getId).collect(Collectors.toList());
            RoleInfo roleInfo = toRoleInfo(role);
            roleInfo.setUserAddable(roleUserIds.size() < allActiveUserId.size());
            roleInfos.add(roleInfo);
        }
        return roleInfos;
    }

    @Override
    public void updateUserStatus(Integer userId, UserStatus userStatus) {
        ListBox listBox = listBoxRepository.findByCatalogAndCode(ListCatalog.USER_STATUS, userStatus.name());
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new BizException("用户不存在");
        }
        Role role = user.getRoles().stream().filter(r -> RoleCode.USER_MANAGER.name().equals(r.getCode())).findFirst().orElse(null);
        if (role != null && role.getUsers().size() <= 1) {
            throw new BizException("不能禁用该帐户，因为该帐户是唯一" + role.getName());
        }
        user.setStatus(listBox);
        userRepository.save(user);
    }

    @Override
    public void updateName(Integer userId, String name) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new BizException("用户不存在");
        }
        UserInfo userInfo = getUserInfo();
        if (userInfo == null) {
            userInfo = new UserInfo();
        }
        if (!user.getId().equals(userInfo.getId())) {
            throw new UnauthorizedException();
        }
        user.setName(name);
        userRepository.save(user);
    }

    @Override
    public void getValCode(Integer userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new BizException("用户不存在");
        }
        String code = UuidUtil.getUuid().substring(0, 8);
        ValCodeBo valCodeBo = new ValCodeBo();
        valCodeBo.setCode(code);
        valCodeBo.setStartTime(Calendar.getInstance().getTimeInMillis());
        Calendar expireTime = Calendar.getInstance();
        expireTime.add(Calendar.MINUTE, 30);
        valCodeBo.setExpireTime(expireTime.getTimeInMillis());
        valCodeBo.setType(ValCodeType.PWD_RESET);
        ValCodeCache.put(valCodeBo);
        mailService.sendValCodeEmail(user.getEmail(), user.getName(), code);
    }

    @Override
    public void logout() {
        SecurityUtils.getSubject().logout();
    }

    private RoleInfo toRoleInfo(Role role) {
        RoleInfo roleInfo = new RoleInfo();
        roleInfo.setId(role.getId());
        roleInfo.setCode(role.getCode());
        roleInfo.setDesc(role.getDesc());
        roleInfo.setName(role.getName());
        final Integer[] activeUserCount = {0};
        roleInfo.setUsers(role.getUsers().stream().map(u -> {
            BasicUserBo user = new BasicUserBo();
            user.setId(u.getId());
            user.setEmail(u.getEmail());
            user.setName(u.getName());
            user.setStatus(u.getStatus().getDisplayName());
            user.setStatusCode(u.getStatus().getCode());
            activeUserCount[0] += UserStatus.NORMAL.name().equals(u.getStatus().getCode()) ? 1 : 0;
            return user;
        }).collect(Collectors.toList()));
        roleInfo.setUserDeletable(!(RoleCode.NORMAL_USER.name().equals(role.getCode()) || RoleCode.USER_MANAGER.name().equals(role.getCode())
                                                                                          && activeUserCount.length <= 1));
        return roleInfo;
    }
}
