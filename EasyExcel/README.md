# easyExcel-tool
对[easyExcel](https://easyexcel.opensource.alibaba.com/docs/current/)的封装，方便制品使用。

# 接口详情
## EasyExcelTools.parseExcel
解析excel 支持xlsx xls csv
入参：

| 属性         | 类型           | 描述           |
|------------|--------------|--------------|
| url        | String       | 解析文件url      |
| sheetNames | List<String> | 需要解析的sheet名称 |
| headRow    | Integer      | 表头行数         |
出参：

| 属性 | 类型                                    | 描述 |
|----|---------------------------------------|----|
|    | [ParseExcelResult](#ParseExcelResult) | 出参 |

## EasyExcelTools.parseFirstSheet
解析第一个sheet
入参：

| 属性         | 类型           | 描述           |
|------------|--------------|--------------|
| url        | String       | 解析文件url      |
| headRow    | Integer      | 表头行数         |
出参：

| 属性 | 类型                                    | 描述 |
|----|---------------------------------------|----|
|    | [ParseSheetResult](#ParseSheetResult) | 出参 |

## EasyExcelTools.createExcelWithUrl
根据输入数据生成excel
（若数据为图片链接，若要将图片展示在excel中，须给此数据加上codewave_excel_pic:前缀。不支持csv文件。）

入参：

| 属性        | 类型                                  | 描述 |
|-----------|-------------------------------------|----|
| excelData | [CustomExcelData](#CustomExcelData) | 入参 |
出参：

| 属性 | 类型                              | 描述 |
|----|---------------------------------|----|
|    | [ExcelResponse](#ExcelResponse) | 出参 |

## EasyExcelTools.parseBigDataExcel
解析excel,适合大数据量
入参：

| 属性      | 类型                             | 描述     |
|---------|--------------------------------|--------|
| handle  | Function<List<String>, String> | 数据处理函数 |
| request | [ParseRequest](#ParseRequest)  | 请求参数   |
出参：

| 属性 | 类型                                            | 描述 |
|----|-----------------------------------------------|----|
|    | [ParseBigDataResponse](#ParseBigDataResponse) | 出参 |

## EasyExcelTools.exportBigDataExcelV2
导出大数据量excel,v2通过class加载数据结构
入参：

| 属性         | 类型                                       | 描述         |
|------------|------------------------------------------|------------|
| queryData  | Function<QueryCondition, List<String>>   | 用户定义数据查询逻辑 |
| condition  | [QueryCondition](#QueryCondition)        | 请求参数       |
| saveResult | Function<ExportBigDataResponse, Boolean> | 数据处理完成回调   |
出参：

| 属性 | 类型                                              | 描述 |
|----|-------------------------------------------------|----|
|    | [ExportBigDataResponse](#ExportBigDataResponse) | 出参 |

## EasyExcelTools.exportBigDataExcel
导出大数据量excel
入参：

| 属性         | 类型                                       | 描述         |
|------------|------------------------------------------|------------|
| queryData  | Function<QueryCondition, List<String>>   | 用户定义数据查询逻辑 |
| condition  | [QueryCondition](#QueryCondition)        | 请求参数       |
| saveResult | Function<ExportBigDataResponse, Boolean> | 数据处理完成回调   |
出参：

| 属性 | 类型                                              | 描述 |
|----|-------------------------------------------------|----|
|    | [ExportBigDataResponse](#ExportBigDataResponse) | 出参 |

## EasyExcelFillTools.complexFill
纵向单列表复杂填充(适合少量数据)
入参：

| 属性      | 类型                                        | 描述 |
|---------|-------------------------------------------|----|
| request | [ComplexFillRequest](#ComplexFillRequest) | 入参 |

出参：

| 属性 | 类型                                              | 描述 |
|----|-------------------------------------------------|----|
|    | [ExportBigDataResponse](#ExportBigDataResponse) | 出参 |

# 结构体
## ParseExcelResult

| 属性       | 类型                            | 描述                    |
|----------|-------------------------------|-----------------------|
| sheetMap | Map<String, ParseSheetResult> | sheet数据 key=sheetName |
| errorMsg | String                        | 错误信息                  |
| success  | Boolean                       | 是否成功                  |

## ParseSheetResult

| 属性        | 类型                 | 描述      |
|-----------|--------------------|---------|
| sheetName | String             | sheet名称 |
| sheetNo   | Integer            | sheet编号 |
| headList  | List<List<String>> | 表头数据    |
| dataList  | List<List<String>> | 数据      |
| errorMsg  | String             | 错误信息    |
| success   | Boolean            | 是否成功    |

## CustomExcelData

| 属性        | 类型                    | 描述      |
|-----------|-----------------------|---------|
| fileName  | String                | 文件名称    |
| sheetList | List<CustomSheetData> | sheet数据 |
## CustomSheetData

| 属性        | 类型                 | 描述                                                   |
|-----------|--------------------|------------------------------------------------------|
| sheetName | String             | sheet名称                                              |
| head      | List<String>       | 表头数据                                                 |
| data      | List<List<String>> | 若数据为图片链接，若要将图片展示在excel中，须给此数据加上codewave_excel_pic:前缀 |
## ExcelResponse

| 属性      | 类型      | 描述   |
|---------|---------|------|
| success | Boolean | 是否成功 |
| msg     | String  | 调用信息 |
| data    | String  | 数据   |
| trace   | String  | 异常信息 |
## ParseRequest

| 属性             | 类型      | 描述                   |
|----------------|---------|----------------------|
| url            | String  | excel的url            |
| hasImageColumn | Boolean | 是否包含图片列需要解析，默认为false |
| fullClassName  | String  | 解析结构类的全路径名           |
## ParseBigDataResponse

| 属性      | 类型      | 描述       |
|---------|---------|----------|
| success | Boolean | 是否成功     |
| msg     | String  | 返回信息     |
| trace   | String  | 异常信息     |
| cost    | Double  | 处理耗时, s  |
| size    | Double  | 文件大小, 字节 |
| total   | Long    | 导入条数     |
## QueryCondition

| 属性                   | 类型                 | 描述                                                                                                                                                             |
|----------------------|--------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------|
| pageNum              | Integer            | 查询起始页码，从0开始                                                                                                                                                    |
| pageSize             | Integer            | 查询每页大小                                                                                                                                                         |
| totalPages           | Integer            | 总页数                                                                                                                                                            |
| isAsync              | Boolean            | 是否开启异步化，默认同步                                                                                                                                                   |
| customQueryCondition | Map<String,String> | 其他自定义查询条件                                                                                                                                                      |
| fileName             | String             | 指定下载的文件名（*.xlsx）                                                                                                                                               |
| sheetTotalRows       | Integer            | 指定每个sheet的最大数据量，注意如果设置程最大行数注意减去表头行数                                                                                                                            |
| headList             | List<HeadData>     | 设置表头,复杂表头设置多行 <br/>headList.add(Arrays.asList("主标题1","子标题1"));<br/>headList.add(Arrays.asList("主标题1","子标题2"));<br/>headList.add(Arrays.asList("主标题2","子标题3")); |
| columnWidth          | Integer            | 设置列宽                                                                                                                                                           |
| fullClassName        | String             | 数据关系映射，未来逐步切换使用该方式，不再通过headList传入                                                                                                                              |

## HeadData

| 属性     | 类型           | 描述 |
|--------|--------------|----|
| titles | List<String> | 表头 |
## ComplexFillRequest

| 属性           | 类型           | 描述      |
|--------------|--------------|---------|
| fileName     | String       | 文件名称    |
| templateUrl  | String       | 模板url   |
| jsonData     | String       | 填充的数据   |
| listJsonData | List<String> | 填充的列表数据 |
## ExportBigDataResponse

| 属性       | 类型      | 描述      |
|----------|---------|---------|
| success  | Boolean | 是否成功    |
| msg      | String  | 返回信息    |
| trace    | String  | 异常信息    |
| filePath | String  | 相对下载路径  |
| url      | String  | 绝对下载路径  |
| cost     | Double  | 处理耗时,s  |  
| size     | Double  | 文件大小,字节 |
| total    | Long    | 处理条数    |
