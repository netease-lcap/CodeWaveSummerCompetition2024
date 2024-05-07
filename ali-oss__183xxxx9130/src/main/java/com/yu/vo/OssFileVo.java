package com.yu.vo;

import com.netease.lowcode.core.annotation.NaslStructure;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

/**
 * @author 余卫青
 * @version 1.0.0
 * @date 2024/4/24 10:58
 **/
@Getter
@Setter
@ToString
@NaslStructure
public class OssFileVo {
    /**
     * bucket 名称
     */
    public String bucketName;

    /**
     * The key under which this object is stored
     */
    public String key;

    /**
     * eTag
     */
    public String eTag;
    /**
     * 文件大小
     */
    public Long size;
    /**
     * 上次修改时间
     */
    public LocalDate lastModified;

    /**
     * 存储类型
     */
    public String storageClass;

    /**
     * 拥有者信息
     */
    public OwnerVo owner;
    /**
     * 文件类型
     */
    public String type;
}
