# 依赖库名称

该依赖库定义了多个方法用于进行不同类型的验证。每个方法都接受一个输入值（除了customValidate()方法），并使用[正则表达式](coco://sendMessage?ext={"s%24wiki_link"%3A"https%3A%2F%2Fm.baike.com%2Fwikiid%2F2753457736136585089"}&msg=正则表达式)来验证该值是否符合特定的格式要求。

## 逻辑详情

- `customValidate(String value, String regex)`：

  - 作用：根据给定的[正则表达式](coco://sendMessage?ext={"s%24wiki_link"%3A"https%3A%2F%2Fm.baike.com%2Fwikiid%2F2753457736136585089"}&msg=正则表达式)验证输入值。
  - 入参：待验证的输入字符串（value）和用于验证的正则表达式（regex）。
  - 入参类型：都是`String`类型。
  - 返回值：一个`RegexValidatorResult`对象，包含验证结果和相应的消息。
  - 返回值类型：`RegexValidatorResult`。

- `checkMobile(String mobile)`：

  - 作用：验证手机号码的格式。
  - 入参：手机号码字符串（mobile）。
  - 入参类型：`String`。
  - 返回值：一个`RegexValidatorResult`对象，指示验证结果和相应的消息。
  - 返回值类型：`RegexValidatorResult`。

- `checkIdCard(String idCard)`：

  - 作用：验证身份证号码的格式。
  - 入参：身份证号码字符串（idCard）。
  - 入参类型：`String`。
  - 返回值：一个`RegexValidatorResult`对象，表示验证结果和相关消息。
  - 返回值类型：`RegexValidatorResult`。

- `checkEmail(String email)`：

  - 作用：验证电子邮件地址的格式。
  - 入参：一个电子邮件地址字符串（email）。
  - 入参类型：String。
  - 返回值：一个 `RegexValidatorResult` 对象，包含验证结果和相应的消息。
  - 返回值类型：`RegexValidatorResult`。

- `checkPostcode(String postcode)`：

  - 作用：匹配中国邮政编码。
  - 入参：一个邮政编码字符串（postcode）。
  - 入参类型：String。
  - 返回值：一个 `RegexValidatorResult` 对象，包含验证结果和相应的消息。
  - 返回值类型：`RegexValidatorResult`。

- `checkDate(String birthday)`：

  - 作用：验证日期（年月日）的格式。
  - 入参：一个日期字符串（birthday）。
  - 入参类型：String。
  - 返回值：一个 `RegexValidatorResult` 对象，包含验证结果和相应的消息。
  - 返回值类型：`RegexValidatorResult`。

- ```
  checkDateTime(String dateTime)：
  ```

  - 作用：验证日期时间的格式。
  - 入参：一个日期时间字符串（dateTime）。
  - 入参类型：String。
  - 返回值：一个`RegexValidatorResult`对象，包含验证结果和相应的消息。
  - 返回值类型：`RegexValidatorResult`。

- ```
  checkInterAndZero(String digit)：
  ```

  - 作用：验证整数（包括 0）的格式。
  - 入参：一个整数字符串（digit）。
  - 入参类型：String。
  - 返回值：一个`RegexValidatorResult`对象，包含验证结果和相应的消息。
  - 返回值类型：`RegexValidatorResult`。

- ```
  checkIntegerNotZero(String digit)：
  ```

  - 作用：验证正整数或负整数（不包括 0）的格式。
  - 入参：一个整数字符串（digit）。
  - 入参类型：String。
  - 返回值：一个`RegexValidatorResult`对象，包含验证结果和相应的消息。
  - 返回值类型：`RegexValidatorResult`。

- ```
  checkDigit(String digit)：
  ```

  - 作用：验证整数和小数的格式。
  - 入参：一个数字字符串（digit）。
  - 入参类型：String。
  - 返回值：一个`RegexValidatorResult`对象，包含验证结果和相应的消息。
  - 返回值类型：`RegexValidatorResult`。

- ```
  checkDecimal(String decimal)：
  ```

  - 作用：验证传入的字符串是否符合小数的格式。
  - 入参：一个字符串（decimal），表示要验证的数值。
  - 入参类型：String。
  - 返回值：一个 `RegexValidatorResult` 对象，包含验证结果和相应的消息。
  - 返回值类型：`RegexValidatorResult`。

- ```
  checkNegativeDecimal(String decimal)：
  ```

  - 作用：验证传入的字符串是否符合负小数的格式。
  - 入参：一个字符串（decimal），表示要验证的数值。
  - 入参类型：String。
  - 返回值：一个 `RegexValidatorResult` 对象，包含验证结果和相应的消息。
  - 返回值类型：`RegexValidatorResult`。

- ```
  checkChinese(String chinese)：
  ```

  - 作用：验证传入的字符串是否只包含中文字符。
  - 入参：一个字符串（chinese），表示要验证的中文字符。
  - 入参类型：String。
  - 返回值：一个 `RegexValidatorResult` 对象，包含验证结果和相应的消息。
  - 返回值类型：`RegexValidatorResult`。

- ```
  checkLetter(String letter)：
  ```

  - 作用：验证传入的字符串是否只包含字母（包括大小写）。
  - 入参：一个字符串（letter），表示要验证的字母。
  - 入参类型：String。
  - 返回值：一个 `RegexValidatorResult` 对象，包含验证结果和相应的消息。
  - 返回值类型：`RegexValidatorResult`。

- ```
  checkMacAddress(String macAddress)：
  ```

  - 作用：验证传入的字符串是否符合 [MAC](coco://sendMessage?ext={"s%24wiki_link"%3A"https%3A%2F%2Fm.baike.com%2Fwikiid%2F8792875024474215202"}&msg=MAC) 地址的格式。
  - 入参：一个字符串（macAddress），表示要验证的 MAC 地址。
  - 入参类型：String。
  - 返回值：一个 `RegexValidatorResult` 对象，包含验证结果和相应的消息。
  - 返回值类型：`RegexValidatorResult`。

- ```
  checkIPv4Address(String ipAddress)：
  ```

  - 作用：验证传入的字符串是否符合 IPv4 地址的格式。
  - 入参：一个字符串（ipAddress），表示要验证的 IPv4 地址。
  - 入参类型：String。
  - 返回值：一个 `RegexValidatorResult` 对象，包含验证结果和相应的消息。
  - 返回值类型：`RegexValidatorResult`

- checkIPv6Address(String ipv6Address)`：

  - 作用：验证 IPv6 地址的格式。
  - 入参：一个 IPv6 地址字符串（ipv6Address）。
  - 入参类型：String。
  - 返回值：一个 RegexValidatorResult 对象，包含验证结果和相应的消息。
  - 返回值类型：RegexValidatorResult。

- `checkPasswordLength8Easy(String password)`：

  - 作用：验证密码的格式，要求长度至少为 8 位，且必须包含字母和数字。
  - 入参：要验证的密码字符串（password）。
  - 入参类型：String。
  - 返回值：一个 RegexValidatorResult 对象，包含验证结果和相应的消息。
  - 返回值类型：RegexValidatorResult。

- `checkPasswordLength6Easy(String password)`：

  - 作用：验证密码的格式，要求长度至少为 6 位，且必须包含字母和数字。
  - 入参：要验证的密码字符串（password）。
  - 入参类型：String。
  - 返回值：一个 RegexValidatorResult 对象，包含验证结果和相应的消息。
  - 返回值类型：RegexValidatorResult。

- `checkPasswordLength8Difficult(String password)`：

  - 作用：验证密码的格式，要求长度至少为 8 位，必须包含特殊字符、字母和数字。
  - 入参：要验证的密码字符串（password）。
  - 入参类型：String。
  - 返回值：一个 RegexValidatorResult 对象，包含验证结果和相应的消息。
  - 返回值类型：RegexValidatorResult。

- `checkPasswordLength6Difficult(String password)`：

  - 作用：验证密码的格式，要求长度至少为 6 位，必须包含特殊字符、字母和数字。
  - 入参：要验证的密码字符串（password）。
  - 入参类型：String。
  - 返回值：一个 RegexValidatorResult 对象，包含验证结果和相应的消息。
  - 返回值类型：RegexValidatorResult。

这些方法都返回一个 `RegexValidatorResult` 对象，其中包含一个布尔值表示验证结果（是否符合规则），以及一个消息字段，用于提供有关验证结果的详细信息。

## 使用步骤说明

1. 应用引用依赖库

3. 逻辑调用示例截图

   ![1709736256327](https://github.com/superName-w/CodeWaveAssetCompetition2024/blob/73c25e396702406d368ff43deeb4597069982a87/regex-validator-util/assets/img.png)

## 应用演示链接

使用了本依赖库的应用的链接。

无法发布生产环境目前只能提供测试环境，考虑测试环境随时失效此处并未提供
