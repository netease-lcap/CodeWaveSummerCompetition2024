package com.yu.connector;

import com.aliyun.oss.model.Bucket;
import com.aliyun.oss.model.OSSObjectSummary;
import com.aliyun.oss.model.PutObjectResult;
import com.yu.vo.BucketVo;
import com.yu.vo.OssFileVo;
import com.yu.vo.UploadReturnVo;
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

    UploadReturnVo convertPutObjRes(PutObjectResult putObjectResult);

    OssFileVo convertOSSObjectSummary(OSSObjectSummary objSummary);

    BucketVo convertBucket(Bucket bucket);


}