package com.github.wanjinzhong.easycron.dao.entity;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name = "job_log",indexes = {
    @Index(columnList = "job_id", name = "job_log_I1"),
    @Index(columnList = "status", name = "job_log_I2")
})
public class JobLog implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;

    @ManyToOne
    @JoinColumn(name = "status", nullable = false)
    private ListBox status;

    @Column(name = "message")
    @Lob
    private String message;

    @Column(name = "start_time")
    private Calendar startTime;

    @Column(name = "end_time")
    private Calendar endTime;

    @Column(name = "entry_datetime", nullable = false)
    private Calendar entryDatetime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
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

    public ListBox getStatus() {
        return status;
    }

    public void setStatus(ListBox status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Calendar getEntryDatetime() {
        return entryDatetime;
    }

    public void setEntryDatetime(Calendar entryDatetime) {
        this.entryDatetime = entryDatetime;
    }
}
