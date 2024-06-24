# 这是一个自定义接口过滤依赖库，当前默认实现防重放

## SecurityFilter自定义实现
spi实现自定义CheckService接口，在实现类中，可以根据需要实现自己的过滤逻辑。
当前默认NonceRedisCheckServiceImpl实现类，使用nonce、timestamp和sign过实现防重放。
须配合前端依赖库一起使用。
前端在header中填充lib-nonce/lib-timestamp/lib-sign。其中lib_sign使用与后端secretKey匹配的私钥进行签名。
支持spi扩展，可实现其他功能的校验。

## storageStrategy配置
根据storageStrategy选择不同的防重放存储策略。当前默认使用redis实现。