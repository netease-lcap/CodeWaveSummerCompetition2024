package com.yu.vo;

import com.netease.lowcode.core.annotation.NaslStructure;
import lombok.ToString;

/**
 * @author 余卫青
 * @version 1.0.0
 * @date 2024/4/21 17:45
 **/
@NaslStructure
@ToString
public class InvokeServVo {
    public String messageId;
    public String result;

    public InvokeServVo() {
    }

    public InvokeServVo(String messageId, String result) {
        this.messageId = messageId;
        this.result = result;
    }
}
