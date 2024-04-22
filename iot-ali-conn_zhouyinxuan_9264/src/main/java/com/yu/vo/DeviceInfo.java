package com.yu.vo;

import com.netease.lowcode.core.annotation.NaslStructure;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author 余卫青
 * @version 1.0.0
 * @date 2024/4/21 15:18
 **/
@NaslStructure
@ToString
@Getter
@Setter
public class DeviceInfo {
    public String deviceId;
    public String deviceName;
    public String deviceSecret;
    public String deviceStatus;
    public String deviceType;
    public String gmtCreate;
    public String gmtModified;
    public String iotId;
    public String nickname;
    public String productKey;
    public String utcCreate;
    public String utcModified;
}
