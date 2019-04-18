package com.neil.easycron.dao.repository;
import com.neil.easycron.dao.entity.JobLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobLogRepository extends JpaRepository<JobLog, Long> {
    Page<JobLog> findByJobIdAndIdGreaterThanOrderByEntryDatetimeAsc(Integer jobId, Long id, Pageable pageable);

    Page<JobLog> findByJobIdOrderByEntryDatetimeAsc(Integer jobId, Pageable pageable);

    Page<JobLog> findByJobIdAndIdIsLessThanOrderByEntryDatetimeDesc(Integer jobId, Long id, Pageable pageable);

    Page<JobLog> findByJobIdOrderByEntryDatetimeDesc(Integer jobId, Pageable pageable);
}
