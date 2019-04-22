package com.github.wanjinzhong.easycron.bo.user;
public class RegisterResultBo {
    private boolean emailSuccess;
    private String email;
    private String password;

    public boolean isEmailSuccess() {
        return emailSuccess;
    }

    public void setEmailSuccess(boolean emailSuccess) {
        this.emailSuccess = emailSuccess;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
