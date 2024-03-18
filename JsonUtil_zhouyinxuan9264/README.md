# JsonUtil
包含JsonUtil依赖库

## 逻辑详情

### 逻辑一  xmlToJson

将xml 字符串转成json字符串
入参 xml 符合格式的xml字符串   入参示例：<root><element1>value1</element1><element2>value2</element2></root>
返回示例：{"element1":"value1","element2":"value2"}

### 逻辑二 queryFromJson

 将json字符串按照指定的jsonPath取出
入参 jsonStr  json字符串 示例 {"element1":"value1","element2":"value2"}
入参 jsonPath json路径  示例 $.element1
 返回值示例  value1


## 使用步骤说明

1.  应用引用依赖库
2.  配置应用配置参数（如果有的话）
3.  逻辑调用示例截图
4.  ![img.png](img.png)
5. ![img_1.png](img_1.png)

## 应用演示链接
[使用了本依赖库的制品应用链接]
