package com.neil.easycron.bo.job;
import java.util.Calendar;

public class JobLogBo {
    private Double during;

    private Boolean resolved;

    private String message;

    private Calendar startTime;

    private Calendar endTime;

    public Double getDuring() {
        return during;
    }

    public void setDuring(Double during) {
        this.during = during;
    }

    public Boolean getResolved() {
        return resolved;
    }

    public void setResolved(Boolean resolved) {
        this.resolved = resolved;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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
