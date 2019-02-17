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
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "job",
       indexes = {
           @Index(columnList = "plugin_id", name = "job_I1"),
           @Index(columnList = "expression", name = "job_I2"),
           @Index(columnList = "status", name = "job_I3"),
           @Index(columnList = "entry_id", name = "job_I4")
})
public class Job implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "plugin_id")
    private Plugin plugin;

    @Column(name = "name", length = 100)
    private String name;

    @Column(name = "position", length = 1024)
    private String position;

    @Column(name = "expression", length = 100)
    private String expression;

    @Column(name = "running_count")
    private Long runningCount;

    @ManyToOne
    @JoinColumn(name = "status")
    private ListBox status;

    @ManyToOne
    @JoinColumn(name = "entry_id")
    private User entryUser;

    @Column(name = "entry_datetime")
    private Calendar entryDatetime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Plugin getPlugin() {
        return plugin;
    }

    public void setPlugin(Plugin plugin) {
        this.plugin = plugin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public Long getRunningCount() {
        return runningCount;
    }

    public void setRunningCount(Long runningCount) {
        this.runningCount = runningCount;
    }

    public ListBox getStatus() {
        return status;
    }

    public void setStatus(ListBox status) {
        this.status = status;
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
