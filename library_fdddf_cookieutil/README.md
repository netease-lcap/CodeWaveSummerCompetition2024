# CookieUtil

**依赖库设计**

这个依赖库旨在提供Cookie设置、获取和删除的逻辑。

**主要特性**

- **fdddf-cookieutil/Cookie操作逻辑：**


**特性 1：** 支持Cookie设置，支持参数设置path, domain, secure, expires, samesite。

**特性 2：** 支持Cookie获取。

**特性 3：** 支持Cookie删除。


### 逻辑

- **getCookie(name):** 获取Cookie, name必填，返回值string

- **setCookie(name, value, path, domain, expires, secure, sameSite):** 设置Cookie, name/value必填，其他参数选填

    * name: cookie名称
    * value: cookie值
    * path: cookie路径
    * domain: cookie域名
    * expires: cookie过期时间
    * secure: https
    * sameSite: 是否跨站，strict,lax,none

- **removeCookie(name, path, domain):** 删除Cookie，name必填，其他参数选填


## 应用演示链接

[查看示例演示](https://dev-testapp-qa.app.codewave.163.com/cookietest)

![img](./Screenshot%202024-03-21%20at%2001.13.57.png)