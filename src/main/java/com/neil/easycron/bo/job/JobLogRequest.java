package com.neil.easycron.bo.job;
import java.util.ArrayList;
import java.util.List;

public class JobLogRequest {
    private Integer jobId;
    private List<Integer> status = new ArrayList<>();
    private boolean onlyResolved = false;
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

    public List<Integer> getStatus() {
        return status;
    }

    public void setStatus(List<Integer> status) {
        this.status = status;
    }

    public boolean isOnlyResolved() {
        return onlyResolved;
    }

    public void setOnlyResolved(boolean onlyResolved) {
        this.onlyResolved = onlyResolved;
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
