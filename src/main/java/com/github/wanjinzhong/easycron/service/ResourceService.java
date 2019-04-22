package com.github.wanjinzhong.easycron.service;
import java.io.File;

import com.github.wanjinzhong.easycron.constant.enums.ResourceType;
import com.github.wanjinzhong.easycron.dao.entity.Resource;

public interface ResourceService {
    byte[] getResource(Long resourceId);

    Resource buildResource(ResourceType type, String fileName);

    String getFilePath(ResourceType type, String fileName);

    File getResourceFile(Long resourceId);
}
