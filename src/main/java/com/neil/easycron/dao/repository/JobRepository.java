package com.neil.easycron.dao.repository;
import com.neil.easycron.dao.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobRepository extends JpaRepository<Job, Integer> {
    Job findByName(String name);
}
