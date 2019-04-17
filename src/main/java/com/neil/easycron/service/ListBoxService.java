package com.neil.easycron.service;
import java.util.List;

import com.neil.easycron.bo.job.JobStatusBo;
import com.neil.easycron.constant.enums.ListCatalog;

public interface ListBoxService {
    List<JobStatusBo> getJobStatus();

    String getNotNullDisplayName(ListCatalog catalog, String configCode);
}
