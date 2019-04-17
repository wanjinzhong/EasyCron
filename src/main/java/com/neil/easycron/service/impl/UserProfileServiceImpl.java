package com.neil.easycron.service.impl;
import com.neil.easycron.constant.enums.ProfileKey;
import com.neil.easycron.dao.entity.UserProfile;
import com.neil.easycron.dao.repository.UserProfileRepository;
import com.neil.easycron.service.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserProfileServiceImpl implements UserProfileService {

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Override
    public String getNotNullValue(Integer userId, ProfileKey key) {
        String str = "";
        UserProfile profile = userProfileRepository.findByUserIdAndKey(userId, key);
        if (profile != null) {
            str = profile.getValue();
        }
        return str;
    }
}
