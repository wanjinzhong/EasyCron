package com.github.wanjinzhong.easycron.dao.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.github.wanjinzhong.easycron.constant.enums.ProfileKey;

@Entity
@Table(name = "user_profile", uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "profile_key"}, name = "user_profile_I1")})
public class UserProfile implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "profile_key", length = 100, nullable = false)
    @Enumerated(EnumType.STRING)
    private ProfileKey key;

    @Column(name = "profile_value", nullable = false)
    private String value;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ProfileKey getKey() {
        return key;
    }

    public void setKey(ProfileKey key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
