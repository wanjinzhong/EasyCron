package com.github.wanjinzhong.easycron.dao.repository;
import java.util.List;

import com.github.wanjinzhong.easycron.dao.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    User findByEmail(String email);

    @Query("from User u where u.status.catalog = 'USER_STATUS' and u.status.code = 'NORMAL' and u.id <> 1")
    List<User> findActive();
}
