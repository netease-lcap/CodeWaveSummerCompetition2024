package com.yu.api;

import com.aliyun.auth.credentials.Credential;
import com.aliyun.auth.credentials.provider.StaticCredentialProvider;
import com.aliyun.sdk.service.iot20180120.AsyncClient;
import com.aliyun.sdk.service.iot20180120.models.*;
import com.google.gson.Gson;
import com.netease.lowcode.core.annotation.NaslConnector;
import com.yu.dto.GetDeviceListDto;
import com.yu.dto.GetPropDto;
import com.yu.dto.InvokeServDto;
import com.yu.vo.*;
import darabonba.core.client.ClientOverrideConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author 余卫青
 * @version 1.0.0
 * @date 2024/4/20 21:19
 **/
@NaslConnector(connectorKind = "iot-ali-conn")
public class IotAliConnector {
    private static final Logger log = LoggerFactory.getLogger(IotAliConnector.class);
    private String accessKey;
    private String accessKeySecret;
    private String instanceId;

    private AsyncClient client;

    @NaslConnector.Creator
    public IotAliConnector init(String accessKey, String accessKeySecret, String instanceId) {
        this.accessKey = accessKey;
        this.accessKeySecret = accessKeySecret;
        this.instanceId = instanceId;
        StaticCredentialProvider provider = StaticCredentialProvider.create(Credential.builder().accessKeyId(this.accessKey).accessKeySecret(this.accessKeySecret).build());
        this.client = AsyncClient.builder().region("cn-shanghai") // Region ID
                .credentialsProvider(provider).overrideConfiguration(ClientOverrideConfiguration.create()
                        // Endpoint 请参考 https://api.aliyun.com/product/Iot
                        .setEndpointOverride("iot.cn-shanghai.aliyuncs.com")).build();
        return this;
    }


    @NaslConnector.Tester
    public Boolean connectTest(String accessKey, String accessKeySecret, String instanceId) {
        init(accessKey, accessKeySecret, instanceId);
        try {
            getProdList(1, 1);
            return true;
        } catch (RuntimeException e) {
            return false;
        }
    }

    /**
     * 获取产品列表
     *
     * @param currentPage
     * @param pageSize
     */
    @NaslConnector.Logic
    public GetProdVo getProdList(Integer currentPage, Integer pageSize) {
        if (currentPage == null || pageSize == null) throw new IllegalArgumentException("请传入完整的分页参数");
        QueryProductListRequest queryProductListRequest = QueryProductListRequest.builder()
                .pageSize(pageSize)
                .currentPage(currentPage)
                .iotInstanceId(this.instanceId).build();
        CompletableFuture<QueryProductListResponse> response = client.queryProductList(queryProductListRequest);
        try {
            QueryProductListResponse resp = response.get();
            if (resp.getBody().getSuccess()) {
                QueryProductListResponseBody.Data data = resp.getBody().getData();
                GetProdVo getProdVo = new GetProdVo();
                getProdVo.pageCount = data.getPageCount();
                getProdVo.total = data.getTotal();
                getProdVo.productInfos = new ArrayList<>();
                for (QueryProductListResponseBody.ProductInfo productInfo : data.getList().getProductInfo()) {
                    ProductInfo product = UserConvertUtils.INSTANCE.convertProductInfo(productInfo);
                    getProdVo.productInfos.add(product);
                }
                return getProdVo;
            } else {
                log.warn("获取产品列表失败：{}", new Gson().toJson(resp));
                throw new RuntimeException(resp.getBody().getErrorMessage());
            }
        } catch (InterruptedException | ExecutionException e) {
            log.error("获取产品列表失败：", e);
            throw new RuntimeException(e);
        }
    }


