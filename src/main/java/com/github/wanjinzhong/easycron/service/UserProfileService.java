package com.github.wanjinzhong.easycron.service;
import com.github.wanjinzhong.easycron.bo.SettingBo;
import com.github.wanjinzhong.easycron.constant.enums.ProfileKey;

public interface UserProfileService {
    String getNotNullValue(Integer userId, ProfileKey key);

    SettingBo getSettings();

    boolean saveSettings(SettingBo settingBo);
}
