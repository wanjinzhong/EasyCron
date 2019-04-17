package com.neil.easycron.bo.role;
public class BasicRoleBo {
    private Integer id;
    private String code;
    private String name;
    private String desc;
    private Boolean deletable;

    public BasicRoleBo() {
    }

    public BasicRoleBo(Integer id, String code, String name, String desc, Boolean deletable) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.desc = desc;
        this.deletable = deletable;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public Boolean getDeletable() {
        return deletable;
    }

    public void setDeletable(Boolean deletable) {
        this.deletable = deletable;
    }
}
