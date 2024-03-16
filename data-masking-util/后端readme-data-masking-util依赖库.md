# 依赖库名称

该依赖库主要用于数据脱敏。支持单条、多条数据脱敏及自定义数据脱敏。（如果参数为非法参数或空白参数则返回空白字符）

## 逻辑详情

主要分为两类逻辑：

##### 1、SingleDataMasking类中主要包含单条数据脱敏。

所有数据脱敏逻辑如果入参为非法参数或者为空都会返回空白字符串不会返回原字符主要考虑数据安全问题避免泄露

- `mobilePhoneSingleDataMasking()`：用于手机号的脱敏，返回屏蔽了部分号码的手机号字符串。
- `chineseNameSingleDataMasking()`：用于中国姓名的脱敏，返回屏蔽了部分姓名的姓名字符串。
- `idCardNumSingleDataMasking()`：用于身份证号码的脱敏，根据提供的起始保留长度和末尾保留长度，返回脱敏后的身份证号码字符串。
- `emailSingleDataMasking()`：用于邮箱的脱敏，返回屏蔽了部分邮箱地址的邮箱字符串。
- `bankCardSingleDataMasking()`：用于银行卡的脱敏，返回屏蔽了部分银行卡号码的银行卡字符串。
- `addressSingleDataMasking()`：用于地址的脱敏，根据提供的敏感长度，返回屏蔽了部分地址的地址字符串。
- `passwordSingleDataMasking()`：用于密码的脱敏，返回用星号填充的密码字符串。
- `ipv4SingleDataMasking()`：用于 IPv4 地址的脱敏，返回屏蔽了部分 IPv4 地址的 IPv4 字符串。
- `ipv6SingleDataMasking()`：用于 IPv6 地址的脱敏，返回屏蔽了部分 IPv6 地址的 IPv6 字符串。
- `generalSingleDataMasking()`：用于自定义脱敏，可以指定脱敏文本、开始位置、脱敏长度和脱敏字符，返回自定义脱敏后的字符串。

##### 2、BulkDataMasking类中主要包含多条数据脱敏

所有数据脱敏逻辑如果入参为非法参数或者为空都会返回空白字符串列表（指的是字符串列表参数）

1. `mobilePhoneBulkDataMasking`：对手机号进行批量脱敏，将中间几位数字用`*`代替。
2. `chineseNameBulkDataMasking`：对中国姓名进行批量脱敏，只保留姓。
3. `idCardNumBulkDataMasking`：对身份证号码进行批量脱敏，根据指定的开头和末尾保留长度进行脱敏。
4. `emailBulkDataMasking`：对邮箱进行批量脱敏，将中间部分用`*`代替。
5. `bankCardBulkDataMasking`：对银行卡进行批量脱敏，将中间部分用`*`代替。
6. `addressBulkDataMasking`：对地址进行批量脱敏，将指定长度的部分用`*`代替。
7. `passwordBulkDataMasking`：对密码进行批量脱敏，用`*`代替所有字符。
8. `ipv4BulkDataMasking`：对 IPv4 地址进行批量脱敏，将中间部分用`.*.`代替。
9. `ipv6BulkDataMasking`：对 IPv6 地址进行批量脱敏，将中间部分用`:`代替。
10. `generalBulkDataMasking`：这是一个通用的脱敏方法，可以自定义脱敏的内容、开始位置、脱敏长度和脱敏字符。

## 使用步骤说明

1. 应用引用依赖库

2. 配置应用配置参数（如果有的话）

3. 逻辑调用示例截图

   ![1709736256327](https://github.com/superName-w/CodeWaveAssetCompetition2024/blob/2f8abdcb6a7eb2411e02fb0e4103e5e92fc51352/data-masking-util/assets/1710604099941.jpg)

## 应用演示链接

使用了本依赖库的应用的链接。

无法发布生产环境目前只能提供测试环境，考虑测试环境随时失效此处并未提供
