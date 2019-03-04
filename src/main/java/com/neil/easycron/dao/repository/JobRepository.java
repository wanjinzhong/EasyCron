package com.neil.easycron.dao.repository;
import com.neil.easycron.dao.entity.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobRepository extends JpaRepository<Job, Integer> {
    Job findByName(String name);

    Page<Job> findByNameLike(String keyword, Pageable pageable);
}
