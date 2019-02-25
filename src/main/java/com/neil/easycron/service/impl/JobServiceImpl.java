package com.neil.easycron.service.impl;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;

import com.neil.easycron.bo.NewJobBo;
import com.neil.easycron.bo.config.ConfigFileBo;
import com.neil.easycron.bo.config.ConfigGroupBo;
import com.neil.easycron.bo.config.ConfigItemBo;
import com.neil.easycron.constant.enums.ConfigItemType;
import com.neil.easycron.constant.enums.JobStatus;
import com.neil.easycron.constant.enums.ListCatalog;
import com.neil.easycron.dao.entity.Job;
import com.neil.easycron.dao.entity.ListBox;
import com.neil.easycron.dao.entity.Plugin;
import com.neil.easycron.dao.entity.User;
import com.neil.easycron.dao.repository.JobRepository;
import com.neil.easycron.dao.repository.ListBoxRepository;
import com.neil.easycron.dao.repository.PluginRepository;
import com.neil.easycron.exception.BizException;
import com.neil.easycron.plugin.service.EasyJobService;
import com.neil.easycron.service.JobService;
import com.neil.easycron.utils.JobPluginUtil;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
@Transactional
public class JobServiceImpl implements JobService {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private ListBoxRepository listBoxRepository;

    @Autowired
    private PluginRepository pluginRepository;

    @Override
    public void createNewJob(NewJobBo newJobBo) {
        if (StringUtils.isBlank(newJobBo.getName())) {
            throw new BizException("任务名称不能为空");
        }
        Job job = jobRepository.findByName(newJobBo.getName());
        if (job != null) {
            throw new BizException("任务名称已存在");
        }
        String rootPath = System.getProperty("user.home") + "/.easycron";
        File jobHome = new File(rootPath + "/jobs/" + newJobBo.getName());
        if (jobHome.exists()) {
            throw new BizException("任务名称已存在");
        }
        Plugin plugin = pluginRepository.findById(newJobBo.getPluginId()).orElse(null);
        if (plugin == null) {
            throw new BizException("插件不存在");
        }
        File pluginFile = new File(plugin.getPackageLocation());
        if (!pluginFile.exists()) {
            throw new BizException("插件包丢失");
        }
        EasyJobService easyJobService = JobPluginUtil.getJobService(plugin);
        InputStream inputStream;
        try {
            inputStream = easyJobService.getConfigFile();
        } catch (Exception e) {
            throw new BizException("无法获取插件包配置", e);
        }
        String configFileName = "config_" + plugin.getId() + ".xml";
        job = new Job();
        job.setName(newJobBo.getName());
        job.setPosition(jobHome.getAbsolutePath());
        job.setDesc(newJobBo.getDesc());
        job.setPlugin(plugin);
        job.setConfigFileName(configFileName);
        User user = new User();
        user.setId(1);
        job.setEntryUser(user);
        job.setEntryDatetime(Calendar.getInstance());
        ListBox listBox = listBoxRepository.findByCatalogAndCode(ListCatalog.JOB_STATUS, JobStatus.WAITING_CONFIG.name());
        job.setStatus(listBox);
        jobRepository.save(job);
        jobHome.mkdirs();
        File configFile = new File(jobHome.getAbsolutePath() + "/" + configFileName);
        byte[] buffer;
        try {
            buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            new FileOutputStream(configFile).write(buffer);
        } catch (IOException e) {
            if (jobHome.exists()) {
                jobHome.delete();
            }
            throw new BizException("读取配置文件失败", e);
        }
    }

    @Override
    public ConfigFileBo getConfigs(Integer jobId) {
        Job job = jobRepository.findById(jobId).orElse(null);
        if (job == null) {
            throw new BizException("任务名称不存在");
        }
        File configFile = new File(job.getPosition() + "/" + job.getConfigFileName());
        if (!configFile.exists()) {
            throw new BizException("任务配置丢失");
        }
        SAXReader saxReader = new SAXReader();
        Document document;
        try {
            document = saxReader.read(configFile);
        } catch (DocumentException e) {
            throw new BizException("无法读取配置文件", e);
        }
        ConfigFileBo configs = new ConfigFileBo();
        List<ConfigGroupBo> groupBos = new ArrayList<>();
        List<ConfigItemBo> itemBos = new ArrayList<>();
        Element root = document.getRootElement();
        for (Element element : root.elements()) {
            if ("Group".equals(element.getName())) {
                ConfigGroupBo groupBo = new ConfigGroupBo();
                groupBo.setGroupName(element.attributeValue("name"));
                if (StringUtils.isNotBlank(element.attributeValue("seq"))) {
                    groupBo.setSeq(Integer.valueOf(element.attributeValue("seq")));
                }
                for (Element item : element.elements()) {
                    groupBo.getItems().add(getConfigItem(item));
                }
                groupBo.getItems().sort(Comparator.comparing(ConfigItemBo::getSeq));
                groupBos.add(groupBo);
            } else {
                itemBos.add(getConfigItem(element));
            }
        }
        groupBos.sort(Comparator.comparing(ConfigGroupBo::getSeq));
        itemBos.sort(Comparator.comparing(ConfigItemBo::getSeq));
        configs.setGroups(groupBos);
        configs.setItems(itemBos);
        return configs;
    }

    private ConfigItemBo getConfigItem(Element element) {
        ConfigItemBo itemBo = new ConfigItemBo();
        itemBo.setId(element.attributeValue("ID"));
        itemBo.setName(element.attributeValue("name"));
        if (StringUtils.isNotBlank(element.attributeValue("type"))) {
            itemBo.setType(ConfigItemType.valueOf(element.attributeValue("type")));
        }
        if (!CollectionUtils.isEmpty(element.content())) {
            itemBo.setValue(element.content().get(0).getText());
        }
        if (StringUtils.isNotBlank(element.attributeValue("seq"))) {
            itemBo.setSeq(Integer.valueOf(element.attributeValue("seq")));
        }
        return itemBo;
    }

}
