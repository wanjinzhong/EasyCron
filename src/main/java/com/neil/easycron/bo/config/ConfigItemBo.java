package com.neil.easycron.bo.config;
import java.util.HashMap;
import java.util.Map;

import com.neil.easycron.constant.enums.ConfigItemType;

public class ConfigItemBo {
    private String id;
    private String name;
    private Integer seq = 0;
    private ConfigItemType type;
    private Object value;
    private Map<String, String> optional = new HashMap<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    public ConfigItemType getType() {
        return type;
    }

    public void setType(ConfigItemType type) {
        this.type = type;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Map<String, String> getOptional() {
        return optional;
    }

    public void setOptional(Map<String, String> optional) {
        this.optional = optional;
    }
}
