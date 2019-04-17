package com.neil.easycron.bo.user;
public class LoginResultBo {
    private String token;

    public LoginResultBo(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
