# RegexValidateUtil依赖库

提供字符串正则匹配逻辑，提供常规的手机号，邮箱，身份证，手机号的合法校验方法

## 逻辑详情

### isEmail

检查提供的字符串是否是有效的电子邮件地址  
入参：String email  
出参：Boolean  


### isMobile

判断传入的字符串是否为有效的国内手机号码  
入参：String mobile  
出参：Boolean  

### isMobilePhone

检查传入的字符串是否为有效的国际手机号码  
入参：mobile 待检查的手机号码字符串   
入参2：countryCode 国家代码
出参：Boolean  

检查传入的字符串是否为有效的手机号码

### isIdCard

检查传入的字符串是否为有效的身份证号码  
入参：String idCard  
出参：Boolean 

### isPhone

检查提供的字符串是否为电话号码  
支持两种格式的电话号码：带区号和不带区号   
入参：String str  
入参2：Boolean isArea 是否需要校验区号
出参：Boolean  

### isUrl

检查传入的字符串是否为有效的URL地址  
入参：String url  
出参：Boolean  

### IsChinese

检查传入的字符串是否为中文字符  
入参：String str  
出参：Boolean  

### isIPv4
判断传入的字符串是否为有效的IPv4地址  
入参：String ip  
出参：Boolean 


## 使用步骤说明

1.  应用引用依赖库
2.  无需配置
3.  逻辑调用示例截图

![img](Screenshot%202024-03-16%20at%2011.35.04.png)

## 应用演示链接

[使用了本依赖库的制品应用链接]

https://dev-testapp-qa.app.codewave.163.com/regexvalidate_page