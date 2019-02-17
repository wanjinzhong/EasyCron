package com.neil.easycron.utils;
import com.neil.easycron.bo.response.JsonEntity;

public class ResponseHelper {

    public ResponseHelper() {
    }

    public static <T> JsonEntity<T> createInstance(T object) {
        JsonEntity<T> response = new JsonEntity(object);
        return response;
    }

    public static <T> JsonEntity<T> of(T object) {
        return createInstance(object);
    }

    public static <T> JsonEntity<T> ofNothing() {
        return createInstance(null);
    }
}