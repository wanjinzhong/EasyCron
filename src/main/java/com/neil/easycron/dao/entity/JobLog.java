package com.neil.easycron.dao.entity;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.neil.easycron.constant.enums.YorN;

@Entity
@Table(name = "job_log",indexes = {
    @Index(columnList = "job_id", name = "job_log_I1"),
    @Index(columnList = "status", name = "job_log_I2"),
    @Index(columnList = "entry_id", name = "job_log_I3")
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

    @Column(name = "resolved", length = 1)
    @Enumerated(EnumType.STRING)
    private YorN resolved;

    @Column(name = "message")
    @Lob
    private String message;

    @Column(name = "start_time")
    private Calendar startTime;

    @Column(name = "end_time")
    private Calendar endTime;

    @ManyToOne
    @JoinColumn(name = "entry_id", nullable = false)
    private User entryUser;

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

    public YorN getResolved() {
        return resolved;
    }

    public void setResolved(YorN resolved) {
        this.resolved = resolved;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User getEntryUser() {
        return entryUser;
    }

    public void setEntryUser(User entryUser) {
        this.entryUser = entryUser;
    }

    public Calendar getEntryDatetime() {
        return entryDatetime;
    }

    public void setEntryDatetime(Calendar entryDatetime) {
        this.entryDatetime = entryDatetime;
    }
}
