package com.github.wanjinzhong.easycron.bo.job;
import com.github.wanjinzhong.easycron.constant.enums.JobStatus;

public class JobBo {
    private Integer id;
    private String name;
    private String desc;
    private String cronReg;
    private String cronDesc;
    private Long count;
    private boolean operable = false;
    private boolean editable = false;
    private boolean logVisiable = false;
    private Long pluginPicId;
    private String plugin;
    private JobStatus status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCronReg() {
        return cronReg;
    }

    public void setCronReg(String cronReg) {
        this.cronReg = cronReg;
    }

    public String getCronDesc() {
        return cronDesc;
    }

    public void setCronDesc(String cronDesc) {
        this.cronDesc = cronDesc;
    }

    public boolean isOperable() {
        return operable;
    }

    public void setOperable(boolean operable) {
        this.operable = operable;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public boolean isLogVisiable() {
        return logVisiable;
    }

    public void setLogVisiable(boolean logVisiable) {
        this.logVisiable = logVisiable;
    }

    public JobStatus getStatus() {
        return status;
    }

    public void setStatus(JobStatus status) {
        this.status = status;
    }

    public String getPlugin() {
        return plugin;
    }

    public void setPlugin(String plugin) {
        this.plugin = plugin;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public Long getPluginPicId() {
        return pluginPicId;
    }

    public void setPluginPicId(Long pluginPicId) {
        this.pluginPicId = pluginPicId;
    }
}
