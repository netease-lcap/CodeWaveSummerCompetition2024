# 依赖说明

## redis-template-tool依赖库1.x版本

使用场景：
低版本redis服务端，包括**集群、哨兵、单机模式**。

## redis-template-tool-acl依赖库2.x版本

依赖必须库配合**翻译器插件**使用。
因为其中使用的ACL相关代码，在默认配置下，找不到对应的类，会导致应用启动报错。

使用场景：

1. 低版本redis服务端，包括**集群、哨兵、单机模式**。
2. 适配7.x版本redis服务端，包括**集群、单机**模式。不支持高版本哨兵，因为7.x版本哨兵模式对jdk和spring boot版本有要求。
3. 支持ACL。6.x以上版本redis依赖库，**须配置username时**，可使用2.x版本依赖库配合翻译器插件使用


# 配置参数示例
## 单机
      redisMode: '2'
      redisHost: '127.0.0.1'
      redisPort: '6379'
      redisPassword: 'admin456'
      redisClusterNodes: ''
      redisUsername: ''
      redisUrl: ''
      redisSentinelMaster: ''
      redisSentinelNodes: ''
## 集群
      redisMode: '4'
      redisHost: ''
      redisPort: ''
      redisPassword: 'admin123'
      redisClusterNodes: '10.242.41.153:7001,10.242.41.153:7002,10.242.41.153:7003'
      redisUrl: ''
      redisUsername: 'myadmin'
      redisSentinelMaster: ''
      redisSentinelNodes: ''
## 哨兵
      redisMode: '3'
      redisHost: ''
      redisPort: ''
      redisPassword: 'admin123'
      redisClusterNodes: ''
      redisUrl: ''
      redisUsername: 'myadmin'
      redisSentinelMaster: 'redis-master'
      redisSentinelNodes: '117.187.150.155:26379,117.187.150.155:26380,117.187.150.155:26381'


