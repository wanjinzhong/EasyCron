package com.neil.easycron.constant;

import java.io.File;
import java.text.SimpleDateFormat;

public class Constant {
    public static final Integer HASH_ITERATIONS = 2;
    public static final String BASIC_GROUP_NAME = "基本";
    public static final Integer SYSTEM_ID = 1;

    public static class ResourcePath {
        public static final String ROOT_PATH = System.getProperty("user.home") + File.separator + ".easycron";
        public static final String RESOURCE_BASE = File.separator + "resources";
        public static final String AVATAR_BASE = RESOURCE_BASE + File.separator + "avatar" + File.separator;
        public static final String PLUGIN_BASE = RESOURCE_BASE + File.separator + "plugin" + File.separator;
        public static final String PLUGIN_IMG_BASE = RESOURCE_BASE + File.separator + "plugin_img" + File.separator;
        public static final String JOBS_BASE = RESOURCE_BASE + File.separator + "jobs" + File.separator;
        public static final String RESOURCE_FULL = ROOT_PATH + RESOURCE_BASE;
        public static final String AVATAR_FULL = ROOT_PATH + AVATAR_BASE;
        public static final String PLUGIN_FULL = ROOT_PATH + PLUGIN_BASE;
        public static final String JOBS_FULL = ROOT_PATH + JOBS_BASE;
        public static final String PLUGIN_IMG_FULL = ROOT_PATH + PLUGIN_IMG_BASE;
    }

    public static final SimpleDateFormat FULL_DATE_FORMAT = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss:SSS");

    public static class JobParam {
        public static final String JOB = "JOB";
        public static final String START_TIME = "START_TIME";
        public static final String ENTRY_USER = "ENTRY_USER";
    }
}
