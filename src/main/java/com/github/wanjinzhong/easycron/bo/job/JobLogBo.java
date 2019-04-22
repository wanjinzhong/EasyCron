package com.github.wanjinzhong.easycron.bo.job;
public class JobLogBo extends JobLogBasicBo {

    private String message;

    public Double getDuring() {
        if (getStartTime() != null && getEndTime() != null) {
            return (getEndTime().getTimeInMillis() - getStartTime().getTimeInMillis()) * 1.0 / 1000;
        }
        return 0d;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
