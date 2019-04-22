package com.neil.easycron;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import com.neil.easycron.bo.ValCodeBo;
import com.neil.easycron.constant.enums.ValCodeType;

public class ValCodeCache {
    private static Set<ValCodeBo> valCodes = new HashSet<>();

    public static void put(ValCodeBo valCode) {
        valCodes.add(valCode);
    }

    public static void remove(ValCodeBo code) {
        valCodes.remove(code);
    }

    public static ValCodeBo get(Integer userId, ValCodeType type) {
        ValCodeBo need = new ValCodeBo();
        need.setUserId(userId);
        need.setType(type);
        ValCodeBo res = null;
        for (ValCodeBo code : valCodes) {
            if (code.equals(need)) {
                res = code;
                break;
            }
        }
        return res;
    }

    public static boolean isValCodeExpired(ValCodeBo code) {
        return code == null || code.getExpireTime() < Calendar.getInstance().getTimeInMillis();
    }
}
