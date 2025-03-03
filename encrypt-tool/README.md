# encrypt-tool
封装各种加解密算法，方便制品使用。

# 接口详情
## DecryptUtil.decryptWithBase64AndDes
先base64解密,再des解密
入参：

| 属性              | 类型     | 描述          |
|-----------------|--------|-------------|
| key             | String | 密钥          |
| encryptedString | String | base64编码的密文 |
出参：

| 属性 | 类型     | 描述      |
|----|--------|---------|
|    | String | 解密后的字符串 |

## DecryptUtil.decryptWithAESAndBase64
先base64解密，再aes解密
入参：

| 属性              | 类型     | 描述          |
|-----------------|--------|-------------|
| key             | String | 密钥          |
| encryptedString | String | base64编码的密文 |
出参：

| 属性 | 类型     | 描述      |
|----|--------|---------|
|    | String | 解密后的字符串 |

## DecryptUtil.decryptWithMySqlAESAndHex
先hex解码，然后使用mysql格式的aes解密。
与`SELECT CONVERT(AES_DECRYPT(UNHEX('ADE307F5EF0053889107C28441170072'), 'mykey') USING utf8);`等价。
入参：

| 属性              | 类型     | 描述        |
|-----------------|--------|-----------|
| key             | String | 密钥        |
| encryptedString | String | hex编码后的密文 |
出参：

| 属性 | 类型     | 描述      |
|----|--------|---------|
|    | String | 解密后的字符串 |
## DecryptUtil.decryptWithBase64
base64解密
入参：

| 属性              | 类型     | 描述 |
|-----------------|--------|----|
| encryptedString | String | 密文 |
出参：

| 属性 | 类型     | 描述 |
|----|--------|----|
|    | String |    |
## DecryptUtil.decryptWithSM2C132
国密sm2 c1c3c2 解密
入参：

| 属性               | 类型     | 描述                               |
|------------------|--------|----------------------------------|
| privateKey       | String | 私钥                               |
| keyEncodeType    | String | 私钥编码方式: hex or base64            |
| base64Str        | String | 密文，可以是hex编码 or base64编码，通过type指定 |
| sourceEncodeType | String | 指定密文的编码方式：hex or base64          |
出参：

| 属性 | 类型     | 描述 |
|----|--------|----|
|    | String | 明文 |
## DecryptUtil.decryptWithSM2C123
国密sm2 c1c2c3 解密
入参：

| 属性               | 类型     | 描述                               |
|------------------|--------|----------------------------------|
| privateKey       | String | 私钥                               |
| keyEncodeType    | String | 私钥编码方式: hex or base64            |
| base64Str        | String | 密文，可以是hex编码 or base64编码，通过type指定 |
| sourceEncodeType | String | 指定密文的编码方式：hex or base64          |
出参：

| 属性 | 类型     | 描述 |
|----|--------|----|
|    | String | 明文 |
## DecryptUtil.decryptWithSM4
国密sm4 解密
入参：

| 属性               | 类型     | 描述                               |
|------------------|--------|----------------------------------|
| key              | String | 密钥                               |
| keyEncodeType    | String | 密钥编码方式: hex or base64            |
| base64Str        | String | 密文，可以是hex编码 or base64编码，通过type指定 |
| sourceEncodeType | String | 指定密文的编码方式：hex or base64          |
出参：

| 属性 | 类型     | 描述 |
|----|--------|----|
|    | String | 明文 |
## EncryptUtil.encryptWithDes
des加密（DESede/ECB/PKCS5Padding）
入参：

| 属性           | 类型     | 描述 |
|--------------|--------|----|
| key          | String | 密钥 |
| sourceString | String | 明文 |
出参：

| 属性 | 类型     | 描述 |
|----|--------|----|
|    | String | 密文 |
## EncryptUtil.encryptWithDesAndHex
des加密（DES/CBC/PKCS5Padding），然后再进行hex编码
入参：

| 属性           | 类型     | 描述             |
|--------------|--------|----------------|
| key8         | String | 密钥（这里要求长度8个字符） |
| sourceString | String | 待加密字符串         |
出参：

| 属性 | 类型     | 描述         |
|----|--------|------------|
|    | String | 加密后的字符串hex |
## EncryptUtil.encryptWithDesAndBase64
先通过des加密(DESede/ECB/PKCS5Padding)，再进行base64加密
入参：

| 属性           | 类型     | 描述     |
|--------------|--------|--------|
| key          | String | 密钥     |
| sourceString | String | 待加密字符串 |
出参：

| 属性 | 类型     | 描述      |
|----|--------|---------|
|    | String | 加密后的字符串 |
## EncryptUtil.encryptWithMD5AndBase64
先通过MD5加密，再进行base64加密
入参：

| 属性           | 类型     | 描述     |
|--------------|--------|--------|
| sourceString | String | 待加密字符串 |
出参：

| 属性 | 类型     | 描述      |
|----|--------|---------|
|    | String | 加密后的字符串 |
## EncryptUtil.encryptWithSHA1AndHexLowerCase
先通过SHA1加密，再转换为16进制字符串，并将所有字符串转为小写
入参：

| 属性           | 类型     | 描述     |
|--------------|--------|--------|
| sourceString | String | 待加密字符串 |
出参：

| 属性 | 类型     | 描述      |
|----|--------|---------|
|    | String | 加密后的字符串 |
## EncryptUtil.encryptWithSM3AndHexLowerCase
SM3加密后转16进制小写
入参：

