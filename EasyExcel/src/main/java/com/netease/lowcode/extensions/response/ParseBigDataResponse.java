package com.netease.lowcode.extensions.response;

import com.netease.lowcode.core.annotation.NaslStructure;

@NaslStructure
public class ParseBigDataResponse {

    public Boolean success;
    public String msg;
    public String trace;

    public Double cost;// 处理耗时,单位 s
    public Double size;// 文件大小,单位字节
    public Long total;// 统计导入条数

    public static ParseBigDataResponse FAIL(String msg) {
        return FAIL(msg,null);
    }

    public static ParseBigDataResponse FAIL(String msg,String trace){
        ParseBigDataResponse fail = new ParseBigDataResponse();
        fail.setSuccess(false);
        fail.setMsg(msg);
        fail.setTrace(trace);
        return fail;
    }

    public static ParseBigDataResponse OK() {
        return OK("success");
    }

    public static ParseBigDataResponse OK(Long total) {
        ParseBigDataResponse ok = OK();
        ok.setTotal(total);
        return ok;
    }

    public static ParseBigDataResponse OK(String msg) {
        ParseBigDataResponse ok = new ParseBigDataResponse();
        ok.setSuccess(true);
        ok.setMsg(msg);
        return ok;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getTrace() {
        return trace;
    }

    public void setTrace(String trace) {
        this.trace = trace;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public Double getSize() {
        return size;
    }

    public void setSize(Double size) {
        this.size = size;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }
}

