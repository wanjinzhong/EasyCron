package com.neil.easycron.service.impl;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.neil.easycron.bo.PageResult;
import com.neil.easycron.bo.config.ConfigBo;
import com.neil.easycron.bo.config.ConfigGroupBo;
import com.neil.easycron.bo.config.ConfigItemBo;
import com.neil.easycron.bo.config.ConfigOption;
import com.neil.easycron.bo.job.JobBo;
import com.neil.easycron.bo.job.JobSearchRequest;
import com.neil.easycron.bo.job.NewJobBo;
import com.neil.easycron.bo.user.UserInfo;
import com.neil.easycron.constant.Constant;
import com.neil.easycron.constant.enums.ConfigItemType;
import com.neil.easycron.constant.enums.JobStatus;
import com.neil.easycron.constant.enums.ListCatalog;
import com.neil.easycron.constant.enums.RoleCode;
import com.neil.easycron.dao.entity.Job;
import com.neil.easycron.dao.entity.ListBox;
import com.neil.easycron.dao.entity.Plugin;
import com.neil.easycron.dao.entity.User;
import com.neil.easycron.dao.repository.JobRepository;
import com.neil.easycron.dao.repository.ListBoxRepository;
import com.neil.easycron.dao.repository.PluginRepository;
import com.neil.easycron.exception.BizException;
import com.neil.easycron.plugin.service.EasyJobService;
import com.neil.easycron.service.CronJob;
import com.neil.easycron.service.JobLogService;
import com.neil.easycron.service.JobService;
import com.neil.easycron.service.UserService;
import com.neil.easycron.utils.JobPluginUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.quartz.CronExpression;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
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

    @Autowired
    private JobLogService jobLogService;

    @Autowired
    private Scheduler scheduler;

    @Autowired
    private UserService userService;

    private Logger logger = LoggerFactory.getLogger(JobServiceImpl.class);

    @Override
    public void createNewJob(NewJobBo newJobBo) {
        if (StringUtils.isBlank(newJobBo.getName())) {
            throw new BizException("任务名称不能为空");
        }
        if (!CronExpression.isValidExpression(newJobBo.getCron())) {
            throw new BizException("Cron表达式不正确");
        }
        if (newJobBo.getPluginId() == null) {
            throw new BizException("插件不能为空");
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
        Job job = new Job();
        job.setName(newJobBo.getName());
        job.setDesc(newJobBo.getDesc());
        job.setExpression(newJobBo.getCron());
        job.setPosition("");
        job.setPlugin(plugin);
        job.setConfigFileName(configFileName);
        User user = new User();
        user.setId(1);
        job.setEntryUser(user);
        job.setEntryDatetime(Calendar.getInstance());
        ListBox listBox = listBoxRepository.findByCatalogAndCode(ListCatalog.JOB_STATUS, JobStatus.WAITING_CONFIG.name());
        job.setStatus(listBox);
        jobRepository.save(job);
        File jobHome = new File(Constant.ResourcePath.JOBS_FULL + job.getId());
        job.setPosition(jobHome.getAbsolutePath());
        jobRepository.save(job);
        jobHome.mkdirs();
        File configFile = new File(jobHome.getAbsolutePath() + File.pathSeparator + configFileName);
        byte[] buffer;
        try {
            buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            FileOutputStream outputStream = new FileOutputStream(configFile);
            outputStream.write(buffer);
            outputStream.close();
            inputStream.close();
        } catch (IOException e) {
            FileUtils.deleteQuietly(jobHome);
            throw new BizException("读取配置文件失败", e);
        }
    }

    @Override
    public Map<String, Object> getConfigMap(Integer jobId) {
        List<ConfigGroupBo> configGroupBos = getConfigs(jobId);
        Map<String, Object> configs = new HashMap<>();
        configGroupBos.forEach(group -> group.getItems().forEach(item -> configs.put(item.getId(), item.getValue())));
        return configs;
    }

    @Override
    public List<ConfigGroupBo> getConfigs(Integer jobId) {
        Job job = jobRepository.findById(jobId).orElse(null);
        if (job == null) {
            throw new BizException("任务不存在");
        }
        File configFile = new File(job.getPosition() + File.pathSeparator + job.getConfigFileName());
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
                        if ("Items".equalsIgnoreCase(item.getName())) {
                            groupBo.getItems().add(getConfigItems(item));
                        } else {
                            groupBo.getItems().add(getConfigItem(item));
                        }
                    }
                    groupBo.getItems().sort(Comparator.comparing(ConfigItemBo::getSeq));
                    groupBos.add(groupBo);
                    break;
                case "Item":
                    itemBos.add(getConfigItem(element));
                    break;
                case "Items":
                    itemBos.add(getConfigItems(element));
                default:
                    break;
            }
        }
        groupBos.sort(Comparator.comparing(ConfigGroupBo::getSeq));
        itemBos.sort(Comparator.comparing(ConfigItemBo::getSeq));
        ConfigGroupBo others = new ConfigGroupBo();
        others.setGroupName("其他");
        others.setItems(itemBos);
        groupBos.add(others);
        ConfigGroupBo basic = getBasicConfigGroup(job);
        groupBos.add(0, basic);
        return groupBos;
    }

    private ConfigItemBo getConfigItems(Element element) {
        ConfigItemBo configItemBo = new ConfigItemBo();
        configItemBo.setId(element.attributeValue("ID"));
        configItemBo.setName(element.attributeValue("name"));
        configItemBo.setSeq(Integer.valueOf(element.attributeValue("seq")));
        configItemBo.setType(ConfigItemType.valueOf(element.attributeValue("type")));
        String value = element.attributeValue("value");
        if (StringUtils.isBlank(value)) {
            value = "";
        }
        if (ConfigItemType.multiple.equals(configItemBo.getType())) {
            configItemBo.setValue(value.split(","));
        } else {
            configItemBo.setValue(value);
        }
        for (Element option : element.elements()) {
            if (!CollectionUtils.isEmpty(element.content())) {
                configItemBo.getOptional().add(new ConfigOption(option.attributeValue("value"), option.getText()));
            }
        }
        return configItemBo;
    }

    private ConfigGroupBo getBasicConfigGroup(Job job) {
        ConfigGroupBo basic = new ConfigGroupBo();
        basic.setGroupName(Constant.BASIC_GROUP_NAME);
        basic.setSeq(0);
        List<ConfigItemBo> items = new ArrayList<>();
        ConfigItemBo item = new ConfigItemBo();
        item.setId("name");
        item.setName("名称");
        item.setValue(job.getName());
        item.setSeq(0);
        item.setType(ConfigItemType.string);
        items.add(item);
        item = new ConfigItemBo();
        item.setId("cron");
        item.setName("表达式");
        item.setValue(job.getExpression());
        item.setSeq(1);
        item.setType(ConfigItemType.string);
        items.add(item);
        item = new ConfigItemBo();
        item.setId("desc");
        item.setName("描述");
        item.setValue(job.getDesc());
        item.setSeq(2);
        item.setType(ConfigItemType.text);
        items.add(item);
        basic.setItems(items);
        return basic;
    }

    @Override
    public void saveConfigs(Integer jobId, ConfigBo configData) {
        Job job = jobRepository.findById(jobId).orElse(null);
        if (job == null) {
            throw new BizException("任务不存在");
        }
        File file = new File(job.getPosition() + File.pathSeparator + job.getConfigFileName());
        if (!file.exists()) {
            throw new BizException("任务配置丢失");
        }
        ConfigGroupBo basic = null;
        Iterator<ConfigGroupBo> it = configData.getConfigs().iterator();
        while (it.hasNext()) {
            ConfigGroupBo groupBo = it.next();
            if (Constant.BASIC_GROUP_NAME.equals(groupBo.getGroupName())) {
                basic = groupBo;
                it.remove();
                break;
            }
        }
        if (basic == null) {
            throw new BizException("缺失基本配置");
        }
        String cron = basic.getItems().stream().filter(item -> "cron".equals(item.getId())).findFirst().orElse(new ConfigItemBo()).getValue().toString();
        if (!CronExpression.isValidExpression(cron)) {
            throw new BizException("表达式错误");
        }
        Map<String, Object> configMap = configBoToMap(configData);
        EasyJobService service = JobPluginUtil.getJobService(job.getPlugin());
        Map<String, String> errors = service.validateConfigFile(configMap);
        if (!CollectionUtils.isEmpty(errors)) {
            StringBuilder msg = new StringBuilder();
            for (Map.Entry<String, String> entry : errors.entrySet()) {
                msg.append(entry.getKey()).append(": ").append(entry.getValue()).append("<br/>");
            }
            throw new BizException(msg.toString());
        }
        job.setName(basic.getItems().stream().filter(item -> "name".equals(item.getId())).findFirst().orElse(new ConfigItemBo()).getValue().toString());
        job.setExpression(cron);
        job.setDesc(basic.getItems().stream().filter(item -> "desc".equals(item.getId())).findFirst().orElse(new ConfigItemBo()).getValue().toString());
        if (JobStatus.WAITING_CONFIG.name().equalsIgnoreCase(job.getStatus().getCode())) {
            ListBox stopped = listBoxRepository.findByCatalogAndCode(ListCatalog.JOB_STATUS, JobStatus.STOPPED.name());
            job.setStatus(stopped);
        }
        jobRepository.save(job);
        saveConfigFile(file, configMap);
        if (JobStatus.RUNNING.name().equals(job.getStatus().getCode())) {
            restartJob(jobId);
        }
    }

    @Override
    public void restartJob(Integer jobId) {
        stopJob(jobId);
        runJob(jobId);
    }

    private Map<String, Object> configBoToMap(ConfigBo configData) {
        Map<String, Object> configMap = new HashMap<>();
        for (ConfigGroupBo groupBo : configData.getConfigs()) {
            for (ConfigItemBo item : groupBo.getItems()) {
                configMap.put(item.getId(), item.getValue());
            }
        }
        return configMap;
    }

    @Override
    public PageResult<JobBo> searchJobs(JobSearchRequest request) {
        PageRequest pageRequest = PageRequest.of(request.getPage() - 1, request.getSize());
        Specification<Job> specification = new JobSpecification(request);
        Page<Job> jobs = jobRepository.findAll(specification, pageRequest);
        return new PageResult<>(request.getPage(), request.getSize(), jobs.getTotalPages(), jobs.getTotalElements(), jobToBos(jobs));
    }

    @Override
    public void deleteJob(Integer jobId) {
        stopJob(jobId);
        jobRepository.deleteById(jobId);
        File jobHome = new File(Constant.ResourcePath.JOBS_FULL + jobId);
        if (jobHome.exists() && !FileUtils.deleteQuietly(jobHome)) {
            throw new BizException("无法删除配置文件");
        }
    }

    @Override
    public void runJob(Integer jobId) {
        Job job = jobRepository.findById(jobId).orElse(null);
        if (job == null) {
            throw new BizException("任务不存在");
        }

        JobDataMap dataMap = new JobDataMap();
        dataMap.put(Constant.JobParam.JOB, job);

        Integer userId = 1;
        try {
            UserInfo userInfo = userService.getUserInfo();
            if (userInfo != null && userInfo.getId() != null) {
                userId = userInfo.getId();
            }
        }catch (Exception e) {
            userId = 1;
        }
        dataMap.put(Constant.JobParam.ENTRY_USER, userId);
        try {
            JobDetail jobDetail = JobBuilder.newJob(CronJob.class).withIdentity("JOB-" + job.getId(), "GROUP-" + job.getId()).setJobData(dataMap).build();
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job.getExpression());
            CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity("JOB-" + job.getId(), "GROUP-" + job.getId()).withSchedule(scheduleBuilder).build();
            scheduler.scheduleJob(jobDetail, cronTrigger);
            ListBox running = listBoxRepository.findByCatalogAndCode(ListCatalog.JOB_STATUS, JobStatus.RUNNING.name());
            job.setStatus(running);
        } catch (SchedulerException e) {
            logger.error("ERROR: " + e.getMessage(), e);
            ListBox stopped = listBoxRepository.findByCatalogAndCode(ListCatalog.JOB_STATUS, JobStatus.STOPPED.name());
            job.setStatus(stopped);
        } finally {
            jobRepository.save(job);
        }
    }

    @Override
    public void stopJob(Integer jobId) {
        Job job = jobRepository.findById(jobId).orElse(null);
        if (job == null) {
            throw new BizException("任务不存在");
        }
        try {
            scheduler.deleteJob(new JobKey("JOB-" + job.getId(), "GROUP-" + job.getId()));
            ListBox stopped = listBoxRepository.findByCatalogAndCode(ListCatalog.JOB_STATUS, JobStatus.STOPPED.name());
            job.setStatus(stopped);
            jobRepository.save(job);
        } catch (SchedulerException e) {
            throw new BizException("无法停止任务：" + e.getMessage(), e);
        }
    }

    private List<JobBo> jobToBos(Page<Job> jobs) {
        Subject subject = SecurityUtils.getSubject();
        boolean editable = subject.hasRole(RoleCode.CRON_EDITOR.name());
        boolean operable = subject.hasRole(RoleCode.CRON_OPERATOR.name());
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
            jobBo.setStatus(JobStatus.valueOf(job.getStatus().getCode()));
            jobBo.setPlugin(job.getPlugin().getName());
            jobBo.setPluginPicId(job.getPlugin().getPicture() == null ? null : job.getPlugin().getPicture().getId());
            jobBo.setCount(job.getRunningCount());
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
                        if ("Items".equalsIgnoreCase(groupChild.getName())) {
                            String value = getNotNullString(configData, groupChild.attributeValue("ID"));
                            if (ConfigItemType.password.name().equalsIgnoreCase(groupChild.attributeValue("type"))) {
                                if (StringUtils.isNotBlank(value)) {
                                    groupChild.addAttribute("value", value);
                                }
                            } else {
                                groupChild.addAttribute("value", value);
                            }
                        } else {
                            String value = getNotNullString(configData, groupChild.attributeValue("ID"));
                            if (ConfigItemType.password.name().equalsIgnoreCase(groupChild.attributeValue("type"))) {
                                if (StringUtils.isNotBlank(value)) {
                                    groupChild.setText(value);
                                }
                            } else {
                                groupChild.setText(value);
                            }
                        }
                    }
                } else if ("Items".equalsIgnoreCase(child.getName())) {
                    String value = getNotNullString(configData, child.attributeValue("ID"));
                    if (ConfigItemType.password.name().equalsIgnoreCase(child.attributeValue("type"))) {
                        if (StringUtils.isNotBlank(value)) {
                            child.addAttribute("value", value);
                        }
                    } else {
                        child.addAttribute("value", value);
                    }
                }
            }
            XMLWriter xmlWriter = new XMLWriter(new FileOutputStream(file), OutputFormat.createPrettyPrint());
            xmlWriter.write(document);
            xmlWriter.close();
        } catch (DocumentException | IOException e) {
            throw new BizException("无法读取配置文件");
        }
    }

    private String getNotNullString(Map<String, Object> configData, String id) {
        Object value = configData.get(id);
        if (configData.get(id) instanceof List) {
            return value == null ? "" : StringUtils.join(((ArrayList) value).toArray(), ",");
        } else {
            return value == null ? "" : value.toString();
        }
    }

    private ConfigItemBo getConfigItem(Element element) {
        ConfigItemBo itemBo = new ConfigItemBo();
        itemBo.setId(element.attributeValue("ID"));
        itemBo.setName(element.attributeValue("name"));
        if (StringUtils.isNotBlank(element.attributeValue("type"))) {
            itemBo.setType(ConfigItemType.valueOf(element.attributeValue("type")));
        } else {
            itemBo.setType(ConfigItemType.string);
        }

        if (!CollectionUtils.isEmpty(element.content())) {
            String value = element.content().get(0).getText();
            if (ConfigItemType.bool.equals(itemBo.getType())) {
                itemBo.setValue("true".equalsIgnoreCase(value));
            } else if (!ConfigItemType.password.equals(itemBo.getType())) {
                itemBo.setValue(value);
            }
        }
        if (StringUtils.isNotBlank(element.attributeValue("seq"))) {
            itemBo.setSeq(Integer.valueOf(element.attributeValue("seq")));
        }
        if (StringUtils.isNotBlank(element.attributeValue("max"))) {
            itemBo.setMax(Double.valueOf(element.attributeValue("max")));
        }
        if (StringUtils.isNotBlank(element.attributeValue("min"))) {
            itemBo.setMin(Double.valueOf(element.attributeValue("min")));
        }
        return itemBo;
    }

    static class JobSpecification implements Specification<Job> {
        private JobSearchRequest request;

        public JobSpecification(JobSearchRequest request) {
            this.request = request;
        }

        @Override
        public Predicate toPredicate(Root<Job> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
            List<Predicate> predicates = new ArrayList<>();
            Path path = root.get("name");
            String keyword = request.getKeyword() == null ? "" : request.getKeyword();
            predicates.add(criteriaBuilder.like(path, "%" + keyword + "%"));
            if (!CollectionUtils.isEmpty(request.getStatus())) {
                path = root.get("status").get("id");
                predicates.add(path.in(request.getStatus().toArray()));
            }
            if (!CollectionUtils.isEmpty(request.getPlugins())) {
                path = root.get("plugin").get("id");
                predicates.add(path.in(request.getPlugins().toArray()));
            }
            Predicate[] pre = new Predicate[predicates.size()];
            criteriaQuery.where(predicates.toArray(pre));
            return criteriaQuery.getGroupRestriction();
        }
    }
}
