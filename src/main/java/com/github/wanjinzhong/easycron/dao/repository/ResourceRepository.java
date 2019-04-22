package com.github.wanjinzhong.easycron.dao.repository;

import com.github.wanjinzhong.easycron.dao.entity.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResourceRepository extends JpaRepository<Resource, Long> {
}
