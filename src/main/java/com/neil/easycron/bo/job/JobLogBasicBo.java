package com.neil.easycron.bo.job;
import java.util.Calendar;

import com.neil.easycron.plugin.constant.JobRunningStatus;

public class JobLogBasicBo {

    private Long id;

    private JobRunningStatus status;

    private Boolean resolved;

    private Calendar startTime;

    private Calendar endTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public JobRunningStatus getStatus() {
        return status;
    }

    public void setStatus(JobRunningStatus status) {
        this.status = status;
    }

    public Boolean getResolved() {
        return resolved;
    }

    public void setResolved(Boolean resolved) {
        this.resolved = resolved;
    }

    public Calendar getStartTime() {
        return startTime;
    }

    public void setStartTime(Calendar startTime) {
        this.startTime = startTime;
    }

    public Calendar getEndTime() {
        return endTime;
    }

    public void setEndTime(Calendar endTime) {
        this.endTime = endTime;
    }
}
