package com.yu.dto;

import com.netease.lowcode.core.annotation.NaslStructure;
import com.yu.annotation.Validate;
import lombok.ToString;

/**
 * @author 余卫青
 * @version 1.0.0
 * @date 2024/4/24 9:40
 **/
@NaslStructure
@ToString
public class UploadDto {
    /**
     * bucker 名称
     */
    @Validate(required = true)
    public String bucketName;
    /**
     * 对象名称
     */
    @Validate(required = true)
    public String objectName;
    /**
     * 文件路径，互联网文件路径
     */
    @Validate(required = true)
    public String filePath;
    /**
     * 文件访问权限 取值为 default private public-read public-read-write
     */
    public String acl;
    /**
     * 存储类型
     */
    public String storeClass;
}
