package com.neil.easycron.service.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import com.neil.easycron.constant.Constant;
import com.neil.easycron.constant.enums.ProfileKey;
import com.neil.easycron.service.MailService;
import com.neil.easycron.service.UserProfileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@Transactional
public class MailServiceImpl implements MailService {


    @Autowired
    private UserProfileService userProfileService;

    @Autowired
    private TemplateEngine templateEngine;

    private Logger logger = LoggerFactory.getLogger(MailServiceImpl.class);

    private JavaMailSenderImpl javaMailSender;

    private MailServiceImpl() {

    }

    @Override
    public JavaMailSenderImpl getInstance() {
        if (javaMailSender != null) {
            return javaMailSender;
        }
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost(userProfileService.getNotNullValue(Constant.SYSTEM_ID, ProfileKey.EMAIL_HOST));
        int port;
        try {
            port = Integer.valueOf(userProfileService.getNotNullValue(Constant.SYSTEM_ID, ProfileKey.EMAIL_PORT));
        } catch (NumberFormatException e) {
            port = 0;
        }
        javaMailSender.setPort(port);

        javaMailSender.setUsername(userProfileService.getNotNullValue(Constant.SYSTEM_ID, ProfileKey.EMAIL_USERNAME));
        javaMailSender.setPassword(userProfileService.getNotNullValue(Constant.SYSTEM_ID, ProfileKey.EMAIL_PASSWORD));
        javaMailSender.setDefaultEncoding("UTF-8");
        Properties properties = new Properties();
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.starttls.enable", "true");
        properties.setProperty("mail.smtp.starttls.required", "true");
        properties.setProperty("mail.smtp.ssl.enable", "true");
        properties.setProperty("mail.smtp.connectiontimeout", "5000");
        properties.setProperty("mail.smtp.timeout", "5000");
        javaMailSender.setJavaMailProperties(properties);
        this.javaMailSender = javaMailSender;
        return javaMailSender;
    }

    private boolean sendEmail(String from, String to, String subject, String template, Map variables) {
        try {
            MimeMessage mimeMessage;
            try {
                mimeMessage = getInstance().createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
                // 设置发件人邮箱
                helper.setFrom(from);
                // 设置收件人邮箱
                helper.setTo(to);
                // 设置邮件标题
                helper.setSubject(subject);

                // 添加正文（使用thymeleaf模板）
                Context context = new Context();
                context.setVariables(variables);
                String content = this.templateEngine.process(template, context);
                helper.setText(content, true);

                // 发送邮件
                javaMailSender.send(mimeMessage);

            } catch (MessagingException e) {
                e.printStackTrace();
            }
        } catch (MailSendException e) {
            logger.error(e.getMessage(), e);
            return false;
        }
        return true;
    }


    @Override
    public boolean sendNewUserEmail(String email, String pwd, String userName) {
        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("TITLE", "用户注册成功");
        dataMap.put("USERNAME", userName);
        dataMap.put("PASSWORD", pwd);
        dataMap.put("EMAIL", email);
        String from = userProfileService.getNotNullValue(Constant.SYSTEM_ID, ProfileKey.EMAIL_USERNAME);
        return sendEmail(from, email, "用户注册成功", "email_register", dataMap);
    }

    @Override
    public boolean sendValCodeEmail(String email, String userName, String valCode) {
        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("TITLE", "修改密码");
        dataMap.put("USERNAME", userName);
        dataMap.put("VAL_CODE", valCode);
        String from = userProfileService.getNotNullValue(Constant.SYSTEM_ID, ProfileKey.EMAIL_USERNAME);
        return sendEmail(from, email, "修改密码", "email_valcode", dataMap);
    }
}
