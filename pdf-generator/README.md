<a name="dzs8P"></a>
# 介绍
您是否想创建这样的pdf模板：<br />![image.png](https://cdn.nlark.com/yuque/0/2024/png/25980294/1722246967895-22ba66e0-516c-495d-8cce-f5804ad54ca7.png#averageHue=%23dee4e0&clientId=u324fdad3-5b4d-4&from=paste&height=842&id=u447a127f&originHeight=842&originWidth=1550&originalType=binary&ratio=1&rotation=0&showTitle=false&size=278911&status=done&style=none&taskId=ub038b244-0d5b-44fc-b291-8c6052b65b4&title=&width=1550)

1. 具有水印，可控制水印稠密、旋转角度等；
2. 具有页眉，可包含logo、文本；![image.png](https://cdn.nlark.com/yuque/0/2024/png/25980294/1722247294904-547703f9-1ae8-4f58-a348-1c5216a11a08.png#averageHue=%23f0efef&clientId=u324fdad3-5b4d-4&from=paste&height=33&id=u1dbc2c7a&originHeight=74&originWidth=291&originalType=binary&ratio=1&rotation=0&showTitle=false&size=11655&status=done&style=none&taskId=u8b53de35-173d-4abf-8f2e-8be7d0fe466&title=&width=130.31251525878906)
3. 具有页脚，可包含文本；
4. 具有页码，可包含当前页码、总页数；![image.png](https://cdn.nlark.com/yuque/0/2024/png/25980294/1722247330673-485fbc7a-b542-40f8-b4c2-4d56ceeafc5f.png#averageHue=%23f9f8f7&clientId=u324fdad3-5b4d-4&from=paste&height=37&id=uab300857&originHeight=33&originWidth=134&originalType=binary&ratio=1&rotation=0&showTitle=false&size=2476&status=done&style=none&taskId=u7bfc4b6a-cf19-44ab-97ab-f4bdf8479d4&title=&width=148.8888928330976)
5. 能添加图片；
6. 能添加表格，精确控制表格样式；
7. 能将长表格拆分为多列；![image.png](https://cdn.nlark.com/yuque/0/2024/png/25980294/1722247372619-be90f138-96e9-403f-8791-5110739f8cd3.png#averageHue=%23ededec&clientId=u324fdad3-5b4d-4&from=paste&height=84&id=ube7a66bb&originHeight=76&originWidth=1035&originalType=binary&ratio=1&rotation=0&showTitle=false&size=19336&status=done&style=none&taskId=u198db1c2-57b8-460f-ae3e-894de7c054d&title=&width=1150.000030464597)
8. 支持freemarker动态填充表格；
9. 表格高度可随文本动态变化；
10. 能添加复选框![image.png](https://cdn.nlark.com/yuque/0/2024/png/25980294/1722247268976-b3618ab6-3b46-4aab-b916-4fe797b837fe.png#averageHue=%23e0dcd8&clientId=u324fdad3-5b4d-4&from=paste&height=28&id=u5994c4f1&originHeight=25&originWidth=176&originalType=binary&ratio=1&rotation=0&showTitle=false&size=3830&status=done&style=none&taskId=u91f98173-ddf3-4924-9672-8a1e84c5707&title=&width=195.55556073600877)

引入依赖库pdf-generator<br />使用方法：createPDFV2<br />参数有两个：

1. jsonData，即是freemarker所需的json格式的请求数据；
2. templateUrl，即是按照下述方式制作的模板url。

<a name="z8kcc"></a>
## 逻辑介绍
<a name="f7wRI"></a>
### createPDFByTemplate
根据DC表单生成pdf，需要首先使用Adobe dc制作表单。<br />请求参数：
```java
public class CreateByTemplateRequest {
    // dc 表单url地址
    public String templateUrl;
    // 表单填充的数据json
    public String jsonData;
    // 导出文件的名称 *.pdf
    public String exportFileName;
}
```
<a name="laitV"></a>
### createPDFV2
根据传入的json模板生成pdf，需要参考下文的模板结构，编写pdf的结构json<br />请求参数：
```java
// 填充的数据json
String jsonData,
// 模板json文件的url地址
String templateUrl
```
<a name="G8ZD6"></a>
### createPDFV2ByStr
根据传入的json模板生成pdf，需要参考下文的模板结构，编写pdf的结构json<br />与createPDFV2不同的是，这里jsonTemplate传入的是字符串，而非文件。<br />请求参数：
```java
// 填充的数据json
String jsonData, 
// 模板json字符串
String jsonTemplate
```
<a name="mF56u"></a>
### createPDF
支持的格式较少，不推荐。<br />根据请求数据生成pdf<br />请求参数：
```java
public class CreateRequest {
    // 导出的文件名称
    public String fileName;
    /**
     * key表示数据类型: 文本、图片、表格
     * value表示具体内容,如果是表格类型，value是包括表头在内的数据集合，
     */
    public List<Map<String, List<String>>> data;
}
```
<a name="LwFIj"></a>
### xlsx2pdf
解析excel（*.xlsx）样式，生成pdf，能够在excel中调整表格样式，字体大小、颜色、居中等，逻辑会解析出样式，并转为pdf<br />请求参数：
```java
public class CreateByXlsxRequest {
    // excel文件url地址，目前仅支持xlsx
    public String templateUrl;
    // 填充的数据json
    public String jsonData;
    // 导出文件名 *.pdf
    public String exportFileName;
    // 纸张大小，默认A4
    public String pageSize = "A4";
    // 纸张方向，默认纵向
    public Boolean rotate = false;
    // 异步执行，默认false(预留参数 当前填哪个都是同步)
    public Boolean async = false;
    // [可选]限定读取范围，防止读取大量空白行
    // 如果不设置就会读取所有行列，当存在大量空白格时可能造成oom
    // 限定行，填模板excel左侧的行号 1,2,3,......
    public Integer lastRowNum;
    // 限定列，填模板excel顶部的列标签 A,B,C,......
    public String lastColLabel;
}
```

示例jsonData数据：
```json
{
    "name":"测试名字",
    "list":[
        {
            "no":1,
            "name":"项目1",
            "std":"国标1"
        },
        {
            "no":2,
            "name":"项目2",
            "std":"国标2"
        },
        {
            "no":3,
            "name":"项目3",
            "std":"国标3"
        }
    ]
}
```
示例模板<br />![image.png](https://cdn.nlark.com/yuque/0/2024/png/25980294/1724313615284-9c2a945b-128c-4c10-93ff-91d205267b51.png#averageHue=%23eaeaea&clientId=u898ea99d-8a39-4&from=paste&height=432&id=u49d4b98e&originHeight=389&originWidth=750&originalType=binary&ratio=0.8999999761581421&rotation=0&showTitle=false&size=52585&status=done&style=none&taskId=u9ba07923-d96e-4f53-a271-793cf6e7547&title=&width=833.3333554091283)
> 普通变量，在excel中使用 ${xx变量名}
> 列表变量，在excel中使用 ${xxlist变量名.xxarr变量名}，中间用`.`分隔
> 其中：
> 数据向下填充
> `${list.no},${list.name},......`
> 数据拆分成两部分，分别填充。
> `${list.no},${list.name},......``${list.no},${list.name},......`

pdf效果：<br />![image.png](https://cdn.nlark.com/yuque/0/2024/png/25980294/1724313655134-256c5841-3b0a-4501-b171-9625647fcc29.png#averageHue=%23f4f3f1&clientId=u898ea99d-8a39-4&from=paste&height=576&id=u5a142486&originHeight=518&originWidth=810&originalType=binary&ratio=0.8999999761581421&rotation=0&showTitle=false&size=137998&status=done&style=none&taskId=u899bf3b2-a640-4c4e-b308-48540a72a07&title=&width=900.0000238418586)
<a name="PviLb"></a>
# 基本知识
要生成pdf需要先根据规则制作一个模板，后续可根据该模板填充数据；

<a name="y99Mi"></a>
## 模板结构
模板文件是一个json格式的文件，他的基本结构如下：
```json
{
  "fileName": "文件名.pdf",
  "font": {全局字体},
  "fontSize": 全局字体大小,
  "pageSize": "纸张大小 默认A4",
  "rotate": 纸张方向，支持纵向、横向,
  "marginLeft": 纸张左边距,
  "marginRight": 纸张右边距,
  "marginTop": 纸张上边距,
  "marginBottom": 纸张下边距,
  "header": {页眉},
  "footer": {页脚},
  "waterMask": {水印},
  "pageNumber": {页码},
  "nodes": [内容]
}
```

了解整体模板结构之后，我们详细介绍下每一项：
<a name="zniLQ"></a>
### fileName
类型为：文本。
```json
{
  "fileName": "test.pdf"
}
```
pdf生成后的文件名，需要带后缀，xxx.pdf。

<a name="XJIJc"></a>
### font
类型为：对象
```json
{
  "font":{
    "fontProgram": "STSong-Light",
    "encoding": "UniGB-UCS2-H"
  }
}
```
全局字体，如无特别需要，可以指定上述字体信息。

<a name="Ap9QF"></a>
### nodes
此处为pdf的内容，如果为空会生成一个空白pdf。<br />格式为对象数组
```json
{
  "nodes": [{},{},...]
}
```
节点目前支持多种类型，详细介绍请看下一节《节点说明》
<a name="bWBfM"></a>
### [非必填]fontSize
类型为：数字
```json
{
  "fontSize": 7
}
```
全局字体大小，默认7

<a name="IOx3y"></a>
### [非必填]pageSize
类型为：文本
```json
{
  "pageSize": "A4"
}
```
纸张大小，系统支持：A0~A10、B0~B10，默认A4
<a name="C3DT6"></a>
### [非必填]rotate
类型为：布尔
```json
{
  "rotate": true
}
```
纸张方向，默认纵向；rotate=true为横向。
<a name="AXTts"></a>
### [非必填]marginLeft
类型为：数字
```json
{
  "marginLeft": 36
}
```
纸张左边距
<a name="Jam7i"></a>
### [非必填]marginRight
类型为：数字
```json
{
  "marginRight": 36
}
```
纸张右边距
<a name="GurVp"></a>
### [非必填]marginTop
类型为：数字
```json
{
  "marginTop": 36
}
```
纸张上边距
<a name="gu2DC"></a>
### [非必填]marginBottom
类型为：数字
```json
{
  "marginBottom": 36
}
```
纸张下边距
<a name="DyuJc"></a>
### [非必填]header
类型为：对象
```json
{
  "header": {
    "fontColor": "LIGHT_GRAY",
    "fontSize": 20,
    "text": "这里是页眉",
    "marginLeft": 35,
    "marginTop": 50,
    "textAlignment": "LEFT",
    "image": {
      "base64": "data:image/png;base64,iVBOAAXLSzXw5ErkJggg==",
      "fitWidth": 100,
      "fitHeight": 100,
      "marginLeft": 0,
      "marginTop": 50,
      "opacity": 0.5
    }
  }
}
```
页眉，下面分别介绍页眉的每一项配置：

- [非必填]fontColor：页眉字体颜色，默认`LIGHT_GRAY`，支持`RED、BLUE、CYAN、DARK_GRAY、GRAY、GREEN、LIGHT_GRAY、MAGENTA、ORANGE、PINK、WHITE、YELLOW、_BLACK_`
- [非必填]fontSize：页眉字体大小，默认20
- text：页眉文本，必填。
- [非必填]marginLeft：页眉左边距，默认0
- [非必填]marginTop：页眉顶边距，默认0
- [非必填]textAlignment：页眉文本对齐方式，默认左对齐`LEFT`，支持`_LEFT、CENTER、RIGHT、JUSTIFIED、JUSTIFIED_ALL_`
- [非必填]image：页眉logo，设置信息请参考image节点。
<a name="p8FQs"></a>
### [非必填]footer
类型为：对象
```json
{
  "footer": {
    "fontColor": "LIGHT_GRAY",
    "fontSize": 20,
    "text": "这里是页脚",
    "marginLeft": 35,
    "marginBottom": 50,
    "textAlignment": "LEFT"
  }
}
```
页脚，下面分别介绍页脚的每一项配置：

- [非必填]fontColor：页脚字体颜色，默认`LIGHT_GRAY`，支持`RED、BLUE、CYAN、DARK_GRAY、GRAY、GREEN、LIGHT_GRAY、MAGENTA、ORANGE、PINK、WHITE、YELLOW、_BLACK_`
- [非必填]fontSize：页脚字体大小，默认20
- text：页脚文本，必填。
- [非必填]marginLeft：页脚左边距，默认0
- [非必填]marginBottom：页脚底边距，默认0
- [非必填]textAlignment：页脚文本对齐方式，默认左对齐`LEFT`，支持`_LEFT、CENTER、RIGHT、JUSTIFIED、JUSTIFIED_ALL_`

<a name="UKdmN"></a>
### [非必填]waterMask
类型为：对象
```json
{
  "waterMask": {
    "fontColor": "LIGHT_GRAY",
    "fontSize": 20,
    "x": 0,
    "y": 0,
    "angle": 30,
    "text": "这里是水印内容",
    "xAxisElementSpacing": 200,
    "yAxisElementSpacing": 200
  }  
}
```
水印，下面分别介绍水印的每一项配置：

- [非必填]fontColor：水印字体颜色，默认`LIGHT_GRAY`，支持`RED、BLUE、CYAN、DARK_GRAY、GRAY、GREEN、LIGHT_GRAY、MAGENTA、ORANGE、PINK、WHITE、YELLOW、_BLACK_`
- [非必填]fontSize：水印字体大小，默认20
- [非必填]x：水印起始x坐标，默认0（从纸张左侧边缘开始）
- [非必填]y：水印起始y坐标，默认0（从纸张底部边缘开始）
- [非必填]angle：水印文本旋转角度，默认30°（0°~360°逆时针方向）
- text：水印文本，必填。
- [非必填]xAxisElementSpacing：水印横向间隔，默认纸张宽度
- [非必填]yAxisElementSpacing：水印纵向间隔，默认纸张高度

<a name="k5qSC"></a>
### [非必填]pageNumber
类型为：对象
```json
{
  "pageNumber":  {
    "fontColor": "LIGHT_GRAY",
    "fontSize": 10,
    "text": "页码 第 %s - %s 页",
    "marginRight": 100,
    "marginBottom": 35,
    "textAlignment": "CENTER"
  }  
}
```
页码，下面分别介绍页码的每一项配置：

- [非必填]fontColor：页码字体颜色，默认`LIGHT_GRAY`，支持`RED、BLUE、CYAN、DARK_GRAY、GRAY、GREEN、LIGHT_GRAY、MAGENTA、ORANGE、PINK、WHITE、YELLOW、_BLACK_`
- [非必填]fontSize：页码字体大小，默认20
- text：页码文本，必填。样式自定义，必须包含`%s`，只包含1个，会解析为页码，如`第 1 页`；包含2个，会解析为页码+总页数，如`第 1/3 页` ；包含2个以上，无意义。
- [非必填]marginRight：页码右边距，默认0
- [非必填]marginBottom：页码底边距，默认0
- [非必填]textAlignment：页码文本对齐方式，默认左对齐`LEFT`，支持`_LEFT、CENTER、RIGHT、JUSTIFIED、JUSTIFIED_ALL_`
<a name="faPcG"></a>
## 节点说明
每个节点是一个对象，在json中使用`{}`包裹，需要添加到nodes数组中。下面将介绍节点的类型：
<a name="p4yGp"></a>
### Image
```json
{
  "type": "Image",
  "base64":"data:image/png;base64,iVBORw0KGgo===",
  "fitWidth":100,
  "fitHeight":100,
  "marginLeft":0,
  "marginTop":50,
  "opacity":0.5
}
```
下面将详细介绍每一项：

- type：节点类型，必填。（页眉logo时可不填此项）
- base64：图片base64编码，必填（暂不支持其他格式图片）
- [非必填]fitWidth：图片缩放，最大宽度（与fitHeight按最小生效），默认100
- [非必填]fitHeight：图片缩放，最大高度（与fitWidth按最小生效），默认100
- [非必填]marginLeft：绝对位置，图片左页边距（页眉logo时可设置此项）
- [非必填]marginTop：绝对位置，图片顶页边距（页眉logo时可设置此项）
- [非必填]opacity：透明度，取值0~1（0完全透明；1完全不透明）

<a name="CdXa7"></a>
### AreaBreak
```json
{
  "type":"AreaBreak"
}
```
pdf另起一页，与页码一起使用时可能会导致总页数错位。

<a name="VAeT1"></a>
### Paragraph
```json
{
  "type": "Paragraph",
  "text": "段落文本",
  "bold": true,
  "textAlignment": "CENTER",
  "fontSize": 21,
  "fontColor": "RED",
  "underline":"",
  "marginLeft:"",
  "marginRight":"",
  "elements":[]
}
```
pdf中的一段文本。

- type：节点类型，必填。
- text：段落文本，必填
- [非必填]bold：true文本加粗，缺省或false不加粗；
- [非必填]textAlignment：文本对齐方式，默认左对齐`LEFT`，支持`_LEFT、CENTER、RIGHT、JUSTIFIED、JUSTIFIED_ALL_`
- [非必填]fontSize：段落文本字体大小
- [非必填]fontColor：页码字体颜色，默认`BLACK`，支持`RED、BLUE、CYAN、DARK_GRAY、GRAY、GREEN、LIGHT_GRAY、MAGENTA、ORANGE、PINK、WHITE、YELLOW、_BLACK_`
- [非必填]underline：文本下划线，缺省字段时无下划线
- [非必填]marginLeft：文本左边距，向右缩进
- [非必填]marginRight：文本右边距，向左缩进
- [非必填]elements：段落子元素节点数组，支持添加节点类型为`Image、Paragraph、Table、CheckBox、LineSeparator`

<a name="DkXtX"></a>
### Table
```json
{
  "type": "Table",
  "width": 100,
  "columnSize": 2,
  "chunkSize": 2,
  "cells": []
}
```
pdf中的表格

- type：节点类型，必填。
- width：表格宽度（单位为百分比，100表示占据纸张100%宽度，不包含页边距）
- columnSize：表格的列数
- [非必填]chunkSize：表格水平分块，如下图所示，即是将表格的行水平均分填充，2就代表均分2份填充![](https://cdn.nlark.com/yuque/0/2024/png/25980294/1722247372619-be90f138-96e9-403f-8791-5110739f8cd3.png?x-oss-process=image%2Fformat%2Cwebp#averageHue=%23ededec&from=url&id=AS3yp&originHeight=76&originWidth=1035&originalType=binary&ratio=1&rotation=0&showTitle=false&status=done&style=none&title=)
- cells：单元格对象数组，每个单元格的结构如下
```json
{
  "width": 50,
  "textAlignment":"",
  "elements": []
}
```
> 单元格各项配置说明如下：
> - width：单元格的宽度（单位为百分比，100表示占据纸张100%宽度，不包含页边距）
> - [非必填]textAlignment：单元格内文本对齐方式，默认左对齐`LEFT`，支持`_LEFT、CENTER、RIGHT、JUSTIFIED、JUSTIFIED_ALL_`
> - elements：单元格内元素节点数组，支持添加节点类型为`Image、Paragraph、Table、CheckBox、LineSeparator`



<a name="M82do"></a>
### CheckBox
```json
{
  "type": "CheckBox",
  "checked": true,
  "checkBoxType": "CROSS",
  "size": 7,
  "border": {
    "width": 1
  }
}
```
下面分别介绍各项配置：

- type：节点类型，必填。
- checked：是否选中
- checkBoxType：类型，枚举可参考下表
- size：大小
- border.width：线条宽度
| checkBoxType | checked |  |
| --- | --- | --- |
| `CROSS` | ![image.png](https://cdn.nlark.com/yuque/0/2024/png/25980294/1722320715265-aac6b80f-1e89-41c5-aa23-564afa4c163a.png#averageHue=%23acacac&clientId=u324fdad3-5b4d-4&from=paste&height=33&id=mISZ0&originHeight=30&originWidth=34&originalType=binary&ratio=1&rotation=0&showTitle=false&size=502&status=done&style=none&taskId=u2d3dffc8-dd4b-429a-8806-5af483a8c62&title=&width=37) | ![image.png](https://cdn.nlark.com/yuque/0/2024/png/25980294/1722320726441-c0282e38-46d9-4cb2-ac5f-418a675611b5.png#averageHue=%23c7c7c7&clientId=u324fdad3-5b4d-4&from=paste&height=33&id=EMzt5&originHeight=32&originWidth=36&originalType=binary&ratio=1&rotation=0&showTitle=false&size=352&status=done&style=none&taskId=u03291b00-7ecd-4a4c-a45c-6d1a7fa64b6&title=&width=37) |
| `CHECK` | ![image.png](https://cdn.nlark.com/yuque/0/2024/png/25980294/1722321084169-74936e3e-34bd-4f62-83af-cc6e18bc3a74.png#averageHue=%23a2a1a0&clientId=u324fdad3-5b4d-4&from=paste&height=36&id=u98b9936c&originHeight=38&originWidth=39&originalType=binary&ratio=1&rotation=0&showTitle=false&size=915&status=done&style=none&taskId=u83789a52-7dd3-4fd1-97b0-d849ed75123&title=&width=37) | ![image.png](https://cdn.nlark.com/yuque/0/2024/png/25980294/1722321096157-1b7d23ac-52cb-4a39-a1e0-a6a0418207e7.png#averageHue=%23b1b1b1&clientId=u324fdad3-5b4d-4&from=paste&height=33&id=u12ae75cf&originHeight=36&originWidth=36&originalType=binary&ratio=1&rotation=0&showTitle=false&size=343&status=done&style=none&taskId=ub4374311-36e6-475f-ac21-0965227f717&title=&width=33) |
| `CIRCLE` | ![image.png](https://cdn.nlark.com/yuque/0/2024/png/25980294/1722321143527-976ee0f5-f495-4a9b-b299-4438dd8ef619.png#averageHue=%23775c43&clientId=u324fdad3-5b4d-4&from=paste&height=33&id=uab347581&originHeight=35&originWidth=36&originalType=binary&ratio=1&rotation=0&showTitle=false&size=830&status=done&style=none&taskId=ubd74abfd-653d-475d-9932-8d105bdca6f&title=&width=34) | ![image.png](https://cdn.nlark.com/yuque/0/2024/png/25980294/1722321153754-cc6ccc8e-58dc-45f1-8b4d-08f79bb2622b.png#averageHue=%239f9f9f&clientId=u324fdad3-5b4d-4&from=paste&height=33&id=u6274b9a8&originHeight=34&originWidth=31&originalType=binary&ratio=1&rotation=0&showTitle=false&size=255&status=done&style=none&taskId=u35903a7c-3a59-4f8e-abf4-6b86f56baf9&title=&width=30) |
| `DIAMOND` | ![image.png](https://cdn.nlark.com/yuque/0/2024/png/25980294/1722321208349-4c25fefc-1cd5-4c8c-85ee-96d4953726d6.png#averageHue=%23686665&clientId=u324fdad3-5b4d-4&from=paste&height=33&id=ucec638a1&originHeight=48&originWidth=53&originalType=binary&ratio=1&rotation=0&showTitle=false&size=1278&status=done&style=none&taskId=u8a537333-e87b-4237-b545-c7574ca1e0b&title=&width=36) | ![image.png](https://cdn.nlark.com/yuque/0/2024/png/25980294/1722321218177-f4cd6489-1174-4f82-a126-0a68bd1ba9cc.png#averageHue=%23939393&clientId=u324fdad3-5b4d-4&from=paste&height=31&id=u19a29fa9&originHeight=48&originWidth=48&originalType=binary&ratio=1&rotation=0&showTitle=false&size=525&status=done&style=none&taskId=u753e0aa3-9f87-4017-b87b-734ecc3e399&title=&width=31) |
| `SQUARE` | ![image.png](https://cdn.nlark.com/yuque/0/2024/png/25980294/1722321268355-72152b7f-dd38-43c3-98df-514aa720d182.png#averageHue=%235e5d5d&clientId=u324fdad3-5b4d-4&from=paste&height=33&id=ud8e6e005&originHeight=29&originWidth=28&originalType=binary&ratio=1&rotation=0&showTitle=false&size=324&status=done&style=none&taskId=u7d5630df-7090-452b-9238-84956d4a7cb&title=&width=32) | ![image.png](https://cdn.nlark.com/yuque/0/2024/png/25980294/1722321277320-f7ea40c0-5ce3-4074-b525-fa602bcb1b73.png#averageHue=%23aaaaaa&clientId=u324fdad3-5b4d-4&from=paste&height=33&id=u530aab47&originHeight=28&originWidth=27&originalType=binary&ratio=1&rotation=0&showTitle=false&size=299&status=done&style=none&taskId=ub7ef13be-5bcc-4e8b-83a4-37c05b2fd76&title=&width=32) |
| `STAR` | ![image.png](https://cdn.nlark.com/yuque/0/2024/png/25980294/1722321323222-5eb11d14-8054-46ea-a383-cfc0c8e793e8.png#averageHue=%23696765&clientId=u324fdad3-5b4d-4&from=paste&height=33&id=u2fdc1152&originHeight=40&originWidth=40&originalType=binary&ratio=1&rotation=0&showTitle=false&size=1100&status=done&style=none&taskId=ueb59ab37-17ee-4b2f-b352-d27a49a2db8&title=&width=33) | ![image.png](https://cdn.nlark.com/yuque/0/2024/png/25980294/1722321333073-bcd4e3db-5a59-4f30-8b99-dd1ce3b35a9a.png#averageHue=%23a5a5a5&clientId=u324fdad3-5b4d-4&from=paste&height=33&id=u4c860a99&originHeight=41&originWidth=43&originalType=binary&ratio=1&rotation=0&showTitle=false&size=391&status=done&style=none&taskId=uf14dc41e-dd95-4ca5-a756-7c0b01da46a&title=&width=35) |


<a name="g6BfL"></a>
### LineSeparator
```json
{
  "type": "LineSeparator",
  "marginLeft": 1,
  "width": 10,
  "lineWidth": 0.5
}
```
线条

- type：节点类型，必填。
- [非必填]marginLeft：线条起点距纸张左边距（向右缩进）
- [非必填]width：宽度（线条长度）
- [非必填]lineWidth：线条宽度（粗细）

<a name="oQg8M"></a>
## 最小示例
```json
{
  "fileName":"test.pdf",
  "font":{
    "fontProgram": "STSong-Light",
    "encoding": "UniGB-UCS2-H"
  },
  "nodes":[
    {
      "type": "Paragraph",
      "text": "测试pdf",
      "textAlignment": "CENTER",
      "bold": true,
      "fontSize": 21,
    },
    {
      "type": "Paragraph",
      "text": "在一个宁静的小村庄里，有一位名叫艾米莉的年轻女子。她是村里唯一的医生，深受村民们的尊敬和爱戴。艾米莉总是全心全意地照顾每一位病人，她的善良和慷慨让人们对她倾慕不已。"
    },
    {
      "type": "Paragraph",
      "text": "有一天，一位叫做约翰的年轻人受了重伤，被送到了艾米莉的诊所。艾米莉倾注所有的精力和关怀，不断地呵护着他。在她的悉心照料下，约翰逐渐康复。感激之情油然而生，他发誓要回报艾米莉的恩情。"
    },
    {
      "type": "Paragraph",
      "text": "约翰决定在村里建立一所小学，让每个孩子都能接受教育。他邀请艾米莉成为这所小学的医务室负责人，希望她能继续照顾村里的孩子们。艾米莉欣然接受了约翰的提议，她将自己的医术和关爱传授给了一代又一代的孩子们。"
    },
    {
      "type": "Paragraph",
      "text": "艾米莉和约翰的善举感动了整个村庄，他们的故事被传颂了许多年。他们的善行激励着人们，让爱与关怀在这个小村庄里代代相传。"
    }
  ]
}
```

![image.png](https://cdn.nlark.com/yuque/0/2024/png/25980294/1722326329040-f9ac5e85-7bf9-441e-966c-4cd2fdad2f42.png#averageHue=%23f8f6f4&clientId=uc8bb7267-40f0-4&from=paste&height=311&id=u99116a08&originHeight=280&originWidth=1001&originalType=binary&ratio=0.8999999761581421&rotation=0&showTitle=false&size=122602&status=done&style=none&taskId=uf3bb054f-4c9b-4370-949e-df7a0d983f2&title=&width=1112.22225168605)

<a name="Ttni8"></a>
## 完整示例
```json
{
  "fileName": "test.pdf",
  "font": {
    "fontProgram": "STSong-Light",
    "encoding": "UniGB-UCS2-H"
  },
  "fontSize": 7,
  "pageSize": "A4",
  "rotate": false,
  "marginLeft": 36,
  "marginRight": 36,
  "marginTop": 36,
  "marginBottom": 36,
  "waterMask": {
    "fontColor": "LIGHT_GRAY",
    "fontSize": 20,
    "x": 0,
    "y": 0,
    "angle": 30,
    "text": "这里是水印内容",
    "xAxisElementSpacing": 200,
    "yAxisElementSpacing": 200
  },
  "header": {
    "fontColor": "LIGHT_GRAY",
    "fontSize": 20,
    "text": "这里是页眉",
    "marginLeft": 35,
    "marginTop": 50,
    "textAlignment": "LEFT",
    "image": {
      "base64": "data:image/png;base64,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",
      "fitWidth": 100,
      "fitHeight": 100,
      "marginLeft": 0,
      "marginTop": 50,
      "opacity": 0.5
    }
  },
  "footer": {
    "fontColor": "LIGHT_GRAY",
    "fontSize": 20,
    "text": "这里是页脚",
    "marginLeft": 35,
    "marginBottom": 50,
    "textAlignment": "LEFT"
  },
  "pageNumber":  {
    "fontColor": "LIGHT_GRAY",
    "fontSize": 10,
    "text": "页码 第 %s - %s 页",
    "marginRight": 100,
    "marginBottom": 35,
    "textAlignment": "CENTER"
  },
  "nodes": [
    {
      "type": "Paragraph",
      "text": "测试申请书-通用",
      "bold": true,
      "textAlignment": "CENTER",
      "fontSize": 21
    },
    {
      "type": "Paragraph",
      "text": "1.基础信息"
    },
    {
      "type": "Table",
      "width": 100,
      "columnSize": 4,
      "cells": [
        {"width": 10, "textAlignment":"CENTER", "elements": [{"type": "Paragraph", "text": "委托单位"}]},{"width": 40, "elements": [{"type": "Paragraph", "text": ""}]},{"width": 10, "textAlignment":"CENTER", "elements": [{"type": "Paragraph", "text": "委托单位地址"}]},{"width": 40, "elements": [{"type": "Paragraph", "text": ""}]},
        {"width": 10, "textAlignment":"CENTER", "elements": [{"type": "Paragraph", "text": "付款单位"}]},{"width": 40, "elements": [{"type": "Paragraph", "text": ""}]},{"width": 10, "textAlignment":"CENTER", "elements": [{"type": "Paragraph", "text": "付款单位地址"}]},{"width": 40, "elements": [{"type": "Paragraph", "text": ""}]}
      ]
    },
    {
      "type": "Table",
      "width": 100,
      "columnSize": 6,
      "cells": [
        {"width": 10, "textAlignment":"CENTER", "elements": [{"type": "Paragraph", "text": "联系人"}]},{"width": 23, "elements": [{"type": "Paragraph", "text": ""}]},{"width": 10, "textAlignment":"CENTER", "elements": [{"type": "Paragraph", "text": "联系人电话"}]},{"width": 23, "elements": [{"type": "Paragraph", "text": ""}]},{"width": 10, "textAlignment":"CENTER", "elements": [{"type": "Paragraph", "text": "联系人电子邮箱"}]},{"width": 24, "elements": [{"type": "Paragraph", "text": ""}]},
        {"width": 10, "textAlignment":"CENTER", "elements": [{"type": "Paragraph", "text": "SPU型号"}]},{"width": 23, "elements": [{"type": "Paragraph", "text": ""}]},{"width": 10, "textAlignment":"CENTER", "elements": [{"type": "Paragraph", "text": "SPU名称"}]},{"width": 23, "elements": [{"type": "Paragraph", "text": ""}]},{"width": 10, "textAlignment":"CENTER", "elements": [{"type": "Paragraph", "text": "品牌"}]},{"width": 24, "elements": [{"type": "Paragraph", "text": ""}]},
        {"width": 10, "textAlignment":"CENTER", "elements": [{"type": "Paragraph", "text": "样品量"}]},{"width": 23, "elements": [{"type": "Paragraph", "text": ""}]},{"width": 10, "textAlignment":"CENTER", "elements": [{"type": "Paragraph", "text": "批次号"}]},{"width": 23, "elements": [{"type": "Paragraph", "text": ""}]},{"width": 10, "textAlignment":"CENTER", "elements": [{"type": "Paragraph", "text": "快递单号"}]},{"width": 24, "elements": [{"type": "Paragraph", "text": ""}]}
      ]
    },
    {
      "type": "Paragraph",
      "text": "2.测试需求"
    },
    {
      "type": "Table",
      "width": 100,
      "chunkSize": 2,
      "chunkVertical": true,
      "columnSize": 3,
      "cells": [
        {"width": 4,"textAlignment":"CENTER","elements": [{"type": "Paragraph", "text": "序号"}]},{"width": 23,"textAlignment":"CENTER","elements": [{"type": "Paragraph", "text": "测试项目"}]},{"width": 23,"textAlignment":"CENTER","elements": [{"type": "Paragraph", "text": "测试标准"}]}
       ,{"width": 4,"textAlignment":"CENTER","elements": [{"type": "Paragraph", "text": "序号"}]},{"width": 23,"textAlignment":"CENTER","elements": [{"type": "Paragraph", "text": "测试项目"}]},{"width": 23,"textAlignment":"CENTER","elements": [{"type": "Paragraph", "text": "测试标准"}]}
         ,{"width": 4,"textAlignment":"CENTER","elements": [{"type": "Paragraph", "text": "1"}]},{"width": 23,"elements": [{"type": "Paragraph", "text": ""}]},{"width": 23,"elements": [{"type": "Paragraph", "text": ""}]}
         ,{"width": 4,"textAlignment":"CENTER","elements": [{"type": "Paragraph", "text": "2"}]},{"width": 23,"elements": [{"type": "Paragraph", "text": ""}]},{"width": 23,"elements": [{"type": "Paragraph", "text": ""}]}
         ,{"width": 4,"textAlignment":"CENTER","elements": [{"type": "Paragraph", "text": "3"}]},{"width": 23,"elements": [{"type": "Paragraph", "text": ""}]},{"width": 23,"elements": [{"type": "Paragraph", "text": ""}]}
         ,{"width": 4,"textAlignment":"CENTER","elements": [{"type": "Paragraph", "text": "4"}]},{"width": 23,"elements": [{"type": "Paragraph", "text": ""}]},{"width": 23,"elements": [{"type": "Paragraph", "text": ""}]}
         ,{"width": 4,"textAlignment":"CENTER","elements": [{"type": "Paragraph", "text": "5"}]},{"width": 23,"elements": [{"type": "Paragraph", "text": ""}]},{"width": 23,"elements": [{"type": "Paragraph", "text": ""}]}
         ,{"width": 4,"textAlignment":"CENTER","elements": [{"type": "Paragraph", "text": "6"}]},{"width": 23,"elements": [{"type": "Paragraph", "text": ""}]},{"width": 23,"elements": [{"type": "Paragraph", "text": ""}]}
         ,{"width": 4,"textAlignment":"CENTER","elements": [{"type": "Paragraph", "text": "7"}]},{"width": 23,"elements": [{"type": "Paragraph", "text": ""}]},{"width": 23,"elements": [{"type": "Paragraph", "text": ""}]}
         ,{"width": 4,"textAlignment":"CENTER","elements": [{"type": "Paragraph", "text": "8"}]},{"width": 23,"elements": [{"type": "Paragraph", "text": ""}]},{"width": 23,"elements": [{"type": "Paragraph", "text": ""}]}
         ,{"width": 4,"textAlignment":"CENTER","elements": [{"type": "Paragraph", "text": "9"}]},{"width": 23,"elements": [{"type": "Paragraph", "text": ""}]},{"width": 23,"elements": [{"type": "Paragraph", "text": ""}]}
        ,{"width": 4,"textAlignment":"CENTER","elements": [{"type": "Paragraph", "text": "10"}]},{"width": 23,"elements": [{"type": "Paragraph", "text": ""}]},{"width": 23,"elements": [{"type": "Paragraph", "text": ""}]}
      ]
    },
    {
      "type": "Table",
      "width": 100,
      "columnSize": 1,
      "cells": [
      {"width": 100,"elements": [{"type": "Paragraph", "text": "判定依据：按照产品执行标准判定，单项需要确认判定要求。"}]}
      ,{"width": 100,"elements": [{"type": "Paragraph", "text": "送检备注：硅胶：123 塑料：234硅胶：123 塑料：234硅胶：123 塑料：234硅胶：123 塑料：234硅胶：123 塑料：234硅胶：123 塑料：234硅胶：123 塑料：234硅胶：123 塑料：234硅胶：123 塑料：234硅胶：123 塑料：234硅胶：123 塑料：234硅胶：123 塑料：234硅胶：123 塑料：234硅胶：123 塑料：234硅胶：123 塑料：234"}]}
      ]
    },
    {
      "type": "Paragraph",
      "text": "3.其他"
    },
    {
      "type": "Table",
      "width": 100,
      "columnSize": 1,
      "cells": [
        {"width": 100,"elements": [
          {"type": "Paragraph", "text": "客户要求：1、资质要求： ","elements": [
            {
              "type": "CheckBox",
              "checked": true,
              "checkBoxType": "CROSS",
              "size": 7,
              "border": {
                "width": 1
              }
            },{
              "type": "Paragraph",
              "text": "CNAS章",
              "marginLeft":3,
              "marginRight": 3
            },
            {
              "type": "CheckBox",
              "checked": true,
              "checkBoxType": "CROSS",
              "size": 7,
              "border": {
                "width": 1
              }
            },
            {
              "type": "Paragraph",
              "text": "CMA章",
              "marginLeft":3
            }]},
          {"type": "Paragraph", "marginLeft":30,"text": "2、报告语言： ","elements": [
            {
              "type": "CheckBox",
              "checked": true,
              "checkBoxType": "CROSS",
              "size": 7,
              "border": {
                "width": 1
              }
            },{
              "type": "Paragraph",
              "text": "中文报告",
              "marginLeft":3,
              "marginRight": 3
            },{
              "type": "CheckBox",
              "checked": false,
              "checkBoxType": "CROSS",
              "size": 7,
              "border": {
                "width": 1
              }
            },{
              "type": "Paragraph",
              "text": "英文报告",
              "marginLeft":3
            }
          ]},
          {"type": "Paragraph", "marginLeft":30,"text": "3、报告出具方式：执行标准一份报告，2C一份报告，只有CMA一份"},
          {"type": "Paragraph", "marginLeft":30,"text": "4、服务类型：","elements": [
            {
              "type": "CheckBox",
              "checked": true,
              "checkBoxType": "CROSS",
              "size": 7,
              "border": {
                "width": 1
              }
            },{
              "type": "Paragraph",
              "text": "普通",
              "marginLeft":3,
              "marginRight": 3
            },{
              "type": "CheckBox",
              "checked": false,
              "checkBoxType": "CROSS",
              "size": 7,
              "border": {
                "width": 1
              }
            },{
              "type": "Paragraph",
              "text": "加急",
              "marginLeft":3
            }
          ]},
          {"type": "Paragraph", "marginLeft":30,"text": "5、是否需要判定：","elements": [
            {
              "type": "CheckBox",
              "checked": true,
              "checkBoxType": "CROSS",
              "size": 7,
              "border": {
                "width": 1
              }
            },{
              "type": "Paragraph",
              "text": "需要",
              "marginLeft":3,
              "marginRight": 3
            },{
              "type": "CheckBox",
              "checked": false,
              "checkBoxType": "CROSS",
              "size": 7,
              "border": {
                "width": 1
              }
            },{
              "type": "Paragraph",
              "text": "不需要",
              "marginLeft":3
            }
          ]},
          {"type": "Paragraph", "marginLeft":30,"text": "6、是否需要退样：","elements": [
            {
              "type": "CheckBox",
              "checked": true,
              "checkBoxType": "CROSS",
              "size": 7,
              "border": {
                "width": 1
              }
            },{
              "type": "Paragraph",
              "text": "不需要",
              "marginLeft":3,
              "marginRight": 3
            },{
              "type": "CheckBox",
              "checked": false,
              "checkBoxType": "CROSS",
              "size": 7,
              "border": {
                "width": 1
              }
            },{
              "type": "Paragraph",
              "text": "需要",
              "marginLeft":3
            }, {
              "type": "Paragraph",
              "text": "退样信息：",
              "marginLeft":8
            },{
              "type": "LineSeparator",
              "marginLeft": 1,
              "width": 10,
              "lineWidth": 0.5
            }
          ]}
        ]}
      ]
    },
    {
      "type": "Table",
      "width": 100,
      "columnSize": 2,
      "cells": [
        {"width": 50,"elements": [
          {"type": "Paragraph", "text": "委托方代表签名：","elements": [{"type": "Paragraph", "marginLeft":90, "text": "日期："}]}]},
        {"width": 50,"elements": [
          {"type": "Paragraph", "text": "受托方代表签名：","elements": [{"type": "Paragraph", "marginLeft":90, "text": "日期："}]}]}
      ]
    },
    {
      "type": "Paragraph",
      "text": "测试须知",
      "bold": true,
      "textAlignment": "CENTER",
      "fontSize": 21,
      "fontColor": "RED"
    },
    {
      "type": "Paragraph",
      "text": "1. 与我司合作过程中，品控部标准送检组人员为唯一对接窗口，在未获得我司书面授权允许的情况下不得以任何形式与其他人员私自对接，进行样品受理、报告修改等与测试相关的业务，如若发生，将停止后续的一切合作，造成不利影响的我司保留追究相关法律责任的权力。"
    },
    {
      "type": "Paragraph",
      "text": "2.委托方披露给受托方或受托方通过双方合作关系主动获悉的任何以及所有以口头或书面，或以其他任何形式披露的信息、资料或数据，包括但不限于产品信息、产品所含知识产权、产品测试内容及测试结果等各类基于甲方商业经营以及测试合作产生的，具有商业价值或涉及第三方个人隐私的内容予以保密，均不得主动向第三人披露。"
    },
    {
      "type": "Image",
      "base64": "data:image/png;base64,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",
      "fitWidth": 100,
      "fitHeight": 100,
      "opacity": 0.5
    },
    {
      "type": "Paragraph",
      "textAlignment": "CENTER",
      "elements": [{
        "type": "Paragraph",
        "text": "图片左边"
      },{
        "type": "Image",
        "base64": "data:image/png;base64,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",
        "fitWidth": 100,
        "fitHeight": 100,
        "opacity": 0.5
      }]
    },

    {
      "type": "Table",
      "width": 100,
      "columnSize": 2,
      "cells": [
        {"width": 50,"elements": [
          {"type": "Paragraph", "text": "委托方代表签名："}]},
        {"width": 50,"elements": [
          {"type": "Image",
            "base64": "data:image/png;base64,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",
            "fitWidth": 150,
            "fitHeight": 100,
            "opacity": 0.5}
        ]}
      ]
    }
  ]
}
```

![image.png](https://cdn.nlark.com/yuque/0/2024/png/25980294/1722326668955-dfa03ceb-4fd1-4dda-a4d8-0a758b5e3f0f.png#averageHue=%23f4f2f1&clientId=uc8bb7267-40f0-4&from=paste&height=940&id=u406baa42&originHeight=846&originWidth=604&originalType=binary&ratio=0.8999999761581421&rotation=0&showTitle=false&size=206927&status=done&style=none&taskId=u4bb58ccc-c1fc-469b-9b92-306a04eae04&title=&width=671.1111288894847)
<a name="mQhW4"></a>
## freemarker示例
制作模板，通过freemarket填充数据<br />普通数据使用了`${fileName}`，表格部分使用了`<#list projectList as project>xxx  ${project.sequence} xxx</#list>`
```json
{
  "fileName": "${fileName}",
  "font": {
    "fontProgram": "STSong-Light",
    "encoding": "UniGB-UCS2-H"
  },
  "fontSize": 7,
  "pageSize": "A4",
  "rotate": false,
  "marginLeft": 36,
  "marginRight": 36,
  "marginTop": 36,
  "marginBottom": 36,
  "header": {
    "fontColor": "LIGHT_GRAY",
    "fontSize": 20,
    "text": "这里是页眉",
    "marginLeft": 35,
    "marginTop": 50,
    "textAlignment": "LEFT",
    "image": {
      "base64": "${image}",
      "fitWidth": 100,
      "fitHeight": 100,
      "marginLeft": 0,
      "marginTop": 50,
      "opacity": 0.5
    }
  },
  "footer": {
    "fontColor": "LIGHT_GRAY",
    "fontSize": 20,
    "text": "这里是页脚",
    "marginLeft": 35,
    "marginBottom": 50,
    "textAlignment": "LEFT"
  },
  "waterMask": {
    "fontColor": "LIGHT_GRAY",
    "fontSize": 20,
    "x": 0,
    "y": 0,
    "angle": 30,
    "text": "这里是水印内容",
    "xAxisElementSpacing": 200,
    "yAxisElementSpacing": 200
  },
  "pageNumber":  {
    "fontColor": "LIGHT_GRAY",
    "fontSize": 10,
    "text": "页码 第 %s - %s 页",
    "marginRight": 100,
    "marginBottom": 35,
    "textAlignment": "CENTER"
  },
  "nodes": [
    {
      "type": "Paragraph",
      "text": "${title}",
      "bold": true,
      "textAlignment": "CENTER",
      "fontSize": 21
    },
    {
      "type": "Paragraph",
      "text": "1.基础信息"
    },
    {
      "type": "Table",
      "width": 100,
      "columnSize": 4,
      "cells": [
        {"width": 10, "textAlignment":"CENTER", "elements": [{"type": "Paragraph", "text": "委托单位"}]},{"width": 40, "elements": [{"type": "Paragraph", "text": "${requestUnit}"}]},{"width": 10, "textAlignment":"CENTER", "elements": [{"type": "Paragraph", "text": "委托单位地址"}]},{"width": 40, "elements": [{"type": "Paragraph", "text": "${requestAddress}"}]},
        {"width": 10, "textAlignment":"CENTER", "elements": [{"type": "Paragraph", "text": "付款单位"}]},{"width": 40, "elements": [{"type": "Paragraph", "text": "${paymentUnit}"}]},{"width": 10, "textAlignment":"CENTER", "elements": [{"type": "Paragraph", "text": "付款单位地址"}]},{"width": 40, "elements": [{"type": "Paragraph", "text": "${paymentAddress}"}]}
      ]
    },
    {
      "type": "Table",
      "width": 100,
      "columnSize": 6,
      "cells": [
        {"width": 10, "textAlignment":"CENTER", "elements": [{"type": "Paragraph", "text": "联系人"}]},{"width": 23, "elements": [{"type": "Paragraph", "text": "${contactPerson}"}]},{"width": 10, "textAlignment":"CENTER", "elements": [{"type": "Paragraph", "text": "联系人电话"}]},{"width": 23, "elements": [{"type": "Paragraph", "text": "${contactPhone}"}]},{"width": 11, "textAlignment":"CENTER", "elements": [{"type": "Paragraph", "text": "联系人电子邮箱"}]},{"width": 23, "elements": [{"type": "Paragraph", "text": ""}]},
        {"width": 10, "textAlignment":"CENTER", "elements": [{"type": "Paragraph", "text": "SPU型号"}]},{"width": 23, "elements": [{"type": "Paragraph", "text": ""}]},{"width": 10, "textAlignment":"CENTER", "elements": [{"type": "Paragraph", "text": "SPU名称"}]},{"width": 23, "elements": [{"type": "Paragraph", "text": ""}]},{"width": 10, "textAlignment":"CENTER", "elements": [{"type": "Paragraph", "text": "品牌"}]},{"width": 24, "elements": [{"type": "Paragraph", "text": ""}]},
        {"width": 10, "textAlignment":"CENTER", "elements": [{"type": "Paragraph", "text": "样品量"}]},{"width": 23, "elements": [{"type": "Paragraph", "text": ""}]},{"width": 10, "textAlignment":"CENTER", "elements": [{"type": "Paragraph", "text": "批次号"}]},{"width": 23, "elements": [{"type": "Paragraph", "text": ""}]},{"width": 10, "textAlignment":"CENTER", "elements": [{"type": "Paragraph", "text": "快递单号"}]},{"width": 24, "elements": [{"type": "Paragraph", "text": ""}]}
      ]
    },
    {
      "type": "Paragraph",
      "text": "2.测试需求"
    },
    {
      "type": "Table",
      "width": 100,
      "chunkSize": 2,
      "chunkVertical": true,
      "columnSize": 3,
      "cells": [
        {"width": 4,"textAlignment":"CENTER","elements": [{"type": "Paragraph", "text": "序号"}]},{"width": 23,"textAlignment":"CENTER","elements": [{"type": "Paragraph", "text": "测试项目"}]},{"width": 23,"textAlignment":"CENTER","elements": [{"type": "Paragraph", "text": "测试标准"}]}
      ,{"width": 4,"textAlignment":"CENTER","elements": [{"type": "Paragraph", "text": "序号"}]},{"width": 23,"textAlignment":"CENTER","elements": [{"type": "Paragraph", "text": "测试项目"}]},{"width": 23,"textAlignment":"CENTER","elements": [{"type": "Paragraph", "text": "测试标准"}]}
        <#list projectList as project>
      ,{"width": 4,"textAlignment":"CENTER","elements": [{"type": "Paragraph", "text": "${project.sequence}"}]},{"width": 23,"elements": [{"type": "Paragraph", "text": "${project.name}"}]},{"width": 23,"elements": [{"type": "Paragraph", "text": "${project.standard}"}]}
        </#list>
      ]
    },
    {
      "type": "Table",
      "width": 100,
      "columnSize": 1,
      "cells": [
        {"width": 100,"elements": [{"type": "Paragraph", "text": "判定依据：按照产品执行标准判定，单项需要确认判定要求。"}]}
      ,{"width": 100,"elements": [{"type": "Paragraph", "text": "送检备注：硅胶：123 塑料：234硅胶：123 塑料：234硅胶：123 塑料：234硅胶：123 塑料：234硅胶：123 塑料：234硅胶：123 塑料：234硅胶：123 塑料：234硅胶：123 塑料：234硅胶：123 塑料：234硅胶：123 塑料：234硅胶：123 塑料：234硅胶：123 塑料：234硅胶：123 塑料：234硅胶：123 塑料：234硅胶：123 塑料：234"}]}
      ]
    },
    {
      "type": "Paragraph",
      "text": "3.其他"
    },
    {
      "type": "Table",
      "width": 100,
      "columnSize": 1,
      "cells": [
        {"width": 100,"elements": [
          {"type": "Paragraph", "text": "客户要求：1、资质要求： ","elements": [
            {
              "type": "CheckBox",
              "checked": true,
              "checkBoxType": "CROSS",
              "size": 7,
              "border": {
                "width": 1
              }
            },{
              "type": "Paragraph",
              "text": "CNAS章",
              "marginLeft":3,
              "marginRight": 3
            },
            {
              "type": "CheckBox",
              "checked": true,
              "checkBoxType": "CROSS",
              "size": 7,
              "border": {
                "width": 1
              }
            },
            {
              "type": "Paragraph",
              "text": "CMA章",
              "marginLeft":3
            }]},
          {"type": "Paragraph", "marginLeft":30,"text": "2、报告语言： ","elements": [
            {
              "type": "CheckBox",
              "checked": true,
              "checkBoxType": "CROSS",
              "size": 7,
              "border": {
                "width": 1
              }
            },{
              "type": "Paragraph",
              "text": "中文报告",
              "marginLeft":3,
              "marginRight": 3
            },{
              "type": "CheckBox",
              "checked": false,
              "checkBoxType": "CROSS",
              "size": 7,
              "border": {
                "width": 1
              }
            },{
              "type": "Paragraph",
              "text": "英文报告",
              "marginLeft":3
            }
          ]},
          {"type": "Paragraph", "marginLeft":30,"text": "3、报告出具方式：执行标准一份报告，2C一份报告，只有CMA一份"},
          {"type": "Paragraph", "marginLeft":30,"text": "4、服务类型：","elements": [
            {
              "type": "CheckBox",
              "checked": true,
              "checkBoxType": "CROSS",
              "size": 7,
              "border": {
                "width": 1
              }
            },{
              "type": "Paragraph",
              "text": "普通",
              "marginLeft":3,
              "marginRight": 3
            },{
              "type": "CheckBox",
              "checked": false,
              "checkBoxType": "CROSS",
              "size": 7,
              "border": {
                "width": 1
              }
            },{
              "type": "Paragraph",
              "text": "加急",
              "marginLeft":3
            }
          ]},
          {"type": "Paragraph", "marginLeft":30,"text": "5、是否需要判定：","elements": [
            {
              "type": "CheckBox",
              "checked": true,
              "checkBoxType": "CROSS",
              "size": 7,
              "border": {
                "width": 1
              }
            },{
              "type": "Paragraph",
              "text": "需要",
              "marginLeft":3,
              "marginRight": 3
            },{
              "type": "CheckBox",
              "checked": false,
              "checkBoxType": "CROSS",
              "size": 7,
              "border": {
                "width": 1
              }
            },{
              "type": "Paragraph",
              "text": "不需要",
              "marginLeft":3
            }
          ]},
          {"type": "Paragraph", "marginLeft":30,"text": "6、是否需要退样：","elements": [
            {
              "type": "CheckBox",
              "checked": true,
              "checkBoxType": "STAR",
              "size": 7,
              "border": {
                "width": 1
              }
            },{
              "type": "Paragraph",
              "text": "不需要",
              "marginLeft":3,
              "marginRight": 3
            },{
              "type": "CheckBox",
              "checked": false,
              "checkBoxType": "STAR",
              "size": 7,
              "border": {
                "width": 1
              }
            },{
              "type": "Paragraph",
              "text": "需要",
              "marginLeft":3
            }, {
              "type": "Paragraph",
              "text": "退样信息：",
              "marginLeft":8
            },{
              "type": "LineSeparator",
              "marginLeft": 1,
              "width": 10,
              "lineWidth": 0.5
            }
          ]}
        ]}
      ]
    },
    {
      "type": "Table",
      "width": 100,
      "columnSize": 2,
      "cells": [
        {"width": 50,"elements": [
          {"type": "Paragraph", "text": "委托方代表签名：","elements": [{"type": "Paragraph", "marginLeft":90, "text": "日期："}]}]},
        {"width": 50,"elements": [
          {"type": "Paragraph", "text": "受托方代表签名：","elements": [{"type": "Paragraph", "marginLeft":90, "text": "日期："}]}]}
      ]
    },
    {
      "type": "Paragraph",
      "text": "测试须知",
      "bold": true,
      "textAlignment": "CENTER",
      "fontSize": 21,
      "fontColor": "RED"
    },
    {
      "type": "Paragraph",
      "text": "1. 与我司合作过程中，品控部标准送检组人员为唯一对接窗口，在未获得我司书面授权允许的情况下不得以任何形式与其他人员私自对接，进行样品受理、报告修改等与测试相关的业务，如若发生，将停止后续的一切合作，造成不利影响的我司保留追究相关法律责任的权力。"
    },
    {
      "type": "Paragraph",
      "text": "2.委托方披露给受托方或受托方通过双方合作关系主动获悉的任何以及所有以口头或书面，或以其他任何形式披露的信息、资料或数据，包括但不限于产品信息、产品所含知识产权、产品测试内容及测试结果等各类基于甲方商业经营以及测试合作产生的，具有商业价值或涉及第三方个人隐私的内容予以保密，均不得主动向第三人披露。"
    },
    {
      "type": "Image",
      "base64": "${image}",
      "fitWidth": 100,
      "fitHeight": 100,
      "opacity": 0.5
    }
  ]
}
```





