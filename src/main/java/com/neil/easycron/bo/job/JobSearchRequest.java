package com.neil.easycron.bo.job;
import java.util.List;

public class JobSearchRequest {
    private String keyword;
    private List<Integer> status;
    private List<Integer> plugins;
    private int page;
    private int size;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
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

    public List<Integer> getStatus() {
        return status;
    }

    public void setStatus(List<Integer> status) {
        this.status = status;
    }

    public List<Integer> getPlugins() {
        return plugins;
    }

    public void setPlugins(List<Integer> plugins) {
        this.plugins = plugins;
    }
}
