package com.neil.easycron.bo.job;
public class JobLogBo extends JobLogBasicBo {

    private Double during;
    private String message;

    public Double getDuring() {
        return during;
    }

    public void setDuring(Double during) {
        this.during = during;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
