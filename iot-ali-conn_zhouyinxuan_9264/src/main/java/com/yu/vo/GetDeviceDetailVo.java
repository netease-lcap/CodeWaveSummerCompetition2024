package com.yu.vo;

import com.netease.lowcode.core.annotation.NaslStructure;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author 余卫青
 * @version 1.0.0
 * @date 2024/4/21 15:44
 **/
@NaslStructure
@ToString
@Getter
@Setter
public class GetDeviceDetailVo {
    public String deviceName;
    public String deviceSecret;
    public String firmwareVersion;
    public String gmtActive;
    public String gmtCreate;
    public String gmtOnline;
    public String iotId;
    public String ipAddress;
    public String nickname;
    public Integer nodeType;
    public Boolean owner;
    public String productKey;
    public String productName;
    public String region;
    public String status;
    public String utcActive;
    public String utcCreate;
    public String utcOnline;
}
