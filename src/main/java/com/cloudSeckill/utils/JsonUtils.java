package com.cloudSeckill.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class JsonUtils {
    private static ObjectMapper objectMapper = null;

    static {
        objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        objectMapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }
    
    public static String writeValueAsString(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj).replaceAll("\r|\n", "");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "";
        }
    }
}
