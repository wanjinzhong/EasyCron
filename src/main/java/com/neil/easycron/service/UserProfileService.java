package com.neil.easycron.service;
import com.neil.easycron.constant.enums.ListCatalog;
import com.neil.easycron.constant.enums.ProfileKey;

public interface UserProfileService {
    String getNotNullValue(Integer userId, ProfileKey key);
}
