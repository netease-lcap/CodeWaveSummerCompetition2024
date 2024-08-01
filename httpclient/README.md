# httpclient

简单http调用客户端。
接口异常时，返回的TransferCommonException类型。ide中可在服务端逻辑中，根据返回类型是否是TransferCommonException判断接口是否执行成功。
TransferCommonException中errorMsg包含了http状态码和返回信息。格式为："httpStatus:" + httpStatus + ",responseBody:" + responseBody

# 接口详情
## LCAPHttpClient.exchange 数据格式为非form
@Deprecated
入参：
- url: 请求地址
- httpMethod: 请求方法
- header: 请求头
- body: 请求体（Map<String,String>格式，不支持form）

出参：第三方返回完整信息的String格式 || TransferCommonException异常

## LCAPHttpClient.exchangeV2 数据格式为非form，异常时返回http错误码
入参：
- url: 请求地址
- httpMethod: 请求方法
- header: 请求头
- body: 请求体（Map<String,String>格式，不支持form）

出参：第三方返回完整信息的String格式  || TransferCommonException异常

## LCAPHttpClient.exchangeWithoutUriEncode 数据格式为非form，url不编码
入参：
- url: 请求地址
- httpMethod: 请求方法
- header: 请求头
- body: 请求体（Map<String,String>格式，不支持form）

出参：第三方返回完整信息的String格式  || TransferCommonException异常

## LCAPHttpClient.exchangeAllBodyType 数据格式为String（包括json序列化）。支持证书校验忽略
入参：
- RequestParamAllBodyType：
  - url: 请求地址
  - httpMethod: 请求方法
  - header: 请求头
  - body: 请求体（String格式，不支持文件。示例：a=1&b=cc&d=aasd1231）
  - isIgnoreCrt:是否忽略证书校验

出参：第三方返回完整信息的String格式  || TransferCommonException异常

## LCAPHttpClient.exchangeCrtForm https请求忽略证书，form表单专用body为MultiValueMap
入参：
- requestParam：
  - url: 请求地址
  - httpMethod: 请求方法
  - header: 请求头
  - body: 请求体（Map<String,String>格式）
  - isIgnoreCrt:是否忽略证书校验

出参：第三方返回完整信息的String格式  || TransferCommonException异常

## LCAPHttpClient.exchangeCrt 数据格式为Map。支持证书校验忽略
入参：
- requestParam：
  - url: 请求地址
  - httpMethod: 请求方法
  - header: 请求头
  - body: 请求体（Map<String,String>格式，不支持form）
  - isIgnoreCrt:是否忽略证书校验

出参：第三方返回完整信息的String格式  || TransferCommonException异常

## LCAPHttpClient.downloadFileUploadNos 下载文件，并上传到nos
入参：
- fileUrl: 请求地址
- header: 请求头
默认get请求，body为空

出参：上传到nos的文件地址  || TransferCommonException异常

## LCAPHttpClient.uploadNosExchange 上传文件到nos，并调用第三方接口
入参：
- fileUrl: 文件地址（ide上传文件后返回，不包括域名）
- requestUrl: 当前请求地址（前端逻辑中使用js代码块window.location.href获取，目的是获取当前应用的域名）
- requestParam：
  - url: 请求地址
  - httpMethod: 请求方法
  - header: 请求头
  - body: 请求体（Map<String,String>格式，仅支持form）
  - isIgnoreCrt:是否忽略证书校验

出参：第三方返回完整信息的String格式  || TransferCommonException异常