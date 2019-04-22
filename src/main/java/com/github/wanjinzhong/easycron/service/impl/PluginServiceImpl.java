package com.github.wanjinzhong.easycron.service.impl;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import com.github.wanjinzhong.easycron.dao.entity.Plugin;
import com.github.wanjinzhong.easycron.dao.repository.PluginRepository;
import com.github.wanjinzhong.easycron.bo.PluginBo;
import com.github.wanjinzhong.easycron.service.PluginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
@Transactional
public class PluginServiceImpl implements PluginService {

    @Autowired
    private PluginRepository pluginRepository;

    @Override
    public List<PluginBo> getAllPlugins() {
        List<Plugin> plugins = pluginRepository.findAll();
        if (CollectionUtils.isEmpty(plugins)) {
            return new ArrayList<>();
        } else {
            return plugins.stream().map(plugin -> toPluginBo(plugin)).collect(Collectors.toList());
        }
    }

    private PluginBo toPluginBo(Plugin plugin) {
        PluginBo pluginBo = new PluginBo();
        pluginBo.setId(plugin.getId());
        pluginBo.setName(plugin.getName());
        pluginBo.setDesc(plugin.getDescription());
        pluginBo.setPicture(plugin.getPicture() == null ? null : plugin.getPicture().getId());
        return pluginBo;
    }
}
