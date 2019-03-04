package com.neil.easycron.service.impl;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.neil.easycron.bo.NewJobBo;
import com.neil.easycron.bo.PageResult;
import com.neil.easycron.bo.config.ConfigFileBo;
import com.neil.easycron.bo.config.ConfigGroupBo;
import com.neil.easycron.bo.config.ConfigItemBo;
import com.neil.easycron.bo.job.JobBo;
import com.neil.easycron.bo.job.JobSearchRequest;
import com.neil.easycron.constant.enums.ConfigItemType;
import com.neil.easycron.constant.enums.JobStatus;
import com.neil.easycron.constant.enums.ListCatalog;
import com.neil.easycron.constant.enums.Role;
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
import com.neil.easycron.utils.CommonUtil;
import com.neil.easycron.utils.JobPluginUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
            throw new BizException("任务不存在");
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
            switch (element.getName()) {
                case "Group":
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
                    break;
                case "Item":
                    itemBos.add(getConfigItem(element));
                    break;
                case "Items":
                    ConfigItemBo configItemBo = new ConfigItemBo();
                    configItemBo.setId(element.attributeValue("ID"));
                    configItemBo.setType(ConfigItemType.multiple);
                    configItemBo.setSeq(Integer.valueOf(element.attributeValue("seq")));
                    configItemBo.setValue(element.attributeValue("value"));
                    for (Element option : element.elements()) {
                        if (!CollectionUtils.isEmpty(element.content())) {
                            configItemBo.getOptional().put(option.attributeValue("value"), option.getText());
                        }
                    }
                    itemBos.add(configItemBo);
                default:
                    break;
            }
        }
        groupBos.sort(Comparator.comparing(ConfigGroupBo::getSeq));
        itemBos.sort(Comparator.comparing(ConfigItemBo::getSeq));
        configs.setGroups(groupBos);
        configs.setItems(itemBos);
        return configs;
    }

    @Override
    public void saveConfigs(Integer jobId, Map<String, Object> configData) {
        Job job = jobRepository.findById(jobId).orElse(null);
        if (job == null) {
            throw new BizException("任务不存在");
        }
        File file = new File(job.getPosition() + "/" + job.getConfigFileName());
        if (!file.exists()) {
            throw new BizException("任务配置丢失");
        }
        EasyJobService service = JobPluginUtil.getJobService(job.getPlugin());
        Map<String, String> errors = service.validateConfigFile(configData);
        if (!CollectionUtils.isEmpty(errors)) {
            StringBuilder msg = new StringBuilder();
            for (Map.Entry<String, String> entry : errors.entrySet()) {
                msg.append(entry.getKey()).append(": ").append(entry.getValue()).append("<br/>");
            }
            throw new BizException(msg.toString());
        }
        saveConfigFile(file, configData);
    }

    @Override
    public PageResult<JobBo> searchJobs(JobSearchRequest request) {
        PageRequest pageRequest = PageRequest.of(request.getPage() - 1, request.getSize());
        String keyword = request.getKeyword();
        if (StringUtils.isBlank(keyword)) {
            keyword = "";
        }
        keyword = CommonUtil.convertMySqlParam(keyword);
        Page<Job> jobs = jobRepository.findByNameLike("%" + keyword + "%", pageRequest);
        return  new PageResult<>(request.getPage(), request.getSize(), jobs.getTotalPages(), jobs.getTotalElements(), jobToBos(jobs));
    }

    private List<JobBo> jobToBos(Page<Job> jobs) {
        Subject subject = SecurityUtils.getSubject();
        boolean editable = subject.hasRole(Role.CRON_EDITOR.name());
        boolean operable = subject.hasRole(Role.CRON_OPERATOR.name());
        return jobs.get().map(job -> {
            JobBo jobBo = new JobBo();
            jobBo.setId(job.getId());
            jobBo.setName(job.getName());
            jobBo.setDesc(job.getDesc());
            jobBo.setCronReg(job.getExpression());
            jobBo.setCronDesc(job.getExpression());
            jobBo.setLogVisiable(true);
            jobBo.setEditable(editable);
            jobBo.setOperable(operable);
            return jobBo;
        }).collect(Collectors.toList());

    }

    private void saveConfigFile(File file, Map<String, Object> configData) {
        try {
            Document document = new SAXReader().read(file);
            Element root = document.getRootElement();
            for (Element child : root.elements()) {
                if ("Item".equalsIgnoreCase(child.getName())) {
                    child.setText(getNotNullString(configData, child.attributeValue("ID")));
                } else if ("Group".equalsIgnoreCase(child.getName())) {
                    for (Element groupChild : child.elements()) {
                        groupChild.setText(getNotNullString(configData, groupChild.attributeValue("ID")));
                    }
                } else if ("Items".equalsIgnoreCase(child.getName())) {
                    child.addAttribute("value", getNotNullString(configData, child.attributeValue("ID")));
                }
            }
            XMLWriter xmlWriter = new XMLWriter(new FileOutputStream(file), OutputFormat.createPrettyPrint());
            xmlWriter.write(document);
            xmlWriter.close();
        } catch (DocumentException | IOException e) {
            throw new BizException("无法读取配置文件");
        }
    }

    private String getNotNullString(Map<String, Object> map, String key) {
        Object value = map.get(key);
        return value == null ? "" : value.toString();
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
