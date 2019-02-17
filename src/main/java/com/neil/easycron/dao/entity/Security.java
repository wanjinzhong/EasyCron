package com.neil.easycron.dao.entity;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "security")
public class Security implements Serializable {
    @Id
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "password", length = 50)
    private String password;

    @Column(name = "salt", length = 20)
    private String salt;

    @Column(name = "update_datetime")
    private Calendar updateDatetime;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public Calendar getUpdateDatetime() {
        return updateDatetime;
    }

    public void setUpdateDatetime(Calendar updateDatetime) {
        this.updateDatetime = updateDatetime;
    }
}
