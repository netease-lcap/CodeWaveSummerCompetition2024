# 依赖库名称

Cookie操作依赖库，可实现对Cookie的读写清除等操作。

## 逻辑详情

`setCookie(NaslCookie naslCookie)`：
作用：在后端设置 Cookie。
入参：一个 `NaslCookie` 对象，包含 Cookie 的名称和值等信息。
入参类型：`NaslCookie`。
返回值：一个布尔值，表示设置 Cookie 是否成功。
返回值类型：Boolean。

`getCookie(String cookieName)`：
作用：根据 Cookie 名称获取对应的 Cookie 对象。
入参：Cookie 的名称（cookieName）。
入参类型：String。
返回值：一个 `NaslCookie` 对象，如果不存在该名称的 Cookie，则返回 null。
返回值类型：NaslCookie。

`clearCookie(String cookieName)`：
作用：清除指定名称的 Cookie。
入参：Cookie 的名称（cookieName）。
入参类型：String。
返回值：一个布尔值，表示清除操作是否成功。
返回值类型：Boolean。

`getCookieValue(String cookieName)`：
作用：根据 Cookie 名称获取对应的 Cookie 值。
入参：Cookie 的名称（cookieName）。
入参类型：String。
返回值：Cookie 的值，如果不存在该名称的 Cookie，则返回 null。
返回值类型：String。

`getCookieMap()`：
作用：将所有 Cookie 封装到 Map 中并返回。
返回值：一个包含所有 Cookie 信息的 Map。
返回值类型：Map<String, NaslCookie>。

## 使用步骤说明

1. 应用引用依赖库
2. 逻辑调用示例截图

![图片1](https://github.com/superName-w/CodeWaveAssetCompetition2024/blob/23d412f581d5936a53f8c8e96c7df5e843d26808/cookie-util/assets/1710948659782.png)

![图片2](https://github.com/superName-w/CodeWaveAssetCompetition2024/blob/7aa9e5b9e98ac0a00cdf58140b20ef410c42ad96/cookie-util/assets/1710948795020.jpg)


## 应用演示链接

使用了本依赖库的应用的链接。

