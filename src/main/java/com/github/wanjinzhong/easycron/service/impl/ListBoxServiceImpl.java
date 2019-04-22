package com.github.wanjinzhong.easycron.service.impl;
import java.util.List;
import java.util.stream.Collectors;

import com.github.wanjinzhong.easycron.bo.job.JobStatusBo;
import com.github.wanjinzhong.easycron.constant.enums.ListCatalog;
import com.github.wanjinzhong.easycron.dao.repository.ListBoxRepository;
import com.github.wanjinzhong.easycron.dao.entity.ListBox;
import com.github.wanjinzhong.easycron.service.ListBoxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ListBoxServiceImpl implements ListBoxService {

    @Autowired
    private ListBoxRepository listBoxRepository;

    @Override
    public List<JobStatusBo> getJobStatus() {
        List<ListBox> jobStatus = listBoxRepository.findByCatalog(ListCatalog.JOB_STATUS);
        return jobStatus.stream().map(status -> {
            JobStatusBo jobStatusBo = new JobStatusBo();
            jobStatusBo.setId(status.getId());
            jobStatusBo.setCode(status.getCode());
            jobStatusBo.setName(status.getDisplayName());
            return jobStatusBo;
        }).collect(Collectors.toList());
    }

    @Override
    public String getNotNullDisplayName(ListCatalog catalog, String configCode) {
        String str = "";
        ListBox listBox = listBoxRepository.findByCatalogAndCode(catalog, configCode);
        if (listBox != null) {
            str = listBox.getDisplayName();
        }
        return str;
    }
}
