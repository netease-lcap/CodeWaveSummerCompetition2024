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
示例代码git仓库：https://github.com/netease-lcap/libraryDemo
二次开发指导文档：https://docs.popo.netease.com/team/pc/0u6nqau4/pageDetail/cf78bb11a5c94c769bbc196ec640889b

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