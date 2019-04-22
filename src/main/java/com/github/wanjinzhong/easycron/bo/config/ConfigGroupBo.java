package com.github.wanjinzhong.easycron.bo.config;
import java.util.ArrayList;
import java.util.List;

public class ConfigGroupBo {
    private String groupName;
    private Integer seq;
    private List<ConfigItemBo> items = new ArrayList<>();

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    public List<ConfigItemBo> getItems() {
        return items;
    }

    public void setItems(List<ConfigItemBo> items) {
        this.items = items;
    }
}
