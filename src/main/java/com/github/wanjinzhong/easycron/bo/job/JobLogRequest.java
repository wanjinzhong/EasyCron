package com.github.wanjinzhong.easycron.bo.job;
public class JobLogRequest {
    private Integer jobId;
    private boolean asc;
    private int page;
    private int size;
    private Long latestId;

    public Integer getJobId() {
        return jobId;
    }

    public void setJobId(Integer jobId) {
        this.jobId = jobId;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public boolean isAsc() {
        return asc;
    }

    public void setAsc(boolean asc) {
        this.asc = asc;
    }

    public Long getLatestId() {
        return latestId;
    }

    public void setLatestId(Long latestId) {
        this.latestId = latestId;
    }
}
