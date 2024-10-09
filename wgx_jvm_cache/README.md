# JvmCache - JVM内存缓存依赖库

## 概述
`JvmCache` 是一个基于Caffeine实现的JVM内存缓存工具，用于高效地管理和操作内存中的缓存数据。
## 应用配置参数

JvmCache 提供了两个可配置的参数，用于调整缓存的性能和容量。这些参数可以通过环境变量或配置文件进行设置。

1. `initialCapacity`
  - 描述：初始的缓存空间大小
  - 单位：MB（兆字节）
  - 默认值：
    - 开发环境：10 MB
    - 生产环境：10 MB
  - 配置示例：`initialCapacity=20`

2. `maximumSize`
  - 描述：缓存的最大条目数
  - 单位：条目数量
  - 默认值：
    - 开发环境：10,000 条
    - 生产环境：10,000 条
  - 配置示例：`maximumSize=20000`

## 主要方法

1. `getAllKeys()`：
- 作用：获取缓存中所有的键。
- 参数：无。
- 返回值：List<String>，包含所有缓存键的列表。如果缓存为空或未初始化，返回空列表。
- 异常：无。方法内部会捕获并处理所有异常，确保始终返回一个列表。

2. `getAllCache()`：
- 作用：获取缓存中的所有键值对。
- 参数：无。
- 返回值：Map<String, String>，包含所有缓存的键值对。如果缓存为空或未初始化，返回空Map。
- 异常：无。方法内部会捕获并记录异常，返回空Map。

3. `exist(String key)`：
- 作用：检查指定的键是否存在于缓存中。
- 参数：key (String) - 要检查的缓存键。
- 返回值：Boolean，如果键存在返回true，否则返回false。
- 异常：无。方法内部处理所有异常情况。

4. `existHash(String key, String hashField)`：
- 作用：检查指定的哈希键是否存在于缓存中。
- 参数：
  - key (String) - 主键
  - hashField (String) - 哈希字段
- 返回值：Boolean，如果哈希键存在返回true，否则返回false。
- 异常：无。方法内部处理所有异常情况。

5. `setCache(String key, String value)`：
- 作用：设置缓存键值对。
- 参数：
  - key (String) - 缓存键
  - value (String) - 缓存值
- 返回值：Boolean，设置成功返回true，失败返回false。
- 异常：抛出CacheException，当设置缓存时发生异常。

6. `setCacheWithExpire(String key, String value, Long expire)`：
- 作用：设置带过期时间的缓存键值对。
- 参数：
  - key (String) - 缓存键
  - value (String) - 缓存值
  - expire (Long) - 过期时间（毫秒）
- 返回值：Boolean，设置成功返回true，失败返回false。
- 异常：抛出CacheException，当设置缓存时发生异常。

7. `setHashCache(String key, String hashField, String value)`：
- 作用：设置哈希缓存。
- 参数：
  - key (String) - 主键
  - hashField (String) - 哈希字段
  - value (String) - 缓存值
- 返回值：Boolean，设置成功返回true，失败返回false。
- 异常：抛出CacheException，当设置哈希缓存时发生异常。

8. `getCache(String key)`：
- 作用：获取指定键的缓存值。
- 参数：key (String) - 缓存键
- 返回值：String，对应的缓存值，如果不存在或已过期返回null。
- 异常：抛出CacheException，当获取缓存时发生异常。

9. `getHashCache(String key, String hashField)`：
- 作用：获取指定哈希键的缓存值。
- 参数：
  - key (String) - 主键
  - hashField (String) - 哈希字段
- 返回值：String，对应的哈希缓存值，如果不存在或已过期返回null。
- 异常：抛出CacheException，当获取哈希缓存时发生异常。

10. `getMultiCache(List<String> keys)`：
- 作用：批量获取多个键的缓存值。
- 参数：keys (List<String>) - 要获取的键的列表
- 返回值：Map<String, String>，包含存在的键值对。
- 异常：抛出CacheException，当批量获取缓存时发生异常。

11. `deleteCache(String key)`：
- 作用：删除指定的缓存键值对。
- 参数：key (String) - 要删除的缓存键
- 返回值：Boolean，删除成功返回true，失败返回false。
- 异常：无。方法内部捕获并处理所有异常。

12. `deleteHashCache(String key, String hashField)`：
- 作用：删除指定的哈希缓存。
- 参数：
  - key (String) - 主键
  - hashField (String) - 哈希字段
- 返回值：Boolean，删除成功返回true，失败返回false。
- 异常：无。方法内部捕获并处理所有异常。

13. `clearAllCache()`：
- 作用：清空所有缓存。
- 参数：无。
- 返回值：Boolean，清空成功返回true，失败返回false。
- 异常：无。方法内部捕获并处理所有异常。

14. `getEstimatedSize()`：
- 作用：获取缓存的估计大小。
- 参数：无。
- 返回值：Long，缓存中的估计条目数，如果缓存未初始化则返回null。
- 异常：无。方法内部处理所有异常情况。

15. `getOrComputeAndSet(String key, Long expireAfterWriteMillis, Function<String, String> computeFunction)`：
- 作用：获取缓存，如果缓存不存在则执行指定的计算函数，并将计算结果存入缓存。
- 参数：
  - key (String) - 缓存键
  - expireAfterWriteMillis (Long) - 写入后的过期时间（毫秒）
  - computeFunction (Function<String, String>) - 如果缓存不存在时，用于计算值的函数
- 返回值：String，缓存中的值或计算后的新值。如果计算结果为null，则返回null且不会更新缓存。
- 异常：抛出CacheException，当获取、计算或设置缓存时发生异常。

注意：
1. 所有方法都会进行缓存初始化检查，如果缓存未初始化，将返回相应的默认值或null。
2. 大多数方法会捕获并记录异常，同时返回适当的错误值或抛出CacheException。
3. 在使用这些方法时，建议进行适当的异常处理，特别是那些可能抛出CacheException的方法。