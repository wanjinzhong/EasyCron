package com.github.wanjinzhong.easycron.service;
import java.util.List;

import com.github.wanjinzhong.easycron.bo.job.JobStatusBo;
import com.github.wanjinzhong.easycron.constant.enums.ListCatalog;

public interface ListBoxService {
    List<JobStatusBo> getJobStatus();

    String getNotNullDisplayName(ListCatalog catalog, String configCode);
}
