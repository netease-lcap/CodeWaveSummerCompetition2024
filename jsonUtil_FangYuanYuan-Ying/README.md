# 依赖库名称

依赖库提供xml转json，在json获得特定Key的内容。

## 逻辑详情

### convertXmlToJson
将xml内容转json
1. 入参：String：xml内容
2. 出参：String: json格式,当返回错误，返回null并且打印error日志

### getXPathKey
通过JSONPath 表达式在json内容获得特定的KEY 
实例： 
json: "{"name":"John","age":30,"address":{"city":"Hangzhou"}}"
key: $.name -> 表示顶层的 name key
key: $.address.city -> 表示嵌套在 address 中的 city key
1. 入参：String：json内容
2. 入参2: String: key JSONPath表达式
3. 出参：String: 返回对应key的内容,当json或者key为空，返回null，当key格式错误返回null,并且打印错误日志

## 使用步骤说明

1. 应用引用依赖库
![img_1.png](img_1.png)
2. 逻辑调用示例截图

![img.png](img.png)
![img_4.png](img_4.png)

![img_2.png](img_2.png)
![img_3.png](img_3.png)


## 应用演示链接
https://dev-testlib1-nt.app.codewave.163.com/test_lib2