package com.neil.easycron.service;
import org.springframework.mail.javamail.JavaMailSenderImpl;

public interface MailService {

    JavaMailSenderImpl getInstance();

    boolean sendNewUserEmail(String email, String pwd, String userName);

    boolean sendValCodeEmail(String email, String userName, String valCode);
}
