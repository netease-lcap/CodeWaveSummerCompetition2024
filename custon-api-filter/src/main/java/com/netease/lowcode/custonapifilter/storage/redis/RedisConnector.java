package com.netease.lowcode.custonapifilter.storage.redis;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * redis工具
 */
@Component
public class RedisConnector {
    @Resource
    private RedisTemplate redisTemplate;


    public static String convertToJsonString(String input) {
        return input.replace("\\", "");
    }

    /**
     * 从redis获取
     *
     * @param key
     * @return
     */
    public String getValue(final String key) {
        Object value = redisTemplate.opsForValue().get(key);
        if (value == null) {
            return null;
        }
        return String.valueOf(value);
    }

    /**
     * 设置key
     *
     * @param key
     * @param value
     * @return
     */
    public String setValue(final String key, final String value) {
        redisTemplate.opsForValue().set(key, value);
        return value;
    }

    /**
     * 删除key
     *
     * @param key
     */
    public Boolean deleteKey(final String key) {
        redisTemplate.delete(key);
        return true;
    }

    /**
     * 设置 Redis 中指定 key 的值为指定字符串，指定过期时间
     *
     * @param key     Redis 中的键
     * @param value   Redis 中的值
     * @param timeout 过期时间
     */
    public String setValueTimeOut(String key, String value, Long timeout) {
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        ops.set(key, value, timeout, TimeUnit.SECONDS);
        return ops.get(key);
    }

    public List<String> getListValue(String key) {
        ListOperations<String, String> ops = redisTemplate.opsForList();
        return ops.range(key, 0, -1);
    }

    public List<String> setListValue(String key, List<String> value) {
        ListOperations<String, String> ops = redisTemplate.opsForList();
        ops.rightPushAll(key, value);
        return ops.range(key, 0, -1);
    }

    /**
     * 如果 Redis 中不存在指定 key，则设置为指定字符串
     *
     * @param key   Redis 中的键
     * @param value Redis 中的值
     * @return 如果设置成功，返回 true，否则返回 false
     */
    public Boolean setIfAbsent(String key, String value) {
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        return ops.setIfAbsent(key, value);
    }

    /**
     * 如果 Redis 中不存在指定 key，则设置为指定字符串，并设置过期时间
     *
     * @param key     Redis 中的键
     * @param value   Redis 中的值
     * @param timeout 过期时间
     * @return 如果设置成功，返回 true，否则返回 false
     */
    public Boolean setIfAbsentWithExpire(String key, String value, Long timeout) {
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        return ops.setIfAbsent(key, value, timeout, TimeUnit.SECONDS);
    }

    /**
     * 如果 Redis 中存在指定 key，则设置为指定字符串，并设置过期时间
     *
     * @param key     Redis 中的键
     * @param value   Redis 中的值
     * @param timeout 过期时间
     * @return 如果设置成功，返回 true，否则返回 false
     */
    public Boolean setIfPresentWithExpire(String key, String value, Long timeout) {
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        return ops.setIfPresent(key, value, timeout, TimeUnit.SECONDS);
    }

    /**
     * 将 Redis 中指定 key 的值增加指定的整数 delta
     *
     * @param key   Redis 中的键
     * @param delta 增加的整数值
     * @return 增加后的结果值
     */
    public Long incrementLong(String key, Long delta) {
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        return ops.increment(key, delta);
    }

    /**
     * 将多个键值对设置到 Redis 哈希表中，并返回更新后的哈希表。
     *
     * @param hashKey     哈希表的键
     * @param keyValueMap 包含要设置到 Redis 哈希表中的键值对的 Map
     * @return 表示更新后 Redis 哈希表的 Map
     */
    public Map<String, String> setHashValues(String hashKey, Map<String, String> keyValueMap) {
        HashOperations<String, String, String> ops = redisTemplate.opsForHash();
        for (Map.Entry<String, String> entry : keyValueMap.entrySet()) {
            ops.put(hashKey, entry.getKey(), entry.getValue());
        }
        return ops.entries(hashKey);
    }

    /**
     * 获取 Redis 哈希表中指定 key 的所有域和值
     *
     * @param hashKey 哈希表的 key
     * @return 包含所有域和值的 Map 对象，键为域，值为对应的值
     */
    public Map<String, String> getHashValuesWithNoKey(String hashKey) {
        redisTemplate.setHashValueSerializer(new StringRedisSerializer());
        HashOperations<String, String, String> ops = redisTemplate.opsForHash();
        return ops.entries(hashKey);
    }

