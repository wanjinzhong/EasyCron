package com.neil.easycron.constant;

import java.text.SimpleDateFormat;

public class Constant {
    public static final Integer HASH_ITERATIONS = 2;
    public static final String BASIC_GROUP_NAME = "基本";
    public static final String ROOT_PATH = System.getProperty("user.home") + "/.easycron";
    public static final Integer SYSTEM_ID = 1;

    public static final SimpleDateFormat FULL_DATE_FORMAT = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss:SSS");

    public static class JobParam {
        public static final String JOB = "JOB";
        public static final String START_TIME = "START_TIME";
        public static final String ENTRY_USER = "ENTRY_USER";
    }
}
