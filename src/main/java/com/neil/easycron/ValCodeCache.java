package com.neil.easycron;
import java.util.HashSet;
import java.util.Set;

import com.neil.easycron.bo.ValCodeBo;
import com.neil.easycron.constant.enums.ValCodeType;

public class ValCodeCache {
    private static Set<ValCodeBo> valCodes = new HashSet<>();

    public static void put(ValCodeBo valCode) {
        valCodes.add(valCode);
    }

    public static ValCodeBo pop(Integer userId, ValCodeType type, String codeStr) {
        ValCodeBo need = new ValCodeBo();
        need.setUserId(userId);
        need.setType(type);
        need.setCode(codeStr);
        ValCodeBo res = null;
        for (ValCodeBo code : valCodes) {
            if (code.equals(need)) {
                res = code;
                break;
            }
        }
        valCodes.remove(res);
        return res;
    }
}
