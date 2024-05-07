package com.yu.vo;

import com.netease.lowcode.core.annotation.NaslStructure;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

/**
 * @author 余卫青
 * @version 1.0.0
 * @date 2024/4/24 11:17
 **/
@Getter
@Setter
@ToString
@NaslStructure
public class BucketVo {
    /**
     * Bucket name
     */
    public String name;

    /**
     * Bucket owner
     */
    public OwnerVo owner;

    /**
     * Bucket location
     */
    public String location;

    /**
     * Created date.
     */
    public LocalDate creationDate;

    /**
     * Storage class (Standard, IA, Archive)
     */
    public String storageClass;

    /**
     * External endpoint.It could be accessed from anywhere.
     */
    public String extranetEndpoint;

    /**
     * Internal endpoint. It could be accessed within AliCloud under the same location.
     */
    public String intranetEndpoint;

    /**
     * Region
     */
    public String region;

    /**
     * The id of resource group.
     */
    public String resourceGroupId;
}
