package com.github.wanjinzhong.easycron.bo.config;
import java.util.ArrayList;
import java.util.List;

import com.github.wanjinzhong.easycron.constant.enums.ConfigItemType;

public class ConfigItemBo {
    private String id;
    private String name;
    private Integer seq = 0;
    private ConfigItemType type;
    private Object value;
    private Double max;
    private Double min;
    private List<ConfigOption> optional = new ArrayList<>();

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

    public List<ConfigOption> getOptional() {
        return optional;
    }

    public void setOptional(List<ConfigOption> optional) {
        this.optional = optional;
    }

    public Double getMax() {
        return max;
    }

    public void setMax(Double max) {
        this.max = max;
    }

    public Double getMin() {
        return min;
    }

    public void setMin(Double min) {
        this.min = min;
    }
}
