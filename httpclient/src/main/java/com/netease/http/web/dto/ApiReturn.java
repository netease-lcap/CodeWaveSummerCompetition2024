package com.netease.http.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ApiReturn<T> {
    @JsonProperty("Code")
    private int code = 200;

    @JsonProperty("Message")
    private String message = "";

    @JsonProperty("Data")
    private T data;

    public static <T> ApiReturn<T> of(T data) {
        ApiReturn apiReturn = new ApiReturn<>();
        apiReturn.setData(data);
        return apiReturn;
    }

    public static <T> ApiReturn<T> of(T data, int code) {
        ApiReturn apiReturn = new ApiReturn<>();
        apiReturn.setData(data);
        apiReturn.setCode(code);
        return apiReturn;
    }

    public static <T> ApiReturn<T> of(T data, int code, String message) {
        ApiReturn apiReturn = new ApiReturn<>();
        apiReturn.setData(data);
        apiReturn.setCode(code);
        apiReturn.setMessage(message);
        return apiReturn;
    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
