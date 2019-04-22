package com.github.wanjinzhong.easycron.service.impl;
import java.util.List;

import com.github.wanjinzhong.easycron.bo.SettingBo;
import com.github.wanjinzhong.easycron.constant.enums.ProfileKey;
import com.github.wanjinzhong.easycron.dao.entity.UserProfile;
import com.github.wanjinzhong.easycron.dao.repository.UserProfileRepository;
import com.github.wanjinzhong.easycron.exception.BizException;
import com.github.wanjinzhong.easycron.service.MailService;
import com.github.wanjinzhong.easycron.service.UserProfileService;
import com.github.wanjinzhong.easycron.utils.ValidatorUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserProfileServiceImpl implements UserProfileService {

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private MailService mailService;

    @Override
    public String getNotNullValue(Integer userId, ProfileKey key) {
        String str = "";
        UserProfile profile = userProfileRepository.findByUserIdAndKey(userId, key);
        if (profile != null) {
            str = profile.getValue();
        }
        return str;
    }

    @Override
    public SettingBo getSettings() {
        List<UserProfile> settings = userProfileRepository.findByUserId(1);
        SettingBo setting = new SettingBo();
        for (UserProfile profile : settings) {
            switch (profile.getKey()) {
                case EMAIL_USERNAME:
                    setting.setEmail(profile.getValue());
                    break;
                case EMAIL_PASSWORD:
                    setting.setEmailPwd(profile.getValue());
                    break;
                case EMAIL_HOST:
                    setting.setEmailHost(profile.getValue());
                    break;
                case EMAIL_PORT:
                    setting.setEmailPort(profile.getValue());
                    break;
            }
        }
        return setting;
    }

    @Override
    public boolean saveSettings(SettingBo settingBo) {
        validateSetting(settingBo);
        List<UserProfile> settings = userProfileRepository.findByUserId(1);
        for (UserProfile profile : settings) {
            switch (profile.getKey()) {
                case EMAIL_USERNAME:
                    profile.setValue(settingBo.getEmail());
                    break;
                case EMAIL_PASSWORD:
                    profile.setValue(settingBo.getEmailPwd());
                    break;
                case EMAIL_HOST:
                    profile.setValue(settingBo.getEmailHost());
                    break;
                case EMAIL_PORT:
                    profile.setValue(settingBo.getEmailPort());
                    break;
            }
        }
        userProfileRepository.saveAll(settings);
        mailService.flush();
        return mailService.testConnection();
    }

    private void validateSetting(SettingBo settingBo) {
        if (StringUtils.isBlank(settingBo.getEmail())) {
            throw new BizException("邮箱用户名不能为空");
        }
        if (StringUtils.isBlank(settingBo.getEmailPwd())) {
            throw new BizException("邮箱密码不能为空");
        }
        if (StringUtils.isBlank(settingBo.getEmailHost())) {
            throw new BizException("邮件服务器不能为空");
        }
        if (StringUtils.isBlank(settingBo.getEmailPort())) {
            throw new BizException("邮件服务器端口不能为空");
        }
        if (!ValidatorUtil.isEmail(settingBo.getEmail())) {
            throw new BizException("邮箱格式不正确");
        }
    }
}
