package com.neil.easycron.service.impl;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

import com.neil.easycron.bo.PageResult;
import com.neil.easycron.bo.job.JobLogBo;
import com.neil.easycron.bo.job.JobLogRequest;
import com.neil.easycron.constant.Constant;
import com.neil.easycron.constant.enums.ListCatalog;
import com.neil.easycron.constant.enums.YorN;
import com.neil.easycron.dao.entity.Job;
import com.neil.easycron.dao.entity.JobLog;
import com.neil.easycron.dao.entity.ListBox;
import com.neil.easycron.dao.entity.User;
import com.neil.easycron.dao.repository.JobLogRepository;
import com.neil.easycron.dao.repository.JobRepository;
import com.neil.easycron.dao.repository.ListBoxRepository;
import com.neil.easycron.dao.repository.UserRepository;
import com.neil.easycron.plugin.bo.JobRunningResult;
import com.neil.easycron.plugin.bo.SingleMessage;
import com.neil.easycron.plugin.constant.JobRunningStatus;
import com.neil.easycron.service.JobLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private ListBoxRepository listBoxRepository;

    private Logger logger = LoggerFactory.getLogger(JobLogServiceImpl.class);

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
            logBo.setResolved(YorN.Y.equals(log.getResolved()));
            return logBo;
        }).collect(Collectors.toList());
        return new PageResult<>(request.getPage(), request.getSize(), jobLogs.getTotalPages(), jobLogs.getTotalElements(), logs);
    }

    @Override
    public void writeJobLog(Integer jobId, Calendar start, Calendar end, JobRunningResult runningResult, Integer userId) {
        JobLog jobLog = new JobLog();
        jobLog.setStartTime(start);
        jobLog.setEndTime(end);
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            user = userRepository.findById(1).orElse(null);
        }
        jobLog.setEntryUser(user);
        jobLog.setEntryDatetime(Calendar.getInstance());
        jobLog.setResolved(YorN.N);
        Job job = jobRepository.findById(jobId).orElse(null);
        if (job == null) {
            logger.error("任务不存在，可能已被删除");
            return;
        }
        jobLog.setJob(job);
        ListBox status = listBoxRepository.findByCatalogAndCode(ListCatalog.JOB_RUNNING_STATUS, runningResult.getRunningStatus().name());
        jobLog.setStatus(status);
        StringBuilder stringBuilder = new StringBuilder();
        runningResult.getMessage().forEach(m -> stringBuilder.append(Constant.FULL_DATE_FORMAT.format(m.getTime().getTime()) + "  " + m.getMessage() + "\r\n"));
        jobLog.setMessage(stringBuilder.toString());
        jobLogRepository.save(jobLog);
    }
}
