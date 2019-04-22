package com.github.wanjinzhong.easycron.bo.role;
import com.github.wanjinzhong.easycron.constant.enums.RoleCode;

public class BasicRoleBo {
    private Integer id;
    private RoleCode code;
    private String name;
    private String desc;
    private Boolean deletable;

    public BasicRoleBo() {
    }

    public BasicRoleBo(Integer id, RoleCode code, String name, String desc, Boolean deletable) {
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

    public RoleCode getCode() {
        return code;
    }

    public void setCode(RoleCode code) {
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