| 属性           | 类型     | 描述     |
|--------------|--------|--------|
| sourceStr | String | 待加密字符串 |
出参：

| 属性 | 类型     | 描述      |
|----|--------|---------|
|    | String | 加密后的字符串 |
## EncryptUtil.encryptWithSHA256AndBase64
先通过HMAC-Sha256进行加密，再进行base64加密
入参：

| 属性 | 类型 | 描述 |
|----|----|----|
| key   | String   |    |
|  sourceString  |  String  |    |
出参：

| 属性 | 类型 | 描述 |
|----|----|----|
|    | String   |    |
## EncryptUtil.encryptWithMD5AndSHA1Base64

入参：

| 属性 | 类型 | 描述 |
|----|----|----|
|    |    |    |
|    |    |    |
出参：

| 属性 | 类型 | 描述 |
|----|----|----|
|    |    |    |
## EncryptUtil.encryptWithSHA256AndHexLowerCase

入参：

| 属性 | 类型 | 描述 |
|----|----|----|
|    |    |    |
|    |    |    |
出参：

| 属性 | 类型 | 描述 |
|----|----|----|
|    |    |    |
## EncryptUtil.encryptWithSHA256AndGetTopEightToHexLowerCase

入参：

| 属性 | 类型 | 描述 |
|----|----|----|
|    |    |    |
|    |    |    |
出参：

| 属性 | 类型 | 描述 |
|----|----|----|
|    |    |    |
## EncryptUtil.encryptWithSHA256ToHexLowerCase

入参：

| 属性 | 类型 | 描述 |
|----|----|----|
|    |    |    |
|    |    |    |
出参：

| 属性 | 类型 | 描述 |
|----|----|----|
|    |    |    |
## EncryptUtil.encryptWithAESAndBase64

入参：

| 属性 | 类型 | 描述 |
|----|----|----|
|    |    |    |
|    |    |    |
出参：

| 属性 | 类型 | 描述 |
|----|----|----|
|    |    |    |
## EncryptUtil.encryptWithMySqlAESAndHex

入参：

| 属性 | 类型 | 描述 |
|----|----|----|
|    |    |    |
|    |    |    |
出参：

| 属性 | 类型 | 描述 |
|----|----|----|
|    |    |    |
## EncryptUtil.encryptWithBase64

入参：

| 属性 | 类型 | 描述 |
|----|----|----|
|    |    |    |
|    |    |    |
出参：

| 属性 | 类型 | 描述 |
|----|----|----|
|    |    |    |
## EncryptUtil.encryptWithSM2C132

入参：

| 属性 | 类型 | 描述 |
|----|----|----|
|    |    |    |
|    |    |    |
出参：

| 属性 | 类型 | 描述 |
|----|----|----|
|    |    |    |
## EncryptUtil.encryptWithSM2C123

入参：

| 属性 | 类型 | 描述 |
|----|----|----|
|    |    |    |
|    |    |    |
出参：

| 属性 | 类型 | 描述 |
|----|----|----|
|    |    |    |
## EncryptUtil.encryptWithSM4

入参：

| 属性 | 类型 | 描述 |
|----|----|----|
|    |    |    |
|    |    |    |
出参：

| 属性 | 类型 | 描述 |
|----|----|----|
|    |    |    |
## EncryptUtil.encryptWithSM3Base64

入参：

| 属性 | 类型 | 描述 |
|----|----|----|
|    |    |    |
|    |    |    |
出参：

| 属性 | 类型 | 描述 |
|----|----|----|
|    |    |    |
## MD5Util.encryptWithMD5

入参：

| 属性 | 类型 | 描述 |
|----|----|----|
|    |    |    |
|    |    |    |
出参：

| 属性 | 类型 | 描述 |
|----|----|----|
|    |    |    |
## RSAUtil.signByPublicKeyWithRSA

入参：

| 属性 | 类型 | 描述 |
|----|----|----|
|    |    |    |
|    |    |    |
出参：

| 属性 | 类型 | 描述 |
|----|----|----|
|    |    |    |
## RSAUtil.signByPrivateKeyWithRSA

入参：

| 属性 | 类型 | 描述 |
|----|----|----|
|    |    |    |
|    |    |    |
出参：

| 属性 | 类型 | 描述 |
|----|----|----|
|    |    |    |
## UrlEncodeUtil.encryptWithUrlEncode

入参：

| 属性 | 类型 | 描述 |
|----|----|----|
|    |    |    |
|    |    |    |
出参：

| 属性 | 类型 | 描述 |
|----|----|----|
|    |    |    |
## UrlEncodeUtil.encryptWithUrlEncodeByEnc

入参：

| 属性 | 类型 | 描述 |
|----|----|----|
|    |    |    |
|    |    |    |
出参：

| 属性 | 类型 | 描述 |
|----|----|----|
|    |    |    |
## UrlEncodeUtil.decryptWithUrlDecode

入参：

| 属性 | 类型 | 描述 |
|----|----|----|
|    |    |    |
|    |    |    |
出参：

| 属性 | 类型 | 描述 |
|----|----|----|
|    |    |    |
## UrlEncodeUtil.decryptWithUrlDecodeByEnc

入参：

| 属性 | 类型 | 描述 |
|----|----|----|
|    |    |    |
|    |    |    |
出参：

| 属性 | 类型 | 描述 |
|----|----|----|
|    |    |    |