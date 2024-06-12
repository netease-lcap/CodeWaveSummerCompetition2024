# spring-boot-starter-dubbo-tool
低代码dubbo基础工具包

## 项目介绍
spring-boot-starter-dubbo-tool是一个基于dubbo-spring-boot-starter的低代码dubbo基础工具包。
本工具包封装了dubbo-spring-boot-starter的基础配置，使得开发者无需关注dubbo的基础配置，只需专注于业务开发。

## 使用方式
在IDE中引入本依赖库。打开右上角应用配置，配置dubbo相关信息即可使用。

### 强类型调用
用户需要先引入本依赖库，再编写自己的业务依赖库。业务依赖库可以参考libraryDemo中的dubbo_biz_demo。
其中业务依赖库不需要引入dubbo-spring-boot-starter，不需要留意dubbo的基础配置，只需要引入本依赖库，在IDE右上角应用配置中配置dubbo信息即可。
IDE右上角应用配置中的dubbo信息是由本依赖库spring-boot-starter-dubbo-tool定义的。
1. 在pom中引入主数据dubbo share包和相关的工具包
2. @DubboReference引入dubbo服务接口
3. 调用dubbo方法

二次开发指导文档：https://community.codewave.163.com/CommunityParent/fileIndex?filePath=40.%E6%89%A9%E5%B1%95%E4%B8%8E%E9%9B%86%E6%88%90%2F10.%E6%89%A9%E5%B1%95%E5%BC%80%E5%8F%91%E6%96%B9%E5%BC%8F%2F30.%E6%9C%8D%E5%8A%A1%E7%AB%AF%E6%89%A9%E5%B1%95%E5%BC%80%E5%8F%91%2F10.%E4%BE%9D%E8%B5%96%E5%BA%93%E5%BC%80%E5%8F%91%2F30.%E4%BE%9D%E8%B5%96%E5%BA%93%E5%BC%80%E5%8F%91%E8%BF%9B%E9%98%B6%E6%95%99%E7%A8%8B-Dubbo%E4%B8%9A%E5%8A%A1%E4%BA%8C%E6%AC%A1%E5%BC%80%E5%8F%91%E6%A1%88%E4%BE%8B.md

### 泛化调用
直接在IDE中调用本依赖库的com.netease.lowcode.dubbo.dubbo.generic.DubboGenericUtil.invoke逻辑。
其中ParameterList参数结构如下：
public class ParameterList {
    /**
    * 参数列表
    */
    public List<Parameter> parameterParameter;
}
Parameter由type和param组成。分别是参数类型和参数值Map。