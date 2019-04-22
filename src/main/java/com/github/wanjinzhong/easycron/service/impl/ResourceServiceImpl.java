package com.github.wanjinzhong.easycron.service.impl;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import com.github.wanjinzhong.easycron.constant.enums.ResourceType;
import com.github.wanjinzhong.easycron.constant.Constant;
import com.github.wanjinzhong.easycron.constant.enums.ListCatalog;
import com.github.wanjinzhong.easycron.dao.entity.ListBox;
import com.github.wanjinzhong.easycron.dao.entity.Resource;
import com.github.wanjinzhong.easycron.dao.repository.ListBoxRepository;
import com.github.wanjinzhong.easycron.dao.repository.ResourceRepository;
import com.github.wanjinzhong.easycron.exception.BizException;
import com.github.wanjinzhong.easycron.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ResourceServiceImpl implements ResourceService {

    @Autowired
    private ResourceRepository resourceRepository;

    @Autowired
    private ListBoxRepository listBoxRepository;

    @Override
    public byte[] getResource(Long resourceId) {
        File file = getResourceFile(resourceId);
        FileInputStream inputStream;
        try {
            inputStream = new FileInputStream(file);
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes, 0, inputStream.available());
            return bytes;
        } catch (IOException e) {
            throw new BizException("无法读取资源");
        }
    }

    @Override
    public Resource buildResource(ResourceType type, String fileName) {
        if (type == null) {
            throw new BizException("没有指定资源类型");
        }
        Resource newAvatar = new Resource();
        ListBox listBox = listBoxRepository.findByCatalogAndCode(ListCatalog.RESOURCE_TYPE, type.name());
        newAvatar.setType(listBox);
        newAvatar.setFileName(fileName);
        return newAvatar;
    }

    private String getFilePrefix(ResourceType type) {
        String path = null;
        switch (type) {
            case PLUGIN: path = Constant.ResourcePath.PLUGIN_FULL; break;
            case PLUGIN_IMG: path = Constant.ResourcePath.PLUGIN_IMG_FULL; break;
        }
        return path;
    }

    @Override
    public String getFilePath(ResourceType type, String fileName) {
        return getFilePrefix(type) + fileName;
    }

    @Override
    public File getResourceFile(Long resourceId) {
        Resource resource = resourceRepository.findById(resourceId).orElse(null);
        if (resource == null) {
            throw new BizException("资源未找到");
        }
        File file = new File(getFilePrefix(ResourceType.valueOf(resource.getType().getCode())) + resource.getFileName());
        if (!file.exists()) {
            throw new BizException("资源未找到");
        }
        return file;
    }

}
