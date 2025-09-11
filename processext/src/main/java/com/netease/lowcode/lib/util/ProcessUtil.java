package com.netease.lowcode.lib.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.netease.codewave.process.util.ProcessConstant;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.flowable.bpmn.model.BaseElement;
import org.flowable.bpmn.model.UserTask;
import org.flowable.common.engine.impl.util.CollectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

public class ProcessUtil {

    private static final Logger logger = LoggerFactory.getLogger("LCAP_EXTENSION_LOGGER");

    private static ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).registerModule(new JavaTimeModule());
    }

    public static ZonedDateTime DateToZonedDateTime(Date date) {
        if (date == null) return null;
        return ZonedDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    public static Date ZonedDateTimeToDate(ZonedDateTime zonedDateTime) {
        if (zonedDateTime == null) return null;
        return Date.from(zonedDateTime.toInstant());
    }

    public static List<String> removeComma(String userList) {
        if (StringUtils.isBlank(userList)) return Collections.emptyList();
        return Arrays.asList(userList.split(","));
    }

    public static List<String> removeComma(List<String> userList) {
        if (CollectionUtil.isEmpty(userList)) return Collections.emptyList();
        List<String> result = new ArrayList<>();
        userList.forEach(user -> result.addAll(removeComma(user)));
        return result;
    }

    public static String getFrontendKey() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (Objects.isNull(requestAttributes)) {
            return "";
        }
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
        HttpServletRequest request = servletRequestAttributes.getRequest();
        String frontendKey = request.getHeader("LCAP-FRONTEND");
        if (StringUtils.isEmpty(frontendKey)) {
            return "default";
        }
        return frontendKey;
    }

    public static <T> T readValueToTypeReference(String content, TypeReference<T> typeReference) {
        if (StringUtils.isBlank(content)) {
            return null;
        }
        try {
            return objectMapper.readValue(content, typeReference);
        } catch (JsonProcessingException e) {
            logger.error("readValue error", e);
        }
        return null;
    }

    public static Map<String, Object> convertValueToMap(Object fromValue) {
        return objectMapper.convertValue(fromValue, new TypeReference<Map<String, Object>>() {
        });
    }

    public static <T> T convertValueToTypeReference(Object fromValue, TypeReference<T> typeReference) {
        return objectMapper.convertValue(fromValue, typeReference);
    }

    public static <T> T convertValueToCustomType(Object fromValue, Class<T> clazz) {
        return objectMapper.convertValue(fromValue, clazz);
    }

    public static String writeValueAsString(Object value) {
        if (value == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(value);
        } catch (Exception e) {
            logger.error("Parse data to json error", e);
        }
        return null;
    }

    public static List<String> getAutomaticApprovalStrategy(BaseElement currentElement) {
        if (ObjectUtils.isEmpty(currentElement)) {
            return new ArrayList<>();
        }
        String approvalStrategy = currentElement.getAttributeValue(ProcessConstant.EXTENSION_NAMESPACE, ProcessConstant.KEY_AUTOMATIC_APPROVAL_STRATEGIES);
        if (StringUtils.isNotBlank(approvalStrategy)) {
            try {
                return objectMapper.readValue(approvalStrategy, new TypeReference<List<String>>() {
                });
            } catch (JsonProcessingException e) {
                logger.error("getAutomaticApprovalStrategy error", e);
            }
        }
        return new ArrayList<>();
    }


    public static String getNodeTitle(BaseElement currentElement) {
        if (ObjectUtils.isEmpty(currentElement)) {
            return "";
        }
        return currentElement.getAttributeValue(ProcessConstant.EXTENSION_NAMESPACE, ProcessConstant.KEY_ELEMENT_TITLE);
    }

    public static String getNodeDescription(BaseElement currentElement) {
        if (ObjectUtils.isEmpty(currentElement)) {
            return "";
        }
        return currentElement.getAttributeValue(ProcessConstant.EXTENSION_NAMESPACE, ProcessConstant.KEY_ELEMENT_DESCRIPTION);
    }

    public static String getTaskApprovalPolicy(UserTask currentUserTask) {
        if (ObjectUtils.isEmpty(currentUserTask)) {
            return "";
        }
        if (currentUserTask.getLoopCharacteristics() == null) {
            //或签
            return ProcessConstant.VALUE_TASK_APPROVAL_POLICY_SIMPLE;
        } else {
            if (currentUserTask.getLoopCharacteristics().isSequential()) {
                return ProcessConstant.VALUE_TASK_APPROVAL_POLICY_SEQUENCE;
            } else {
                return ProcessConstant.VALUE_TASK_APPROVAL_POLICY_PARALLEL;
            }
        }
    }

}