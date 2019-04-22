package com.github.wanjinzhong.easycron.service.impl;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

import com.github.wanjinzhong.easycron.bo.PageResult;
import com.github.wanjinzhong.easycron.bo.job.JobLogBasicBo;
import com.github.wanjinzhong.easycron.bo.job.JobLogBo;
import com.github.wanjinzhong.easycron.bo.job.JobLogRequest;
import com.github.wanjinzhong.easycron.constant.Constant;
import com.github.wanjinzhong.easycron.constant.enums.ListCatalog;
import com.github.wanjinzhong.easycron.dao.entity.Job;
import com.github.wanjinzhong.easycron.dao.entity.JobLog;
import com.github.wanjinzhong.easycron.dao.entity.ListBox;
import com.github.wanjinzhong.easycron.dao.repository.JobLogRepository;
import com.github.wanjinzhong.easycron.dao.repository.JobRepository;
import com.github.wanjinzhong.easycron.dao.repository.ListBoxRepository;
import com.github.wanjinzhong.easycron.dao.repository.UserRepository;
import com.github.wanjinzhong.easycron.exception.BizException;
import com.github.wanjinzhong.easycron.service.JobLogService;
import com.github.wanjinzhong.easycronbase.bo.JobRunningResult;
import com.github.wanjinzhong.easycronbase.constant.JobRunningStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public PageResult<JobLogBasicBo> getJobLogs(JobLogRequest request) {
        Page<JobLog> jobLogs;
        PageRequest pageRequest = PageRequest.of(request.getPage() - 1, request.getSize());
        if (request.isAsc()) {
            jobLogs = jobLogRepository.findByJobIdOrderByStartTimeAsc(request.getJobId(), pageRequest);
        } else {
            jobLogs = jobLogRepository.findByJobIdOrderByStartTimeDesc(request.getJobId(), pageRequest);
        }
        List<JobLogBasicBo> logs = jobLogs.get().map(log -> toJobLogBasicBo(log)).collect(Collectors.toList());
        return new PageResult<>(request.getPage(), request.getSize(), jobLogs.getTotalPages(), jobLogs.getTotalElements(), logs);
    }

    private JobLogBasicBo toJobLogBasicBo(JobLog log) {
        JobLogBasicBo logBo = new JobLogBasicBo();
        logBo.setId(log.getId());
        logBo.setStartTime(log.getStartTime());
        logBo.setEndTime(log.getEndTime());
        logBo.setStatus(JobRunningStatus.valueOf(log.getStatus().getCode()));
        return logBo;
    }

    private JobLogBo toJobLogBo(JobLog log) {
        JobLogBo logBo = new JobLogBo();
        logBo.setId(log.getId());
        logBo.setStartTime(log.getStartTime());
        logBo.setEndTime(log.getEndTime());
        logBo.setMessage(log.getMessage());
        logBo.setStatus(JobRunningStatus.valueOf(log.getStatus().getCode()));
        return logBo;
    }

    @Override
    public void writeJobLog(Integer jobId, Calendar start, Calendar end, JobRunningResult runningResult, Integer userId) {
        JobLog jobLog = new JobLog();
        jobLog.setStartTime(start);
        jobLog.setEndTime(end);
        jobLog.setEntryDatetime(Calendar.getInstance());
        Job job = jobRepository.findById(jobId).orElse(null);
        if (job == null) {
            logger.error("任务不存在，可能已被删除");
            return;
        }
        jobLog.setJob(job);
        ListBox status = listBoxRepository.findByCatalogAndCode(ListCatalog.JOB_RUNNING_STATUS, runningResult.getRunningStatus().name());
        jobLog.setStatus(status);
        StringBuilder stringBuilder = new StringBuilder();
        runningResult.getMessage().forEach(m -> {
            stringBuilder.append(Constant.FULL_DATE_FORMAT.format(m.getTime().getTime()))
                         .append("  ")
                         .append(m.getLevel().name())
                         .append("\t")
                         .append(m.getMessage())
                         .append("\r\n");
        });
        jobLog.setMessage(stringBuilder.toString());
        jobLogRepository.save(jobLog);
    }

    @Override
    public JobLogBo getJobLogDetail(Long logId) {
        JobLog log = jobLogRepository.findById(logId).orElse(null);
        if (log == null) {
            throw new BizException("日志不存在，可能已被清理");
        }
        return toJobLogBo(log);
    }

    @Override
    public void cleanLog(Integer jobId) {
        jobLogRepository.deleteByJobId(jobId);
    }
}
