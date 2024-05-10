package com.yu.connector;

import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.yu.vo.BucketVo;
import com.yu.vo.OssFileVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author: 余卫青
 * @version: 1.0.0
 * @date: 2024/4/21 20:18
 **/
@Mapper
public interface UserConvertUtils {
    UserConvertUtils INSTANCE = Mappers.getMapper(UserConvertUtils.class);

    OssFileVo convertOSSObjectSummary(S3ObjectSummary objSummary);

    BucketVo convertBucket(Bucket bucket);


}