package com.netease.lowcode.extensions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netease.lowcode.extensions.jackson.serializers.LocalDateSerializer;
import com.netease.lowcode.extensions.jackson.serializers.LocalTimeSerializer;
import com.netease.lowcode.extensions.jackson.serializers.ZonedDateTimeSerializer;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;

public class JsonUtil {
    private static final ObjectMapper MAPPER = Jackson2ObjectMapperBuilder
            .json()
            .indentOutput(false)
            .failOnEmptyBeans(false)
            .failOnUnknownProperties(false)
            .serializerByType(LocalTime.class, new LocalTimeSerializer())
            .serializerByType(ZonedDateTime.class, new ZonedDateTimeSerializer())
            .serializerByType(LocalDate.class, new LocalDateSerializer()).build();

    public static <T> T fromJson(String json, Class<T> valueType) throws JsonProcessingException {
        return MAPPER.readValue(json, valueType);
    }

    public static String toJson(Object object) throws JsonProcessingException {
        return MAPPER.writeValueAsString(object);

    }

    public static JsonNode fromJson(String json) throws JsonProcessingException {
        return MAPPER.readTree(json);
    }
}
