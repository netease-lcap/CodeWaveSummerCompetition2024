package com.netease.cloud;

import com.netease.lowcode.core.annotation.NaslConnector;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Redis 连接器，可连接第三方Redis服务并执行操作
 */
@NaslConnector(connectorKind = "redis")
public class RedisConnector {
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 将 Redis 中指定 key 的值增加指定的浮点数 delta
     *
     * @param key   Redis 中的键
     * @param delta 增加的浮点数值
     * @return 增加后的结果值
     */
    /*public Double incrementDouble(String key, Double delta) {
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        return ops.increment(key, delta);
    }*/
    public static String convertToJsonString(String input) {
        return input.replace("\\", "");
    }

    /**
     * 初始化 Redis 连接
     *
     * @param host     redis地址
     * @param port     redis端口
     * @param password redis密码
     * @param database redis数据库
     * @return
     */
    @NaslConnector.Creator
    public RedisConnector initRedisTemplate(String host, Integer port, String password, Integer database) {
        RedisConnector redisTool = new RedisConnector();
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setPort(port);
        redisStandaloneConfiguration.setDatabase(database);
        redisStandaloneConfiguration.setHostName(host);
        redisStandaloneConfiguration.setPassword(password);

        LettuceConnectionFactory redisConnectionFactory = new LettuceConnectionFactory(redisStandaloneConfiguration);
        redisConnectionFactory.afterPropertiesSet();
        RedisTemplate<String, String> _redisTemplate = new RedisTemplate<>();
        _redisTemplate.setConnectionFactory(redisConnectionFactory);
        _redisTemplate.setKeySerializer(new StringRedisSerializer());
        _redisTemplate.setValueSerializer(new StringRedisSerializer());
        _redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        _redisTemplate.setHashValueSerializer(new StringRedisSerializer());
        _redisTemplate.afterPropertiesSet();
        redisTemplate = _redisTemplate;
        redisTool.redisTemplate = _redisTemplate;
        return redisTool;
    }

    /**
     * 获取key对应的字符串值
     *
     * @param key
     * @return
     */
    @NaslConnector.Logic
    public String getValue(final String key) {
        Object value = redisTemplate.opsForValue().get(key);
        if (value == null) {
            return null;
        }
        return String.valueOf(value);
    }

    /**
     * 指定 key 的值为指定字符串
     *
     * @param key
     * @param value
     * @return
     */
    @NaslConnector.Logic
    public String setValue(final String key, final String value) {
        redisTemplate.opsForValue().set(key, value);
        return value;
    }

    /**
     * 删除指定的key
     *
     * @param key
     */
    @NaslConnector.Logic
    public Boolean deleteKey(final String key) {
        redisTemplate.delete(key);
        return true;
    }

    /**
     * 测试链接是否可用，如果可用，则返回 true，否则返回 false
     *
     * @param host     redis地址
     * @param port     redis端口
     * @param password redis密码
     * @param database redis数据库
     * @return
     */
    @NaslConnector.Tester
    public Boolean testConnection(String host, Integer port, String password, Integer database) {
        RedisURI redisURI = RedisURI.Builder.redis(host, port).withPassword(password).withDatabase(database).withTimeout(Duration.of(3, ChronoUnit.SECONDS)).build();
        RedisClient client = RedisClient.create(redisURI);
        try (StatefulRedisConnection<String, String> connect = client.connect()) {
            String pong = connect.sync().ping();
            return "PONG".equals(pong);
        } catch (Exception e) {
            return false;
        } finally {
            // 连通性测试后关闭连接
            client.shutdown();
        }
    }

    /**
     * 指定 key 的值与过期时间，超期key自动删除（单位为秒）
     *
     * @param key     要设置的键
     * @param value   要设置的值
     * @param timeout 过期时间
     */
    @NaslConnector.Logic
    public String setValueTimeOut(String key, String value, Long timeout) {
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        ops.set(key, value, timeout, TimeUnit.SECONDS);
        return ops.get(key);
    }

    /**
     * 指定 key 的对应的队列
     *
     * @param key
     * @return
     */
    @NaslConnector.Logic
    public List<String> getListValue(String key) {
        ListOperations<String, String> ops = redisTemplate.opsForList();
        return ops.range(key, 0, -1);
    }

    /**
     * 指定 key 的值的队列新增数据
     *
     * @param key
     * @return
     */
    @NaslConnector.Logic
    public List<String> setListValue(String key, List<String> value) {
        ListOperations<String, String> ops = redisTemplate.opsForList();
        ops.rightPushAll(key, value);
        return ops.range(key, 0, -1);
    }

    /**
     * 若key不存在，则指定key的值
     *
     * @param key   要设置的键
     * @param value 要设置的值
     * @return 如果设置成功，返回 true，否则返回 false
     */
    @NaslConnector.Logic
    public Boolean setIfAbsent(String key, String value) {
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        return ops.setIfAbsent(key, value);
    }

