package com.neil.easycron.bo.response;

import java.io.Serializable;

public class JsonEntity<T> implements Serializable {
    private int code = 200;
    private String message;
    private T data;

    public JsonEntity() {
    }

    public JsonEntity(T data) {
        this.data = data;
    }

    public int getCode() {
        return this.code;
    }

    public JsonEntity setCode(int code) {
        this.code = code;
        return this;
    }

    public String getMessage() {
        return this.message;
    }

    public JsonEntity setMessage(String message) {
        this.message = message;
        return this;
    }

    public T getData() {
        return this.data;
    }

    public JsonEntity setData(T data) {
        this.data = data;
        return this;
    }
}
