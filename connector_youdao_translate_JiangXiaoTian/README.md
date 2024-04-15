# 有道翻译连接器

## 功能概述

有道翻译 API，为广大开发者提供开放接口。您的应用或网站可通过有道翻译 API，构建丰富多样的功能或应用，为用户带来即时，准确，方便的查词或翻译体验，从而降低语言理解与应用门槛。有道翻译连接器用于调用有道翻译 API。

调用有道翻译 API，需要接入有道翻译服务，请参考：[有道翻译-接入服务](https://ai.youdao.com/doc.s#guide)

## 功能介绍

用于将给定的文本，从源语言翻译为目标语言。支持单条和批量文本翻译。

| 操作标识         | 操作名称     | 对应有道翻译接口文档              |
| ---------------- | ------------ | --------------------------------- |
| translation      | 单条文本翻译 | https://fanyi.youdao.com/openapi/ |
| translationBatch | 批量文本翻译 | https://fanyi.youdao.com/openapi/ |

### translation 标识说明

`translation 标识` 入参说明

| 参数名 | 类型   | 是否必填 | 描述           | 备注            |
| ------ | ------ | -------- | -------------- | --------------- |
| q      | String | 是       | 需要翻译的文本 | 单条文本翻译    |
| from   | String | 是       | 翻译源语言     | 可设置为 auto   |
| to     | String | 是       | 翻译目标语言   | 不可设置为 auto |

`translation 标识` 出参说明

| 参数名      | 类型   | 描述     | 备注                                                           |
| ----------- | ------ | -------- | -------------------------------------------------------------- |
| from        | String | 源语言   | 返回用户指定的语言，或者自动检测出的语种（源语言设为 auto 时） |
| to          | String | 目标语言 | 返回用户指定的目标语言                                         |
| query       | String | 原文     | 需要翻译的文本                                                 |
| translation | String | 译文     | 翻译结果                                                       |
| errorCode   | String | 错误码   | 一定存在，正确时为 0                                           |
| errorMsg    | String | 错误信息 | 仅当存在错误时有值                                             |

### translationBatch 标识说明

`translationBatch 标识` 入参说明

| 参数名 | 类型         | 是否必填 | 描述               | 备注            |
| ------ | ------------ | -------- | ------------------ | --------------- |
| q      | List<String> | 是       | 需要翻译的文本列表 | 批量文本翻译    |
| from   | String       | 是       | 翻译源语言         | 可设置为 auto   |
| to     | String       | 是       | 翻译目标语言       | 不可设置为 auto |

`translationBatch 标识` 出参说明

| 参数名           | 类型            | 描述     | 备注                                                           |
| ---------------- | --------------- | -------- | -------------------------------------------------------------- |
| from             | String          | 源语言   | 返回用户指定的语言，或者自动检测出的语种（源语言设为 auto 时） |
| to               | String          | 目标语言 | 返回用户指定的目标语言                                         |
| translateResults | List<Translate> | 翻译结果 | 返回翻译结果，包括 `query` 和 `translation` 字段               |
| errorCode        | String          | 错误码   | 一定存在，正确时为 0                                           |
| errorMsg         | String          | 错误信息 | 仅当存在错误时有值                                             |

`Translate`结构说明

| 字段名      | 类型   | 说明 |
| ----------- | ------ | ---- |
| query       | String | 原文 |
| translation | String | 译文 |

## 操作示例

### 1. 添加连接器

添加连接器并填入`appid`（应用 ID）和`secretKey`（密钥）。

![image](youdao_translate_001.png)

### 2. 调用连接器

示例：使用有道翻译连接器将中文翻译成英文。

创建服务端逻辑，添加输入参数`params`，类型为`String`，代表需要翻译的文本，调用有道翻译连接器，操作选择`translation`(单条文本翻译)，`from参数`输入文本`zh-CHS`，`to参数`输入文本`en`，创建局部变量`data`接收连接器调用结果。

![image](youdao_translate_002.png)

添加输出参数`result`，对接收的结果`data`进行匹配，当结果正确返回时，将`data`赋值给`result`；当结果返回`Error`时，输出日志。

![image](youdao_translate_003.png)

创建页面如下图所示，使用`多行输入组件`接收用户输入的需要翻译的文本，使用`文本组件`展示翻译结果。

![image](youdao_translate_004.png)

在页面创建`text局部变量`用于绑定多行输入组件的值，创建`result局部变量`用于绑定翻译结果。

![image](youdao_translate_005.png)

翻译按钮添加事件逻辑，调用之前创建的服务端逻辑。对调用返回结果进行判断，当结果中错误码不为`字符串 0`时，弹出消息；当调用结果中错误码为`字符串 0`时，将翻译结果赋值给`result局部变量`。

![image](youdao_translate_006.png)

预览应用，输入需要翻译的文本，点击翻译，即可差看到翻译结果。

![image](youdao_translate_007.png)

## 支持语种列表

常见语种列表

| 名称     | 代码   |
| -------- | ------ |
| 自动检测 | auto   |
| 中文     | zh-CHS |
| 中文繁体 | zh-CHT |
| 英文     | en     |
| 法文     | fr     |
| 西班牙文 | es     |
| 俄文     | ru     |
| 阿拉伯文 | ar     |

更多有道翻译支持语种列表请参考[如何调用接口-批量文本翻译-支持语言](https://fanyi.youdao.com/openapi/)
