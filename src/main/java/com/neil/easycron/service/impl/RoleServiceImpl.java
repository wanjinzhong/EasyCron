package com.neil.easycron.service.impl;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.neil.easycron.bo.role.BasicRoleBo;
import com.neil.easycron.dao.entity.Role;
import com.neil.easycron.dao.entity.User;
import com.neil.easycron.dao.repository.RoleRepository;
import com.neil.easycron.dao.repository.UserRepository;
import com.neil.easycron.exception.BizException;
import com.neil.easycron.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<BasicRoleBo> getRoles() {
        List<Role> roles = roleRepository.findAll();
        List<BasicRoleBo> roleBos = new ArrayList<>();
        for (Role role : roles) {
            BasicRoleBo roleBo = new BasicRoleBo();
            roleBo.setId(role.getId());
            roleBo.setCode(role.getCode());
            roleBo.setDesc(role.getDesc());
            roleBo.setName(role.getName());
            roleBos.add(roleBo);
        }
        return roleBos;
    }

    @Override
    public void deleteUserRole(Integer userId, Integer roleId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new BizException("用户不存在");
        }
        Iterator<Role> it = user.getRoles().iterator();
        while (it.hasNext()) {
            Role role = it.next();
            if (role.getId().equals(roleId)) {
                it.remove();
                break;
            }
        }
        userRepository.save(user);
    }

    @Override
    public void addUserRole(Integer userId, Integer roleId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new BizException("用户不存在");
        }
        boolean exists = false;
        for (Role role : user.getRoles()) {
            if (role.getId().equals(roleId)) {
                exists = true;
                break;
            }
        }
        if (!exists) {
            Role role = roleRepository.findById(roleId).orElse(null);
            if (role == null) {
                throw new BizException("角色不存在");
            }
            user.getRoles().add(role);
            userRepository.save(user);
        }
    }
}
