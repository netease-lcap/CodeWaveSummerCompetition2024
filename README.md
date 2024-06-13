# 网易低代码平台依赖库开源仓库
## CodeWaveAssetCompetition2024
网易低代码开发者活动-资产大赛2024
![image](https://github.com/netease-lcap/CodeWaveAssetCompetition2024/assets/158463965/ea58284c-5be6-4b2d-956b-7b300499ee1e)

欢迎开发者们参加2024网易低代码资产共建大赛，活动官网介绍：https://sf.163.com/2024lcapassetcompetition

参赛指引：
![image](https://github.com/netease-lcap/CodeWaveAssetCompetition2024/assets/158463965/8f658d1c-0f81-4062-b794-a3d0e0ec900a)


—大赛介绍

1、资产共建大赛简介：

基于平台现有扩展集成能力，和开发者一起构建CodeWave平台资产市场，包括连接器、依赖库，打造丰富的开发场景资产，降低开发成本提升效率。活动结束后资产将会在平台的【资产中心】上架开放，提供企业&开发者后续使用。

2、资产共建大赛特色

  1）赛道自由选择，大赛提供了三个赛道方向可供参赛开发者选择，包括命题组的连接器需求、依赖库需求和非命题组（开发者自由提供优质资产)；

  2）赛事赋能保障，配套赋能直播课程和参赛交流社群，保障大家的学习、交流和问题咨询；

  3）多劳多得，上不封顶，活动鼓励开发者可多次参与提供多个资产，验收通过后均可获得激励；

  4）礼品随心选，活动以严选卡的形式发放激励，开发者可以根据自己的爱好前往严选平台购买心怡礼品


—赛事规则

1、参赛机制：

  1）一个命题可允许最多3名开发者参加，开发者需要在命题需求发布帖子下方评论区进行报名领取需求；

  2）一个命题涉及到多个资产提交，根据资产的质量度进行梯队评估激励，每个资产会有一个激励池总额，最佳质量资产（第一名）将获得85%资产池的激励，剩下15%作为其他资产激励；

  3）一个开发者最多同时参加三个命题需求领取，需求开发完提交验收通过后，会释放领取额度继续申领新需求；

  4）一个开发者在活动期间超过三次占用命题参加额度，但未提交资产，则后续不可以参与资产命题需求的申领（自由资产提供不限制）；

2、验收机制
![image](https://github.com/netease-lcap/CodeWaveAssetCompetition2024/assets/158463965/1b02393f-3df9-41f5-a187-64e71f7cef9f)

3、赛事激励：

激励内容：严选卡

说明：本次活动的激励因为资产价值恒量的不确定性，不再通过具体实物进行奖励，而是发放等同价值的严选卡。开发者获得严选卡激励后，可以前往严选平台绑定卡片自行进行商品挑选。 

激励形式：

  1）平台发布资产需求会提供对应的单个命题需求的激励额度池，开发者交付资产验收后符合规则即可获得一定的激励额度；

  2）开发者自己向平台主动提供的资产，平台会验收评审资产的价值，给到对应的激励额度；

严选平台优质商品推荐：https://docs.popo.netease.com/lingxi/65c7da790e5d40baaf7ee15e62e843e5

## README
### 项目名称
lcap_library_nexus是一个低代码平台依赖库开源仓库，用于存放低代码平台前端依赖库和服务端依赖库。
* 服务端扩展依赖库是指开发者使用Java编写的平台扩展能力库，用于满足服务端定制化需求和功能扩展。
* 前端依赖库主要提供扩展组件和前端自定义逻辑编写能力，满足前端定制化需求和功能扩展。

#### 官方依赖库

| 依赖库名称 | 依赖库类型 | 文件夹名称 | 描述                  | 资产市场地址                                                                                                                            |
| ------- | ------- | ------- |---------------------|-----------------------------------------------------------------------------------------------------------------------------------|
| excel解析 | 后端  | excel-parser   | 自动解析excel文档到IDE数据表格 | https://community.codewave.163.com/CommunityParent/CodeWareMarketLibraryDetail?id=2840513069648896&isLatest=false&isClassics=true |
| http请求客户端 | 后端   | httpclient   | 扩展平台原有api功能         | https://community.codewave.163.com/CommunityParent/CodeWareMarketLibraryDetail?id=2883851125296640&isLatest=false&isClassics=false 
| 音频依赖库 | 前端   | cw_audio_library   | 提供音频播放/录制功能         | 暂无
| 签名板依赖库 | 前端   | cw_signature_view   | 提供组件签名能力        | 暂无 
| 大文件上传依赖库 | 前端   | cw_large_file_uploader   | 提供分片上传能力        | 暂无
| markdown依赖库 | 前端   | cw_markdown   | 提供markdown编辑/预览能力        | 暂无   
| 数据脱敏依赖库 | 前端   | cw_desensitization   | 提供前端数据脱敏能力        | 暂无
| 全局水印依赖库 | 前端   | cw_watermark   | 提供页面全局水印的能力        | 暂无                                                                                                                                |
| redis依赖库 | 后端   | redis-template-tool   |支持各种模式的redis集群         | https://community.codewave.163.com/CommunityParent/CodeWareMarketLibraryDetail?id=2849760328948736&isLatest=false&isClassics=false
| dubbo依赖库 | 后端   | dubbo-tool   | 用于dubbo服务发现         | https://community.codewave.163.com/CommunityParent/CodeWareMarketLibraryDetail?id=2811501029676800&isLatest=false&isClassics=false
|EasyExcel导出数据依赖库| 后端   |  EasyExcel |支持各种模式的redis集群         | 暂无
|map有序化依赖库 | 后端   | sort-map |1. CodeWave当前支持的map都是无序的hashmap，这导致有些业务场景无法满足。所以这里提供了一个转为有序map的依赖库。2. 当前支持的Json都是无序的，这里提供了一个调整json顺序的方法，满足调用第三方客户接口的json顺序要求。         | https://community.codewave.163.com/CommunityParent/CodeWareMarketLibraryDetail?id=2849760328948736&isLatest=false&isClassics=false
|Freemaker依赖库| 后端   |  freemarker-tool |支持模板引擎         | https://community.codewave.163.com/CommunityParent/CodeWareMarketLibraryDetail?id=2884580661931520&isLatest=false&isClassics=false



### 贡献指南
[开发提交作品路径说明](%E5%BC%80%E5%8F%91%E6%8F%90%E4%BA%A4%E4%BD%9C%E5%93%81%E8%B7%AF%E5%BE%84%E8%AF%B4%E6%98%8E.md)

### 依赖库开发规范指导
[依赖库开发规范指导](%E4%BE%9D%E8%B5%96%E5%BA%93%E5%BC%80%E5%8F%91%E8%A7%84%E8%8C%83%E6%8C%87%E5%AF%BC.md)

### 版本管理
项目初始版本是 1.0.0。
项目进行 Bug 修正时，最后一位加 1，比如 1.0.1。
项目有新特性发布时，中间一位数加 1，同时最后一位复位为 0，比如 1.1.0。
项目有重大特性发布，同时结构可能不向下兼容时，第一位数字加 1，其他位数复位为0，比如2.0.0。
依赖库做任何代码修改时，都需要更新版本号。

### 测试用例
建议测试用例覆盖率达到70%以上。

### 许可证
本项目遵循网易贡献许可协议 - 有关详细信息，请参阅 [LICENSE](./LICENSE) 文件。



