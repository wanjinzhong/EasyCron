package com.github.wanjinzhong.easycron.api;

import com.github.wanjinzhong.easycron.service.MailService;
import io.swagger.annotations.Api;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("public/api")
@Api
@RequiresAuthentication
public class MailApi {

    @Autowired
    private MailService mailService;

    @PostMapping("email")
    public void sendEmail() {
        mailService.sendNewUserEmail("1051750377@qq.com", "123456", "Neil Wan");
    }
}