    /**
     * 从 Redis 哈希表中获取多个键对应的值，并返回一个包含这些键值对的 Map。
     *
     * @param hashKey   哈希表的键
     * @param fieldKeys 包含要获取值的键的列表
     * @return 一个包含指定键值对的 Map
     */
    public Map<String, String> getHashValues(String hashKey, List<String> fieldKeys) {
        HashOperations<String, String, String> ops = redisTemplate.opsForHash();
        List<String> values = ops.multiGet(hashKey, fieldKeys);

        Map<String, String> resultMap = new HashMap<>();
        for (int i = 0; i < fieldKeys.size(); i++) {
            String key = fieldKeys.get(i);
            String value = values.get(i);
            if (value != null) {
                resultMap.put(key, value);
            }
        }
        return resultMap;
    }


    /**
     * 删除 Redis 哈希表中指定的一个或多个域
     *
     * @param hashKey 哈希表的 key
     * @param keys    要删除的一个或多个域
     * @return 被删除的域的数量
     */
    public Long deleteHashfieldKeys(String hashKey, List<String> keys) {
        HashOperations<String, String, String> ops = redisTemplate.opsForHash();
        String[] fieldKeysArray = keys.toArray(new String[0]);
        return ops.delete(hashKey, fieldKeysArray);
    }

    /**
     * 检查 Redis 哈希表中是否存在指定的域
     *
     * @param hashKey  哈希表的 key
     * @param fieldKey 要检查的域
     * @return 如果存在，返回 true；否则，返回 false
     */
    public Boolean hashfieldKeyExists(String hashKey, String fieldKey) {
        HashOperations<String, String, String> ops = redisTemplate.opsForHash();
        return ops.hasKey(hashKey, fieldKey);
    }

    /**
     * 将指定的域设置为指定的值，仅当域不存在时才进行设置
     *
     * @param hashKey  哈希表的 key
     * @param fieldKey 要设置的域
     * @param value    要设置的值
     * @return 如果设置成功，返回 true；如果域已存在，不进行设置，返回 false
     */
    public Boolean hashSetIfNotExists(String hashKey, String fieldKey, String value) {
        HashOperations<String, String, String> ops = redisTemplate.opsForHash();
        return ops.putIfAbsent(hashKey, fieldKey, value);
    }

    /**
     * 获取 Redis 哈希表中域的数量
     *
     * @param hashKey 哈希表的 key
     * @return 哈希表中域的数量
     */
    public Long hashSize(String hashKey) {
        HashOperations<String, String, String> ops = redisTemplate.opsForHash();
        return ops.size(hashKey);
    }


    /**
     * 获取 Redis 哈希表中的所有域
     *
     * @param hashKey 哈希表的 key
     * @return 哈希表中的所有域
     */
    public List<String> hashKeys(String hashKey) {
        HashOperations<String, String, String> ops = redisTemplate.opsForHash();
        return new ArrayList<>(ops.keys(hashKey));
    }

    /**
     * 将 Redis 哈希表中指定域的值增加指定的增量
     *
     * @param hashKey   哈希表的 key
     * @param fieldKey  要增加值的域
     * @param increment 增量
     * @return 增加指定增量后域的值
     */
    public Long hashIncrementBy(String hashKey, String fieldKey, Long increment) {
        HashOperations<String, String, String> ops = redisTemplate.opsForHash();
        return ops.increment(hashKey, fieldKey, increment);
    }

    /**
     * 将 Redis 哈希表中指定域的值增加指定的浮点数增量
     *
     * @param hashKey   哈希表的 key
     * @param fieldKey  要增加值的域
     * @param increment 增量
     * @return 增加指定增量后域的值
     */
    /*public Double hashIncrementByDouble(String hashKey, String fieldKey, Double increment) {
        HashOperations<String, String, String> ops = redisTemplate.opsForHash();
        return ops.increment(hashKey, fieldKey, increment);
    }*/

    /**
     * 关闭redis连接
     */
    public void close() throws Exception {
        if (redisTemplate != null) {
            // 获取连接工厂
            RedisConnectionFactory connectionFactory = redisTemplate.getConnectionFactory();
            if (connectionFactory != null) {
                // 关闭连接
                connectionFactory.getConnection().close();
                if (connectionFactory instanceof DisposableBean) {
                    try {
                        // 关闭连接工厂， 连接工厂也需要关闭，要不然连接池不会回收
                        ((DisposableBean) connectionFactory).destroy();
                    } catch (Exception closeException) {
                        throw closeException;
                    }
                }
            }
        }
    }
}
