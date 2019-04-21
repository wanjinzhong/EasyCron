package com.neil.easycron.service;
import java.io.File;

import com.neil.easycron.constant.enums.ResourceType;
import com.neil.easycron.dao.entity.Resource;

public interface ResourceService {
    byte[] getResource(Long resourceId);

    Resource buildResource(ResourceType type, String fileName);

    String getFilePath(ResourceType type, String fileName);

    File getResourceFile(Long resourceId);
}
