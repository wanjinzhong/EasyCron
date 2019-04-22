package com.github.wanjinzhong.easycron.bo;
import java.util.List;

public class PageResult<T> {
    private int page;

    private int size;

    private int totalPages;

    private long totalCount;

    private List<T> data;

    public PageResult(int page, int size, int totalPages, long totalCount, List<T> data) {
        this.page = page;
        this.size = size;
        this.totalPages = totalPages;
        this.totalCount = totalCount;
        this.data = data;
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

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
