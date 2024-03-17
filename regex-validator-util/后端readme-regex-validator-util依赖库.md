# 依赖库名称

该依赖库定义了多个方法用于进行不同类型的验证。每个方法都接受一个输入值（除了customValidate()方法），并使用[正则表达式](coco://sendMessage?ext={"s%24wiki_link"%3A"https%3A%2F%2Fm.baike.com%2Fwikiid%2F2753457736136585089"}&msg=正则表达式)来验证该值是否符合特定的格式要求。

## 逻辑详情

- `customValidate` 方法用于验证输入值是否符合给定的[正则表达式](coco://sendMessage?ext={"s%24wiki_link"%3A"https%3A%2F%2Fm.baike.com%2Fwikiid%2F2753457736136585089"}&msg=正则表达式)。
- `checkMobile` 方法用于验证手机号码是否符合特定的格式。
- `checkIdCard` 方法用于验证身份证号码是否符合特定的格式。
- `checkEmail` 方法用于验证电子邮件地址是否符合标准的格式。
- `checkPostcode` 方法用于验证中国邮政编码是否符合特定的格式。
- `checkDate`：验证日期格式，如`1992-09-03`或`1992.09.03`。
- `checkDateTime`：验证日期时间格式，如`1992-09-03 12:22:21`。
- `checkInterAndZero`：验证整数，包括 0。
- `checkIntegerNotZero`：验证正整数或负整数，但不包括 0。
- `checkDigit`：验证整数和小数。
- `checkDecimal`：验证小数。
- `checkPositiveDecimal`：验证正的小数。
- `checkNegativeDecimal` ：验证负的小数。
- `checkChinese`：验证中文字符的格式。
- `checkLetter`：验证字母的格式。
- `checkMacAddress`：验证 [MAC](coco://sendMessage?ext={"s%24wiki_link"%3A"https%3A%2F%2Fm.baike.com%2Fwikiid%2F8792875024474215202"}&msg=MAC) 地址的格式。
- `checkIPv4Address`：验证 IPv4 地址的格式。
- `checkIPv6Address`：验证 IPv6 地址的格式。
- `checkPasswordLength8Easy`：验证密码的长度至少为 8 位，且必须包含字母和数字。
- `checkPasswordLength6Easy`：验证密码的长度至少为 6 位，且必须包含字母和数字。
- `checkPasswordLength8Difficult`：验证密码的长度至少为 8 位，且必须包含特殊字符、字母和数字。
- checkPasswordLength6Difficult`：验证密码的长度至少为 6 位，且必须包含特殊字符、字母和数字。

这些方法都返回一个 `RegexValidatorResult` 对象，其中包含一个布尔值表示验证结果（是否符合规则），以及一个消息字段，用于提供有关验证结果的详细信息。

## 使用步骤说明

1. 应用引用依赖库

2. 配置应用配置参数（如果有的话）

3. 逻辑调用示例截图

   ![1709736256327](https://github.com/superName-w/CodeWaveAssetCompetition2024/blob/2f8abdcb6a7eb2411e02fb0e4103e5e92fc51352/data-masking-util/assets/1710604099941.jpg)

## 应用演示链接

使用了本依赖库的应用的链接。

无法发布生产环境目前只能提供测试环境，考虑测试环境随时失效此处并未提供
