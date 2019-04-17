package com.neil.easycron.dao.repository;
import com.neil.easycron.constant.enums.ProfileKey;
import com.neil.easycron.dao.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Integer> {

    UserProfile findByUserIdAndKey(Integer userId, ProfileKey key);
}
