package com.neil.easycron.bo;
import java.util.Objects;

import com.neil.easycron.constant.enums.ValCodeType;

public class ValCodeBo {
    private Integer userId;
    private Long startTime;
    private Long expireTime;
    private ValCodeType type;
    private String code;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public ValCodeType getType() {
        return type;
    }

    public void setType(ValCodeType type) {
        this.type = type;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Long expireTime) {
        this.expireTime = expireTime;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ValCodeBo valCodeBo = (ValCodeBo) o;
        return userId.equals(valCodeBo.userId) &&
               type == valCodeBo.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, type);
    }
}
