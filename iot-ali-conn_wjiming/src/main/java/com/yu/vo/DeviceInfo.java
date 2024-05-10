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
    /**
     * 设备id
     */
    public String deviceId;
    /**
     * 设备名称
     */
    public String deviceName;
    /**
     * 设备密码
     */
    public String deviceSecret;
    /**
     * 设备状态
     */
    public String deviceStatus;
    /**
     * 设备类型
     */
    public String deviceType;
    /**
     * 创建时间
     */
    public String gmtCreate;
    /**
     * 修改时间
     */
    public String gmtModified;
    /**
     * iot ID
     */
    public String iotId;
    /**
     * 别名
     */
    public String nickname;
    /**
     * 产品key
     */
    public String productKey;
    public String utcCreate;
    public String utcModified;
}
