package com.github.wanjinzhong.easycron.utils;
import java.util.List;

import com.google.common.collect.Lists;

public class CommonUtil {
    private CommonUtil() {
    }

    /**
     * Convert mysql special characters
     *
     * @param param param
     * @return result
     */
    public static String convertMySqlParam(String param) {
        List<String> specialChars = Lists.newArrayList("_", "%");
        for (String ch : specialChars) {
            param = param.replace(ch, "\\" + ch);
        }
        return param;
    }
}
