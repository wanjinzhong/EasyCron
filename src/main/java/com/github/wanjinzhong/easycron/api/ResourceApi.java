package com.github.wanjinzhong.easycron.api;

import com.github.wanjinzhong.easycron.service.ResourceService;
import io.swagger.annotations.Api;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("public/api")
@Api
@RequiresAuthentication
public class ResourceApi {

    @Autowired
    private ResourceService resourceService;

    @GetMapping(value = "resource/{resourceId}", produces = {MediaType.IMAGE_JPEG_VALUE,MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_GIF_VALUE})
    public byte[] getResource(@PathVariable("resourceId") Long resourceId) {
        return resourceService.getResource(resourceId);
    }
}
