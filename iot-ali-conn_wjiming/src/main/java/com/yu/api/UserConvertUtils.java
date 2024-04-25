package com.yu.api;

import com.aliyun.sdk.service.iot20180120.models.QueryDeviceDetailResponseBody;
import com.aliyun.sdk.service.iot20180120.models.QueryDeviceResponseBody;
import com.aliyun.sdk.service.iot20180120.models.QueryProductListResponseBody;
import com.yu.vo.DeviceInfo;
import com.yu.vo.GetDeviceDetailVo;
import com.yu.vo.ProductInfo;
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

    ProductInfo convertProductInfo(QueryProductListResponseBody.ProductInfo productInfo);

    DeviceInfo convertDeviceInfo(QueryDeviceResponseBody.DeviceInfo deviceInfo);

    GetDeviceDetailVo convertDeviceDetailVo(QueryDeviceDetailResponseBody.Data data);

}