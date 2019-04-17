package com.neil.easycron.service.impl;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.neil.easycron.constant.Constant;
import com.neil.easycron.dao.entity.Resource;
import com.neil.easycron.dao.repository.ResourceRepository;
import com.neil.easycron.exception.BizException;
import com.neil.easycron.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ResourceServiceImpl implements ResourceService {

    @Autowired
    private ResourceRepository resourceRepository;

    @Override
    public byte[] getResource(Long resourceId) {
        Resource resource = resourceRepository.findById(resourceId).orElse(null);
        if (resource == null) {
            throw new BizException("资源未找到");
        }
        File file = new File(Constant.ROOT_PATH + resource.getPath());
        if (!file.exists()) {
            throw new BizException("资源未找到");
        }
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes, 0, inputStream.available());
            return bytes;
        } catch (IOException e) {
            throw new BizException("无法读取资源");
        }
    }
}