    /**
     * 若key不存在，则指定key的值，并设置过期时间（单位为秒）
     *
     * @param key     要设置的键
     * @param value   要设置的值
     * @param timeout 过期时间
     * @return 如果设置成功，返回 true，否则返回 false
     */
    @NaslConnector.Logic
    public Boolean setIfAbsentWithExpire(String key, String value, Long timeout) {
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        return ops.setIfAbsent(key, value, timeout, TimeUnit.SECONDS);
    }

    /**
     * 若key存在，则指定key的值，并设置过期时间（单位为秒）
     *
     * @param key     要设置的键
     * @param value   要设置的值
     * @param timeout 过期时间
     * @return 如果设置成功，返回 true，否则返回 false
     */
    @NaslConnector.Logic
    public Boolean setIfPresentWithExpire(String key, String value, Long timeout) {
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        return ops.setIfPresent(key, value, timeout, TimeUnit.SECONDS);
    }

    /**
     * 指定 key 的值增加指定的整数 delta
     *
     * @param key   要设置的键
     * @param delta 增加的整数值
     * @return 增加后的结果值
     */
    @NaslConnector.Logic
    public Long incrementLong(String key, Long delta) {
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        return ops.increment(key, delta);
    }

    /**
     * 将多个键值对设置到哈希表中
     *
     * @param hashKey     哈希表的键
     * @param keyValueMap 包含要设置到 Redis 哈希表中的键值对的 Map
     * @return 设置后哈希表的 Map
     */
    @NaslConnector.Logic
    public Map<String, String> setHashValues(String hashKey, Map<String, String> keyValueMap) {
        HashOperations<String, String, String> ops = redisTemplate.opsForHash();
        for (Map.Entry<String, String> entry : keyValueMap.entrySet()) {
            ops.put(hashKey, entry.getKey(), entry.getValue());
        }
        return ops.entries(hashKey);
    }

    /**
     * 由哈希表中获取指定 key 的所有字段和值
     *
     * @param hashKey 哈希表的 key
     * @return 包含所有域和值的 Map 对象，键为域，值为对应的值
     */
    @NaslConnector.Logic
    public Map<String, String> getHashValuesWithNoKey(String hashKey) {
        redisTemplate.setHashValueSerializer(new StringRedisSerializer());
        HashOperations<String, String, String> ops = redisTemplate.opsForHash();
        return ops.entries(hashKey);
    }

    /**
     * 由哈希表中获取多个键对应的值
     *
     * @param hashKey   哈希表的键
     * @param fieldKeys 包含要获取值的键的列表
     * @return 一个包含指定键值对的 Map
     */
    @NaslConnector.Logic
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
     * 删除哈希表中指定的一个或多个字段
     *
     * @param hashKey 哈希表的 key
     * @param keys    要删除的一个或多个域
     * @return 被删除的域的数量
     */
    @NaslConnector.Logic
    public Long deleteHashfieldKeys(String hashKey, List<String> keys) {
        HashOperations<String, String, String> ops = redisTemplate.opsForHash();
        String[] fieldKeysArray = keys.toArray(new String[0]);
        return ops.delete(hashKey, fieldKeysArray);
    }

    /**
     * 判断哈希表中是否存在指定的字段
     *
     * @param hashKey  哈希表的 key
     * @param fieldKey 要检查的域
     * @return 如果存在，返回 true；否则，返回 false
     */
    @NaslConnector.Logic
    public Boolean hashfieldKeyExists(String hashKey, String fieldKey) {
        HashOperations<String, String, String> ops = redisTemplate.opsForHash();
        return ops.hasKey(hashKey, fieldKey);
    }

    /**
     * 将哈希表中指定的字段设置为指定的值
     *
     * @param hashKey  哈希表的 key
     * @param fieldKey 要设置的域
     * @param value    要设置的值
     * @return 如果设置成功，返回 true；如果域已存在，不进行设置，返回 false
     */
    @NaslConnector.Logic
    public Boolean hashSetIfNotExists(String hashKey, String fieldKey, String value) {
        HashOperations<String, String, String> ops = redisTemplate.opsForHash();
        return ops.putIfAbsent(hashKey, fieldKey, value);
    }

    /**
     * 获取哈希表中字段的数量
     *
     * @param hashKey 哈希表的 key
     * @return 哈希表中字段的数量
     */
    @NaslConnector.Logic
    public Long hashSize(String hashKey) {
        HashOperations<String, String, String> ops = redisTemplate.opsForHash();
        return ops.size(hashKey);
    }

    /**
     * 获取 Redis string 的keys
     */
    @NaslConnector.Logic
    public List<String> keys(String key) {
        Set<String> keys = redisTemplate.keys(key);
        if (keys == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(keys);
    }


    /**
     * 获取哈希表中的所有字段列表
     *
     * @param hashKey 哈希表的 key
     * @return 哈希表中的所有域
     */
    @NaslConnector.Logic
    public List<String> hashKeys(String hashKey) {
        HashOperations<String, String, String> ops = redisTemplate.opsForHash();
        return new ArrayList<>(ops.keys(hashKey));
    }

    /**
     * 将哈希表中指定字段的值增加指定的增量
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
