package com.github.wanjinzhong.easycron.bo.config;
import java.util.List;

public class ConfigBo {
    private List<ConfigGroupBo> configs;

    public ConfigBo() {
    }

    public ConfigBo(List<ConfigGroupBo> configs) {
        this.configs = configs;
    }

    public List<ConfigGroupBo> getConfigs() {
        return configs;
    }

    public void setConfigs(List<ConfigGroupBo> configs) {
        this.configs = configs;
    }
}
