package com.github.wanjinzhong.easycron.dao.repository;
import java.util.List;

import com.github.wanjinzhong.easycron.dao.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface JobRepository extends JpaRepository<Job, Integer>, JpaSpecificationExecutor<Job> {
    Job findByName(String name);
    List<Job> findByStatusCode(String status);
}
