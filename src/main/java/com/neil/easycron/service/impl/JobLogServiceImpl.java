package com.neil.easycron.service.impl;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.neil.easycron.bo.PageResult;
import com.neil.easycron.bo.job.JobLogBo;
import com.neil.easycron.bo.job.JobLogRequest;
import com.neil.easycron.dao.entity.JobLog;
import com.neil.easycron.dao.repository.JobLogRepository;
import com.neil.easycron.service.JobLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
@Transactional
public class JobLogServiceImpl implements JobLogService {

    @Autowired
    private JobLogRepository jobLogRepository;

    @Override
    public PageResult<JobLogBo> getJobLogs(JobLogRequest request) {
        if (CollectionUtils.isEmpty(request.getStatus())) {
            return new PageResult<>(request.getPage(), request.getSize(), 0, 0, new ArrayList<>());
        }
        Page<JobLog> jobLogs;
        PageRequest pageRequest = PageRequest.of(request.getPage() - 1, request.getSize());
        if (request.getPage() == 1) {
            if (request.isAsc()) {
                jobLogs = jobLogRepository.findByJobIdAndStatusIdInAndResolvedOrderByEntryDatetimeAsc(request.getJobId(), request.getStatus(),
                                                                                                      request.isOnlyResolved() ? "Y" : "N", pageRequest);
            } else {
                jobLogs = jobLogRepository.findByJobIdAndStatusIdInAndResolvedOrderByEntryDatetimeDesc(request.getJobId(), request.getStatus(),
                                                                                                       request.isOnlyResolved() ? "Y" : "N", pageRequest);
            }
        } else {
            if (request.isAsc()) {
                jobLogs = jobLogRepository.findByJobIdAndStatusIdInAndResolvedAndIdGreaterThanOrderByEntryDatetimeAsc(request.getJobId(), request.getStatus(),
                                                                                                                      request.isOnlyResolved() ? "Y" : "N",
                                                                                                                      request.getLatestId(), pageRequest);
            } else {
                jobLogs = jobLogRepository.findByJobIdAndStatusIdInAndResolvedAndIdIsLessThanOrderByEntryDatetimeDesc(request.getJobId(), request.getStatus(),
                                                                                                                      request.isOnlyResolved() ? "Y" : "N",
                                                                                                                      request.getLatestId(), pageRequest);
            }
        }
        List<JobLogBo> logs = jobLogs.get().map(log -> {
            JobLogBo logBo = new JobLogBo();
            logBo.setStartTime(log.getStartTime());
            logBo.setEndTime(log.getEndTime());
            logBo.setDuring((log.getEndTime().getTimeInMillis() - log.getStartTime().getTimeInMillis()) * 1.0 / 1000);
            logBo.setMessage(log.getMessage());
            logBo.setResolved("Y".equalsIgnoreCase(log.getResolved()));
            return logBo;
        }).collect(Collectors.toList());
        return new PageResult<>(request.getPage(), request.getSize(), jobLogs.getTotalPages(), jobLogs.getTotalElements(), logs);
    }
}
