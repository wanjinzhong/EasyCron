package com.neil.easycron.dao.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.neil.easycron.constant.enums.RoleCode;

@Entity
@Table(name = "role", uniqueConstraints = {@UniqueConstraint(columnNames = "name", name = "role_I1")})
public class Role implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "code", length = 50, nullable = false)
    @Enumerated(EnumType.STRING)
    private RoleCode code;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String desc;

    @Column(name = "permissions")
    private String permissions;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_role",
               joinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")},
               inverseJoinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")})
    private List<User> users = new ArrayList<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public RoleCode getCode() {
        return code;
    }

    public void setCode(RoleCode code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPermissions() {
        return permissions;
    }

    public void setPermissions(String permissions) {
        this.permissions = permissions;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
