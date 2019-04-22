package com.github.wanjinzhong.easycron.dao.repository;
import java.util.List;

import com.github.wanjinzhong.easycron.constant.enums.ProfileKey;
import com.github.wanjinzhong.easycron.dao.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Integer> {

    UserProfile findByUserIdAndKey(Integer userId, ProfileKey key);

    List<UserProfile> findByUserId(Integer userId);
}
