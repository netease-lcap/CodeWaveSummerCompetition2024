package com.yu.dto;

import com.netease.lowcode.core.annotation.NaslStructure;
import lombok.ToString;

/**
 * @author 余卫青
 * @version 1.0.0
 * @date 2024/4/19 17:21
 **/
@NaslStructure
@ToString
public class SendForbiddenDto {
    public String code;
    public Integer count;
    public String userid;

}
