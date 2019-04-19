package com.neil.easycron.dao.repository;
import com.neil.easycron.dao.entity.JobLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobLogRepository extends JpaRepository<JobLog, Long> {
    Page<JobLog> findByJobIdOrderByStartTimeAsc(Integer jobId, Pageable pageable);

    Page<JobLog> findByJobIdOrderByStartTimeDesc(Integer jobId, Pageable pageable);

    void deleteByJobId(Integer jobId);
}
