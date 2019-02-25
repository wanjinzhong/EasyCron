package com.neil.easycron.bo.config;
import java.util.List;

public class ConfigFileBo {
    private List<ConfigGroupBo> groups;
    private List<ConfigItemBo> items;

    public List<ConfigGroupBo> getGroups() {
        return groups;
    }

    public void setGroups(List<ConfigGroupBo> groups) {
        this.groups = groups;
    }

    public List<ConfigItemBo> getItems() {
        return items;
    }

    public void setItems(List<ConfigItemBo> items) {
        this.items = items;
    }
}
