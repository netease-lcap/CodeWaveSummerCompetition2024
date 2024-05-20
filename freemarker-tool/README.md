freemarker工具

# 接口详情
## FreeMarkerUtil.createFile 根据模板创建文件
入参：
- request: [CreateRequest](#createrequest)

出参：

## FreeMarkerUtil.createNewDocxFile 根据模板创建docx文件
入参：
- request：[CreateDocxRequest](#createdocxrequest)

出参：


# 结构体详情
## CreateRequest
| 属性          | 类型     | 描述         |
|-------------|--------|------------|
| templateUrl | String | 模板url      |
| outFileName | String | 模板数据       |
| outFileName | String | 输出文件名(带后缀) |


## CreateDocxRequest
| 属性                  | 类型                 | 描述                                                                                                                                                                                              |
|---------------------|--------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| templateUrl         | Map<String,String> | k=模板名称,v=模板url<br/><ul><li>word/document.xml</li><li>word/_rels/document.xml.rels</li><li>word/header1.xml</li><li>word/header2.xml</li><li>word/footer1.xml</li><li>word/footer2.xml</li></ul> |
| outFileName         | String             | 输出文件名(带后缀)                                                                                                                                                                                      |
| templateDocxFileUrl | String             | 模板文件url                                                                                                                                                                                         |
| jsonData            | String             | 模板数据                                                                                                                                                                                            |
| imageMap            | Map<String,String> | k=图片名称,v=图片信息                                                                                                                                                                                   |
| base64              | Boolean            | 图片是否编码<br/><ul><li>false:传入图片url</li><li>true:传入图片base64编码</li></ul>                                                                                                                            |
