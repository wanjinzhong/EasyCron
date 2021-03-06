package com.github.wanjinzhong.easycron.utils;
import java.net.URL;
import java.net.URLClassLoader;

import com.github.wanjinzhong.easycron.dao.entity.Plugin;
import com.github.wanjinzhong.easycron.exception.BizException;
import com.github.wanjinzhong.easycronbase.service.EasyJobService;

public class JobPluginUtil {
    private JobPluginUtil() {
    }

    public static EasyJobService getJobService(Plugin plugin) {
        String path = "file:" + plugin.getPackageLocation();
        try {
            URL url1 = new URL(path);
            URLClassLoader myClassLoader1 = new URLClassLoader(new URL[] {url1}, Thread.currentThread()
                                                                                       .getContextClassLoader());
            Class<?> serviceImpl = myClassLoader1.loadClass(plugin.getMainClass());
            return (EasyJobService) serviceImpl.newInstance();
        } catch (Exception e) {
            throw new BizException("插件包错误", e);
        }
    }
}