    /**
     * 获取设备历史属性数据
     *
     * @param propDto
     */
    @NaslConnector.Logic
    public GetPropVo getDeviceProp(GetPropDto propDto) {
        if (propDto == null) throw new IllegalArgumentException("查询参数不得为空");
        if (propDto.iotId == null && (propDto.productKey == null || propDto.deviceName == null))
            throw new IllegalArgumentException("传入 productKey和deviceName 或者传入iotId  不得两个筛选条件都为空");
        try {
            ReflectUtil.validRequire(propDto);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        QueryDevicePropertyDataRequest.Builder builder = QueryDevicePropertyDataRequest.builder();
        builder.iotInstanceId(this.instanceId).asc(propDto.asc).startTime(propDto.startTime).endTime(propDto.endTime).identifier(propDto.identifier).pageSize(propDto.pageSize);
        Optional.ofNullable(propDto.iotId).ifPresent(builder::iotId);
        Optional.ofNullable(propDto.productKey).ifPresent(builder::productKey);
        Optional.ofNullable(propDto.deviceName).ifPresent(builder::deviceName);
        QueryDevicePropertyDataRequest queryDevicePropertyDataRequest = builder.build();
        CompletableFuture<QueryDevicePropertyDataResponse> response = this.client.queryDevicePropertyData(queryDevicePropertyDataRequest);
        QueryDevicePropertyDataResponse resp = null;
        try {
            resp = response.get();
            if (resp.getBody().getSuccess()) {
                QueryDevicePropertyDataResponseBody.Data data = resp.getBody().getData();
                QueryDevicePropertyDataResponseBody.List list = data.getList();
                GetPropVo propVo = new GetPropVo();
                propVo.NextValid = data.getNextValid();
                propVo.NextTime = data.getNextTime();
                propVo.propertyInfos = new ArrayList<>();
                for (QueryDevicePropertyDataResponseBody.PropertyInfo propertyInfo : list.getPropertyInfo()) {
                    propVo.propertyInfos.add(new PropertyInfo(propertyInfo.getTime(), propertyInfo.getValue()));
                }
                return propVo;
            } else {
                log.warn("获取设备属性失败：{}", new Gson().toJson(resp));
                throw new RuntimeException(resp.getBody().getErrorMessage());
            }
        } catch (InterruptedException | ExecutionException e) {
            log.error("获取设备属性失败：", e);
            throw new RuntimeException(e);
        }
    }


    /**
     * 获取设备列表信息
     *
     * @param deviceDto
     * @return
     */
    @NaslConnector.Logic
    public GetDeviceListVo getDeviceList(GetDeviceListDto deviceDto) {
        if (deviceDto == null) throw new IllegalArgumentException("查询参数不得为空");
        if (deviceDto.productKey == null) throw new IllegalArgumentException("productKey不得为空");
        QueryDeviceRequest queryDeviceRequest = QueryDeviceRequest.builder().iotInstanceId(this.instanceId).productKey(deviceDto.productKey).pageSize(deviceDto.pageSize).currentPage(deviceDto.currentPage).build();
        CompletableFuture<QueryDeviceResponse> response = client.queryDevice(queryDeviceRequest);
        QueryDeviceResponse resp = null;
        try {
            resp = response.get();
            if (resp.getBody().getSuccess()) {
                GetDeviceListVo listVo = new GetDeviceListVo();
                QueryDeviceResponseBody body = resp.getBody();
                listVo.NextToken = body.getNextToken();
                listVo.PageCount = body.getPageCount();
                listVo.Total = body.getTotal();
                listVo.deviceInfos = new ArrayList<>();
                for (QueryDeviceResponseBody.DeviceInfo deviceInfo : body.getData().getDeviceInfo()) {
                    DeviceInfo device = UserConvertUtils.INSTANCE.convertDeviceInfo(deviceInfo);
                    listVo.deviceInfos.add(device);
                }
                return listVo;
            } else {
                log.warn("获取设备列表失败：{}", new Gson().toJson(resp));
                throw new RuntimeException(resp.getBody().getErrorMessage());
            }
        } catch (InterruptedException | ExecutionException e) {
            log.error("获取设备列表失败：", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取设备详细信息
     *
     * @param productKey
     * @param deviceName
     * @param iotId
     * @return
     */
    @NaslConnector.Logic
    public GetDeviceDetailVo getDeviceDetail(String productKey, String deviceName, String iotId) {
        QueryDeviceDetailRequest.Builder builder = QueryDeviceDetailRequest.builder().iotInstanceId(this.instanceId);
        if (iotId == null && (productKey == null || deviceName == null))
            throw new IllegalArgumentException("传入 productKey和deviceName 或者传入iotId  不得两个筛选条件都为空");

        Optional.ofNullable(productKey).ifPresent(builder::productKey);
        Optional.ofNullable(deviceName).ifPresent(builder::deviceName);
        Optional.ofNullable(iotId).ifPresent(builder::iotId);
        QueryDeviceDetailRequest queryDeviceDetailRequest = builder.build();
        CompletableFuture<QueryDeviceDetailResponse> response = client.queryDeviceDetail(queryDeviceDetailRequest);
        QueryDeviceDetailResponse resp = null;
        try {
            resp = response.get();
            if (resp.getBody().getSuccess()) {
                QueryDeviceDetailResponseBody.Data data = resp.getBody().getData();
                return UserConvertUtils.INSTANCE.convertDeviceDetailVo(data);
            } else {
                log.warn("获取设备详细失败：{}", new Gson().toJson(resp));
                throw new RuntimeException(resp.getBody().getErrorMessage());
            }
        } catch (InterruptedException | ExecutionException e) {
            log.error("获取设备详细信息失败：", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 设备远程服务调用
     *
     * @param servDto
     * @return
     */
    @NaslConnector.Logic
    public InvokeServVo invokeDevService(InvokeServDto servDto) {
        if (servDto.iotId == null && (servDto.productKey == null || servDto.deviceName == null))
            throw new IllegalArgumentException("传入 productKey和deviceName 或者传入iotId  不得两个筛选条件都为空");
        ReflectUtil.validRequire(servDto);
        InvokeThingServiceRequest.Builder builder = InvokeThingServiceRequest.builder().identifier(servDto.identifier).args(servDto.args).iotId(this.instanceId);
        Optional.ofNullable(servDto.productKey).ifPresent(builder::productKey);
        Optional.ofNullable(servDto.deviceName).ifPresent(builder::deviceName);
        Optional.ofNullable(servDto.iotId).ifPresent(builder::iotId);
        InvokeThingServiceRequest invokeThingServiceRequest = builder.build();

        CompletableFuture<InvokeThingServiceResponse> response = client.invokeThingService(invokeThingServiceRequest);
        InvokeThingServiceResponse resp = null;
        try {
            resp = response.get();
            if (resp.getBody().getSuccess()) {
                InvokeThingServiceResponseBody.Data data = resp.getBody().getData();
                return new InvokeServVo(data.getMessageId(), data.getResult());
            } else {
                log.warn("设备远程控制接口调用失败：{}", new Gson().toJson(resp));
                throw new RuntimeException(resp.getBody().getErrorMessage());
            }
        } catch (InterruptedException | ExecutionException e) {
            log.error("设备远程控制接口调用失败：", e);
            throw new RuntimeException(e);
        }
    }
}
