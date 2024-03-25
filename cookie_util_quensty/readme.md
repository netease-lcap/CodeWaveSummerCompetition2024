复制本文件到个人依赖库文件夹下，按照个人依赖库填写模板。本模板勿删，勿动
# 依赖库名称
cookie工具
**依赖库设计**

这个依赖库提供了操作cookie的逻辑，方便开发者在低开平台中存取cookie

**主要特性**

- **逻辑一：getCookie** 设置cookie
- **逻辑二：setCookie** 根据cookie名获取值
- **逻辑三：removeCookie** 删除指定cookie

## 使用说明

### 逻辑

- **setCookie(key,value,expire,path):** 设置cookie，key-cookie名称，必填，value-cookie值，必填，expire-过期时间，单位秒，默认会话结束时，选填，path-cookie作用域，默认请求为域名，选填，返回cookie值
- **getCookie(key):** 获取cookie值，key-cookie名称，必填，result-返回cookie值
- **removeCookie(key):** 删除cookie，key-cookie名称，必填

## 应用演示链接

[查看示例演示](https://dev-helloworld-jorchi.app.codewave.163.com/dashboard/cookieUtil)
