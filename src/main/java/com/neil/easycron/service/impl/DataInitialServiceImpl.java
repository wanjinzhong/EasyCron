package com.neil.easycron.service.impl;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import com.neil.easycron.constant.enums.ListCatalog;
import com.neil.easycron.constant.enums.RoleCode;
import com.neil.easycron.dao.entity.ListBox;
import com.neil.easycron.dao.entity.Role;
import com.neil.easycron.dao.entity.User;
import com.neil.easycron.dao.repository.ListBoxRepository;
import com.neil.easycron.dao.repository.RoleRepository;
import com.neil.easycron.dao.repository.UserRepository;
import com.neil.easycron.service.DataInitialService;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
@Transactional
public class DataInitialServiceImpl implements DataInitialService {

    @Autowired
    private ListBoxRepository listBoxRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void init() throws Exception {
        List<ListBox> listBoxes = new ArrayList<>();
        List<Role> roles = new ArrayList<>();
        List<User> users = new ArrayList<>();
        readInitialData(listBoxes, roles, users, new ClassPathResource("initial_data.xml").getInputStream());
        initListBox(listBoxes);
        initRole(roles);
        initUser(users);
    }

    private void initUser(List<User> users) {
        for (User user : users) {
            User exists = userRepository.findById(user.getId()).orElse(null);
            if (exists == null) {
                userRepository.save(user);
            } else {
                exists.setName(user.getName());
                exists.setEmail(user.getEmail());
                userRepository.save(exists);
            }
        }
    }

    private void initRole(List<Role> roles) {
        for (Role role : roles) {
            Role exists = roleRepository.findByNameAndPermissions(role.getName(), role.getPermissions());
            if (exists == null) {
                roleRepository.save(role);
            }
        }
    }

    private void initListBox(List<ListBox> listBoxes) {
        for (ListBox listBox : listBoxes) {
            ListBox exists = listBoxRepository.findByCatalogAndCode(listBox.getCatalog(), listBox.getCode());
            if (exists == null) {
                listBoxRepository.save(listBox);
            } else {
                exists.setDetail(listBox.getDetail());
                exists.setDisplayName(listBox.getDisplayName());
                exists.setSeq(listBox.getSeq());
                listBoxRepository.save(exists);
            }
        }
    }

    private void readInitialData(List<ListBox> listBoxes, List<Role> roles, List<User> users, InputStream inputStream)
        throws DocumentException, NoSuchFieldException, IllegalAccessException {
        SAXReader saxReader = new SAXReader();
        Document document = saxReader.read(inputStream);
        Element root = document.getRootElement();
        for (Element table : root.elements()) {
            String tableName = table.attributeValue("name");
            switch (tableName) {
                case "ListBox":
                    listBoxes.addAll(buildListBoxes(table));
                    break;
                case "Role":
                    roles.addAll(buildRoles(table));
                    break;
                case "User":
                    users.addAll(buildUsers(table));
                    break;
            }
        }
    }

    private List<User> buildUsers(Element table) throws NoSuchFieldException, IllegalAccessException {
        List<User> users = new ArrayList<>();
        for (Element data : table.elements()) {
            User user = new User();
            for (Element field : data.elements()) {
                Field f = user.getClass().getDeclaredField(field.attributeValue("name"));
                f.setAccessible(true);
                if (f.getType().equals(Integer.class)) {
                    f.set(user, Integer.valueOf(field.content().get(0).getText()));
                } else {
                    f.set(user, CollectionUtils.isEmpty(field.content()) ? null : field.content().get(0).getText());
                }
            }
            users.add(user);
        }
        return users;
    }

    private List<Role> buildRoles(Element table) throws NoSuchFieldException, IllegalAccessException {
        List<Role> roles = new ArrayList<>();
        for (Element data : table.elements()) {
            Role role = new Role();
            for (Element field : data.elements()) {
                Field f = role.getClass().getDeclaredField(field.attributeValue("name"));
                f.setAccessible(true);
                if (f.getType().equals(Integer.class)) {
                    f.set(role, Integer.valueOf(field.content().get(0).getText()));
                } else if (f.getType().equals(RoleCode.class)) {
                    f.set(role, RoleCode.valueOf(CollectionUtils.isEmpty(field.content()) ? null : field.content().get(0).getText()));
                } else {
                    f.set(role, CollectionUtils.isEmpty(field.content()) ? null : field.content().get(0).getText());
                }
            }
            roles.add(role);
        }
        return roles;
    }

    private List<ListBox> buildListBoxes(Element table) throws NoSuchFieldException, IllegalAccessException {
        List<ListBox> listBoxes = new ArrayList<>();
        for (Element data : table.elements()) {
            ListBox listBox = new ListBox();
            for (Element field : data.elements()) {
                Field f = listBox.getClass().getDeclaredField(field.attributeValue("name"));
                f.setAccessible(true);
                if (f.getType().equals(Integer.class)) {
                    f.set(listBox, Integer.valueOf(field.content().get(0).getText()));
                } else if (f.getType().equals(ListCatalog.class)) {
                    f.set(listBox, ListCatalog.valueOf(field.content().get(0).getText()));
                } else {
                    f.set(listBox, CollectionUtils.isEmpty(field.content()) ? null : field.content().get(0).getText());
                }
            }
            listBoxes.add(listBox);
        }
        return listBoxes;
    }
}
