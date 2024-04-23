# 依赖库名称
作者： superName-w

该依赖库主要用于数据脱敏。支持单条、多条数据脱敏及自定义数据脱敏。（如果参数为非法参数或空白参数则返回空白字符）

## 逻辑详情

主要分为两类逻辑：

##### 1、SingleDataMasking类中主要包含单条数据脱敏。

所有数据脱敏逻辑如果入参为非法参数或者为空都会返回空白字符串不会返回原字符主要考虑数据安全问题避免泄露

1. `mobilePhoneSingleDataMasking(String phone)`: 这个方法用于对手机号进行脱敏。它接受一个手机号、座机号、国际号码作为参数，参数类型为`String`，并返回脱敏后的手机号，返回值类型为`String`。例如，输入`18312345678`可能会返回`183****5678`。
2. `chineseNameSingleDataMasking(String chineseName)`: 这个方法用于对中国姓名进行脱敏。它接受一个中国姓名作为参数，参数类型为`String`，并返回脱敏后的中国姓名，返回值类型为`String`。例如，输入`张三丰`可能会返回`张**`。
3. `idCardNumSingleDataMasking(String idCardNum, Integer startSaveLength, Integer endSaveLength)`: 这个方法用于对身份证号码进行脱敏。它接受一个身份证号码和保留身份证前面的长度作及保留身份证后面的长度为参数，参数类型分别为`String`、`Integer`和`Integer`，并返回脱敏后的身份证号码，返回值类型为`String`。例如，输入`410101199001011234`和`6`、`4`可能会返回`410123********1234`。
4. `emailSingleDataMasking(String email)`: 这个方法用于对邮箱进行脱敏。它接受一个邮箱作为参数，参数类型为`String`，并返回脱敏后的邮箱，返回值类型为`String`。例如，输入`walter@163.com`可能会返回`w***********@163.com`。
5. `bankCardSingleDataMasking(String bankCard)`: 这个方法用于对银行卡进行脱敏。它接受一个银行卡号码作为参数，参数类型为`String`，并返回脱敏后的银行卡号码，返回值类型为`String`。例如，输入`6200123456789012`可能会返回`6200 **** **** 12`。
6. `addressSingleDataMasking(String address, Integer sensitiveSize)`: 这个方法用于对地址进行脱敏。它接受一个地址和从后往前的脱敏字符索引作为参数，参数类型分别为`String`和`Integer`，并返回脱敏后的地址，返回值类型为`String`。例如，输入`山东省泰安市花园路 123 号`和`4`可能会返回`山东省泰安市***路 123 号`。
7. `passwordSingleDataMasking(String password)`: 这个方法用于对密码进行脱敏。它接受一个密码作为参数，参数类型为`String`，并返回脱敏后的密码，返回值类型为`String`。例如，输入`12345678`可能会返回`************`。
8. `ipv4SingleDataMasking(String ipv4)`: 这个方法用于对 IPv4 进行脱敏。它接受一个 IPv4 地址作为参数，参数类型为`String`，并返回脱敏后的 IPv4 地址，返回值类型为`String`。例如，输入`192.168.0.1`可能会返回`192.*.*.*`。
9. `ipv6SingleDataMasking(String ipv6)`: 这个方法用于对 IPv6 进行脱敏。它接受一个 IPv6 地址作为参数，参数类型为`String`，并返回脱敏后的 IPv6 地址，返回值类型为`String`。例如，输入`FC00:0000:0000:0000:0000:0000:0000:0001`可能会返回`FC00:*:*:*:*:*:*:*:0001`。
10. `generalSingleDataMasking(String text, Integer start, Integer desensitizationLength, String maskChar)`: 这个方法用于进行自定义脱敏。它接受一个文本内容、开始脱敏的字符长度、结束脱敏的字符长度和脱敏符号作为参数，参数类型分别为`String`、`Integer`、`Integer`和`String`，并返回脱敏后的文本，返回值类型为`String`。例如，输入`hello world`、`5`、`3`和`*`可能会返回`hel*** w***d`。

##### 2、BulkDataMasking类中主要包含多条数据脱敏

所有数据脱敏逻辑如果入参为非法参数或者为空都会返回空白字符串列表（指的是字符串列表参数）

