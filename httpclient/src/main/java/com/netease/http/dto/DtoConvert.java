package com.netease.http.dto;

public class DtoConvert {

    /**
     * object 转换为泛型类型 T
     *
     * @param object
     * @param <T>
     * @return
     */
    public static <T> T convertToGeneric(Object object) {
        return (T) object;
    }

    /**
     * RequestParam对象转换为RequestParamAllBodyTypeInner
     *
     * @param requestParam
     * @return
     */
    public static RequestParamAllBodyTypeInner convertToRequestParamAllBodyTypeInner(RequestParam requestParam) {
        RequestParamAllBodyTypeInner requestParamAllBodyTypeInner = new RequestParamAllBodyTypeInner();
        requestParamAllBodyTypeInner.setBody(requestParam.getBody());
        requestParamAllBodyTypeInner.setHeader(requestParam.getHeader());
        requestParamAllBodyTypeInner.setHttpMethod(requestParam.getHttpMethod());
        requestParamAllBodyTypeInner.setUrl(requestParam.getUrl());
        return requestParamAllBodyTypeInner;
    }

    /**
     * RequestParam对象转换为RequestParamAllBodyTypeInner
     *
     * @param requestParam
     * @return
     */
    public static RequestParamAllBodyTypeInner convertToRequestParamAllBodyTypeInner(RequestParamAllBodyType requestParam) {
        RequestParamAllBodyTypeInner requestParamAllBodyTypeInner = new RequestParamAllBodyTypeInner();
        requestParamAllBodyTypeInner.setBody(requestParam.getBody());
        requestParamAllBodyTypeInner.setHeader(requestParam.getHeader());
        requestParamAllBodyTypeInner.setHttpMethod(requestParam.getHttpMethod());
        requestParamAllBodyTypeInner.setUrl(requestParam.getUrl());
        return requestParamAllBodyTypeInner;
    }
}
