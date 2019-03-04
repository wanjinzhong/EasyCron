package com.neil.easycron.bo.job;
public class JobBo {
    private Integer id;
    private String name;
    private String desc;
    private String cronReg;
    private String cronDesc;
    private boolean operable = false;
    private boolean editable = false;
    private boolean logVisiable = false;

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
}
