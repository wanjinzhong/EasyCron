package com.neil.easycron.dao.repository;
import java.util.Collection;
import java.util.List;

import com.neil.easycron.dao.entity.JobLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobLogRepository extends JpaRepository<JobLog, Long> {
    Page<JobLog> findByJobIdAndStatusIdInAndResolvedAndIdGreaterThanOrderByEntryDatetimeAsc(Integer jobId, Collection<Integer> status_id, String resolved,
                                                                                            Long id, Pageable pageable);
    Page<JobLog> findByJobIdAndStatusIdInAndResolvedOrderByEntryDatetimeAsc(Integer jobId, List<Integer> status, String resolved, Pageable pageable);
    Page<JobLog> findByJobIdAndStatusIdInAndResolvedAndIdIsLessThanOrderByEntryDatetimeDesc(Integer jobId, Collection<Integer> status_id, String resolved,
                                                                                            Long id, Pageable pageable);
    Page<JobLog> findByJobIdAndStatusIdInAndResolvedOrderByEntryDatetimeDesc(Integer jobId, List<Integer> status, String resolved, Pageable pageable);
}
