package com.neil.easycron.dao.entity;

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
    @Index(columnList = "status", name = "job_log_I2"),
    @Index(columnList = "entry_id", name = "job_log_I3")
})
public class JobLog implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "job_id")
    private Job job;

    @Column(name = "during")
    private Long during;

    @ManyToOne
    @JoinColumn(name = "status")
    private ListBox status;

    @Column(name = "resolved")
    private Boolean resolved;

    @Column(name = "message")
    @Lob
    private String message;

    @ManyToOne
    @JoinColumn(name = "entry_id")
    private User entryUser;

    @Column(name = "entry_datetime")
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

    public Long getDuring() {
        return during;
    }

    public void setDuring(Long during) {
        this.during = during;
    }

    public ListBox getStatus() {
        return status;
    }

    public void setStatus(ListBox status) {
        this.status = status;
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
