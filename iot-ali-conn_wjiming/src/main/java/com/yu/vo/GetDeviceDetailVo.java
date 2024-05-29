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
    /**
     * 设备名称
     */
    public String deviceName;
    /**
     * 设备密码
     */
    public String deviceSecret;
    /**
     * 固件版本
     */
    public String firmwareVersion;
    /**
     * gmt 活跃时间
     */
    public String gmtActive;
    /**
     * gmt 创建时间
     */
    public String gmtCreate;
    /**
     *gmt 在线时间
     */
    public String gmtOnline;
    /**
     *iot ID
     */
    public String iotId;
    /**
     * ip地址
     */
    public String ipAddress;
    /**
     * 昵称
     */
    public String nickname;
    /**
     * 节点类型
     */
    public Long nodeType;
    /**
     * 所有者
     */
    public Boolean owner;
    /**
     * 产品key
     */
    public String productKey;
    /**
     * 产品名称
     */
    public String productName;
    /**
     * 区域
     */
    public String region;
    /**
     * 状态
     */
    public String status;
    /**
     * 跟gmt类似
     */
    public String utcActive;
    /**
     *
     */
    public String utcCreate;
    /**
     *
     */
    public String utcOnline;
}
