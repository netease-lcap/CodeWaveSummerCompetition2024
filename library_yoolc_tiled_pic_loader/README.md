# 大图瓦片加载

**依赖库设计**

这个依赖库旨在实现超大图纸渲染，图片上传后被切割存储，然后前端拿到图片拼接

**主要特性**

- **组件一：** 大图瓦片加载器，拿到被切割的图片并拼接显示
- **逻辑一：** 大图瓦片切片器，拿到用户上传的图片并切割存储

## 大图瓦片加载器

**特性1**: 基于 `leaflet` 加载瓦片大图，可以自由缩放及拖拽

## 大图瓦片切片器

**特性 1**:  使用前端 Web Worker 切片上传，确保性能的同时，缓解了后端服务器的压力

**特性 2**: 使用 `webp` 格式图片进行压缩，缓解切片带来的文件存储额外开销

**特性 3**: 支持超大尺寸的 `.jpg/.png` 格式图片进行分割

## 使用说明

### 组件：大图瓦片加载器

- **attrs**
  
  - `pictureURL`: 瓦片加载地址，默认为网站格式示例 `https://example.com/pic_test/{z}-{x}-{y}.png'`
  - `tileSize`: 瓦片像素大小，默认为 `512`
  - `maxZoom`: 最大缩放等级，默认为 `3`，缩放一级等于图片尺寸缩小一倍
  - `attribution`: 版权信息，默认为 `大图瓦片加载`
  
- **methods**

​	无

- **events**

​	无

### 逻辑：大图瓦片切片器

- `uploadPic`: 上传图片，为 `File` 类型的对象
- `ossEndpoint`: OSS 服务器上传地址, 示例 `https://oss-cn-hangzhou.aliyuncs.com`
- `ossRegion`: OSS 服务器上传 Region, 示例 `oss-cn-hangzhou`
- `ossAccessKeyId`: OSS 服务器上传 AccessKeyId
- `ossAccessKeySecret`: OSS 服务器上传 AccessKeySecret
- `ossBucket`: OSS 服务器上传 Bucket, 示例 `example-bucket`
- `maxZoomLevel`: 最大缩放级别, 示例 `3`
- `tileSize`: 瓦片大小, 示例 `512`
- `compressionQuality`: 压缩质量，填入 `0~1` 之间的数值，示例 `0.8`
- `maxWidth`: 切图限制最大宽度
- `maxHeight`: 切图限制最大高度
- `minWidth`: 切图限制最小宽度
- `minHeight`: 切图限制最小高度
- `maxSize`: 上传限制文件大小

## 应用演示链接

[查看示例演示](https://dev-tileloader-yoolc.app.codewave.163.com/test)

![image-20240523195159534](README.assets/image-20240523195159534.png)