1. `mobilePhoneBulkDataMasking(List<String> phoneList)`: 这个方法用于对手机号列表进行批量脱敏。它接受一个手机号列表作为参数，类型为`List<String>`，并返回脱敏后的手机号列表，类型也为`List<String>`。例如，输入`["18312345678", "13987654321"]`可能会返回`["183****5678", "139****4321"]`。
2. `chineseNameBulkDataMasking(List<String> chineseNameList)`: 这个方法用于对中文姓名列表进行批量脱敏。它接受一个中文姓名列表作为参数，类型为`List<String>`，并返回脱敏后的中文姓名列表，类型同样为`List<String>`。例如，输入`["张三", "李四"]`可能会返回`["张*", "李*"]`。
3. `idCardNumBulkDataMasking(List<String> idCardNumList, Integer startSaveLength, Integer endSaveLength)`: 这个方法用于对身份证号码列表进行批量脱敏。它接受一个身份证号码列表、需要保留的身份证号码开头长度和末尾长度作为参数，参数类型分别为`List<String>`、`Integer`和`Integer`，并返回脱敏后的身份证号码列表，类型为`List<String>`。例如，输入`["410105199001011234", "410105198012035678"]`、`6`和`4`可能会返回`["4101051990**1234", "4101051980**5678"]`。
4. `emailBulkDataMasking(List<String> emailList)`: 这个方法用于对邮箱列表进行批量脱敏。它接受一个邮箱列表作为参数，类型为`List<String>`，并返回脱敏后的邮箱列表，类型同样为`List<String>`。例如，输入`["example@example.com", "another@example.org"]`可能会返回`["w*******@example.com", "w*******@example.org"]`。
5. `bankCardBulkDataMasking(List<String> bankCardList)`: 这个方法用于对银行卡列表进行批量脱敏。它接受一个银行卡列表作为参数，类型为`List<String>`，并返回脱敏后的银行卡列表，类型为`List<String>`。例如，输入`["6200123456789012", "6200987654321098"]`可能会返回`["6200 **** **** 12", "6200 **** **** 98"]`。
6. `addressBulkDataMasking(List<String> addressList, Integer sensitiveSize)`: 这个方法用于对地址列表进行批量脱敏。它接受一个地址列表和需要格式化为`*`的长度作为参数，参数类型分别为`List<String>`和`Integer`，并返回脱敏后的地址列表，类型为`List<String>`。例如，输入`["山东省济南市历下区解放路 123 号", "上海市浦东新区张江镇高科路 456 号"]`和`6`可能会返回`["山东省济南市历下区解**路 123 号", "上海市浦东新区张**镇高**路 456 号"]`。
7. `passwordBulkDataMasking(List<String> passwordList)`: 这个方法用于对密码列表进行批量脱敏。它接受一个密码列表作为参数，类型为`List<String>`，并返回脱敏后的密码列表，类型为`List<String>`。例如，输入`["password123", "secret456"]`可能会返回`["************", "************"]`。
8. `ipv4BulkDataMasking(List<String> ipv4List)`: 这个方法用于对 IPv4 地址列表进行批量脱敏。它接受一个 IPv4 地址列表作为参数，类型为`List<String>`，并返回脱敏后的 IPv4 地址列表，类型为`List<String>`。例如，输入`["192.168.0.1", "10.0.0.1"]`可能会返回`["192.*.*.1", "10.*.*.1"]`。
9. `ipv6BulkDataMasking(List<String> ipv6List)`: 这个方法用于对 IPv6 地址列表进行批量脱敏。它接受一个 IPv6 地址列表作为参数，类型为`List<String>`，并返回脱敏后的 IPv6 地址列表，类型为`List<String>`。例如，输入`["2001:0db8:85a3:0000:0000:8a2e:0370:7334", "2001:0db8:85a3:0000:0000:8a2e:0370:7335"]`可能会返回`["2001:0db8:85a3:*:*:8a2e:0370:7334", "2001:0db8:85a3:*:*:8a2e:0370:7335"]`。
10. `generalBulkDataMasking(List<String> textList, Integer start, Integer desensitizationLength, String maskChar)`: 这个方法用于进行自定义批量脱敏。它接受一个文本列表、开始位置、脱敏长度和脱敏字符作为参数，参数类型分别为`List<String>`、`Integer`、`Integer`和`String`，并返回脱敏后的文本列表，类型为`List<String>`。例如，输入`["This is a sample text.", "Another text for masking."]`、`5`、`3`和`X`可能会返回`["This is a sXmplX tXt.", "Another tXt fXr mXsKing."]`。

## 使用步骤说明

1. 应用引用依赖库

3. 逻辑调用示例截图

   单条数据脱敏
   
   ![1709736256327](https://github.com/superName-w/CodeWaveAssetCompetition2024/blob/b7d4bba501d0cc82cc7e7c36f78d3def72383b52/data-masking-util/assets/1710604099941.jpg)
   
   多条数据脱敏
   
   ![1711125211861](https://github.com/superName-w/CodeWaveAssetCompetition2024/blob/af76de893c1bc22577609df91af8f2db5429b04d/data-masking-util/assets/1711125211861.png)

![1711127016564](https://github.com/superName-w/CodeWaveAssetCompetition2024/blob/af76de893c1bc22577609df91af8f2db5429b04d/data-masking-util/assets/1711127016564.png)

## 应用演示链接

使用了本依赖库的应用的链接。

无法发布生产环境目前只能提供测试环境，考虑测试环境随时失效此处并未提供
