package com.github.wanjinzhong.easycron.service;
import org.springframework.mail.javamail.JavaMailSenderImpl;

public interface MailService {

    JavaMailSenderImpl getInstance();

    void flush();

    boolean sendNewUserEmail(String email, String pwd, String userName);

    boolean sendValCodeEmail(String email, String userName, String valCode);

    boolean testConnection();
}
