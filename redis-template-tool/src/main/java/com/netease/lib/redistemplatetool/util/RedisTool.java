package com.netease.lib.redistemplatetool.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netease.lowcode.core.annotation.NaslLogic;
import com.netease.lowcode.core.util.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.text.Collator;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


@Component
public class RedisTool {
    private static final Logger logger = LoggerFactory.getLogger("LCAP_EXTENSION_LOGGER");

    @Autowired
    public RedisTemplate<String, String> redisTemplate;
    private ObjectMapper mapper = new ObjectMapper();

    /**
     * 是否存在key，返回true/false
     *
     * @param key
     * @return
     */
    @NaslLogic
    public Boolean hasKey(String key) {
        if (StringUtils.isEmpty(key)) {
            return false;
        }
        return redisTemplate.hasKey(key);
    }

    /**
     * 批量删除key
     *
     * @param keys
     * @return
     */
    @NaslLogic
    public Long deleteKeys(List<String> keys) {
        if (CollectionUtils.isEmpty(keys)) {
            return 0L;
        }
        return redisTemplate.delete(keys);
    }

    /**
     * 设置 Redis 中指定 key 的过期时间
     *
     * @param key
     * @param timeout
     * @return
     */
    @NaslLogic
    public Boolean setExpire(String key, Long timeout) {
        return redisTemplate.expire(key, timeout, TimeUnit.SECONDS);
    }

    /**
     * 从集合中删除指定元素
     *
     * @param key
     * @param value
     * @return
     */
    @NaslLogic
    public Long removeFromSet(String key, String value) {
        SetOperations<String, String> setOperations = redisTemplate.opsForSet();
        return setOperations.remove(key, value);
    }

    /**
     * 从集合中删除多个指定元素
     *
     * @param key
     * @param values
     * @return
     */
    @NaslLogic
    public Long removesFromSet(String key, List<String> values) {
        SetOperations<String, String> setOperations = redisTemplate.opsForSet();
        return setOperations.remove(key, values.toArray());
    }

    /**
     * 向集合中添加元素
     *
     * @param key
     * @param value
     * @return
     */
    @NaslLogic
    public Long addToSet(String key, String value) {
        SetOperations<String, String> setOperations = redisTemplate.opsForSet();
        return setOperations.add(key, value);
    }

    /**
     * 根据key获取集合中的所有元素
     *
     * @param key
     * @return
     */
    @NaslLogic
    public List<String> getSetMembers(String key) {
        SetOperations<String, String> setOperations = redisTemplate.opsForSet();
        //set转list
        return new ArrayList<>(Objects.requireNonNull(setOperations.members(key)));
    }

    /**
     * 设置 Redis 中指定 key 的值为指定字符串
     *
     * @param key
     * @param value
     * @param timeout
     * @return
     */
    @NaslLogic
    public String setValueTimeOut(String key, String value, Long timeout) {
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        ops.set(key, value, timeout, TimeUnit.SECONDS);
        return ops.get(key);
    }

    /**
     * 设置 Redis 中指定 key 的值为指定字符串
     *
     * @param key   Redis 中的键
     * @param value Redis 中的值
     */
    @NaslLogic
    public String setValue(String key, String value) {
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        ops.set(key, value);
        return ops.get(key);
    }

    @NaslLogic
    public List<String> getListValue(String key) {
        ListOperations<String, String> ops = redisTemplate.opsForList();
        return ops.range(key, 0, -1);
    }

    @NaslLogic
    public List<String> setListValue(String key, List<String> value) {
        ListOperations<String, String> ops = redisTemplate.opsForList();
        ops.rightPushAll(key, value);
        return ops.range(key, 0, -1);
    }

    /**
     * 获取 Redis 中指定 key 的值
     *
     * @param key Redis 中的键
     * @return Redis 中的值
     */
    @NaslLogic
    public String getValue(String key) throws NullPointerException {
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        return ops.get(key);
    }

    /**
     * 如果 Redis 中不存在指定 key，则设置为指定字符串
     *
     * @param key   Redis 中的键
     * @param value Redis 中的值
     * @return 如果设置成功，返回 true，否则返回 false
     */
    @NaslLogic
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
    @NaslLogic
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
    @NaslLogic
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
    @NaslLogic
    public Long incrementLong(String key, Long delta) {
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        return ops.increment(key, delta);
    }

    /**
     * 将 Redis 中指定 key 的值增加指定的浮点数 delta
     *
     * @param key   Redis 中的键
     * @param delta 增加的浮点数值
     * @return 增加后的结果值
     */
    @NaslLogic
    public Double incrementDoulbe(String key, Double delta) {
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        return ops.increment(key, delta);
    }

    /**
     * 删除 Redis 中指定的 key
     *
     * @param key Redis 中的键
     */
    @NaslLogic
    public Boolean deleteKey(String key) {
        return redisTemplate.delete(key);
    }

    public String convertToJsonString(String input) {
        return input.replace("\\", "");
    }

    /**
     * 将多个键值对设置到 Redis 哈希表中，并返回更新后的哈希表。
     *
     * @param hashKey     哈希表的键
     * @param keyValueMap 包含要设置到 Redis 哈希表中的键值对的 Map
     * @return 表示更新后 Redis 哈希表的 Map
     */
    @NaslLogic
    public Map<String, String> setHashValues(String hashKey, Map<String, String> keyValueMap) {
        HashOperations<String, String, String> ops = redisTemplate.opsForHash();
        for (Map.Entry<String, String> entry : keyValueMap.entrySet()) {
            ops.put(hashKey, entry.getKey(), entry.getValue());
        }
        return ops.entries(hashKey);
    }

    /**
     * 将多个键值对设置到 Redis 哈希表中，并返回更新后的哈希表。
     *
     * @param hashKey     哈希表的键
     * @param keyValueMap 包含要设置到 Redis 哈希表中的键值对的 Map
     * @return 是否更新成功
     */
    @NaslLogic
    public Boolean setHashValuesReturnBoolean(String hashKey, Map<String, String> keyValueMap) {
        HashOperations<String, String, String> ops = redisTemplate.opsForHash();
        for (Map.Entry<String, String> entry : keyValueMap.entrySet()) {
            ops.put(hashKey, entry.getKey(), entry.getValue());
        }
        return true;
    }

    /**
     * 获取 Redis 哈希表中指定 key 的所有域和值
     *
     * @param hashKey 哈希表的 key
     * @return 包含所有域和值的 Map 对象，键为域，值为对应的值
     */
    @NaslLogic
    public Map<String, String> getHashValuesWithNoKey(String hashKey) {
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
    @NaslLogic
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
     * 从 Redis 哈希表中获取键对应的值。
     *
     * @param hashKey  哈希表的键
     * @param fieldKey 获取值的键
     * @return 单条数据value
     */
    @NaslLogic
    public String getHashValue(String hashKey, String fieldKey) {
        HashOperations<String, String, String> ops = redisTemplate.opsForHash();
        return ops.get(hashKey, fieldKey);
    }

    /**
     * 根据传入的 key 和 field 列表查询 Redis 中对应的值，并将查询结果存储在一个 Map<String, Map<String, String>> 中
     *
     * @param keyAndFieldsMap Map<String, String>，其中 key 是 Redis 中的 key，value 是要查询的 field
     * @return Map<String, Map < String, String>>，其中外层的 key 是 Redis 中的 key，内层的 key-value 对是 field 和它对应的值
     */
    @NaslLogic
    public Map<String, Map<String, String>> getHashValuesByKeysAndField(Map<String, String> keyAndFieldsMap) {
        HashOperations<String, String, String> ops = redisTemplate.opsForHash();
        Map<String, Map<String, String>> resultMap = new HashMap<>();

        for (Map.Entry<String, String> entry : keyAndFieldsMap.entrySet()) {
            String hashKey = entry.getKey();
            String fieldKey = entry.getValue();
            String value = ops.get(hashKey, fieldKey);
            if (value != null) {
                Map<String, String> fieldMap = new HashMap<>();
                fieldMap.put(fieldKey, value);
                resultMap.put(hashKey, fieldMap);
            }
        }

        return resultMap;
    }

    /**
     * 根据传入的 key 和 field 列表查询 Redis 中对应的值，并将查询结果存储在一个 Map<String, Map<String, String>> 中
     *
     * @param keyAndFieldsMap Map<String, List<String>>，其中 key 是 Redis 中的 key，value 是要查询的 hashMap 中对应的 field 的列表
     * @return Map<String, Map < String, String>>，其中外层的 key 是 Redis 中的 key，内层的 key-value 对是 field 和它对应的值
     */
    @NaslLogic
    public Map<String, Map<String, String>> getHashValuesByKeysAndFiledList(Map<String, List<String>> keyAndFieldsMap) {
        HashOperations<String, String, String> ops = redisTemplate.opsForHash();
        Map<String, Map<String, String>> resultMap = new HashMap<>();

        for (Map.Entry<String, List<String>> entry : keyAndFieldsMap.entrySet()) {
            String hashKey = entry.getKey();
            List<String> fieldKeys = entry.getValue();

            List<String> values = ops.multiGet(hashKey, fieldKeys);

            Map<String, String> fieldMap = new HashMap<>();
            for (int i = 0; i < fieldKeys.size(); i++) {
                String key = fieldKeys.get(i);
                String value = values.get(i);
                if (value != null) {
                    fieldMap.put(key, value);
                }
            }

            resultMap.put(hashKey, fieldMap);
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
    @NaslLogic
    public Long deleteHashfieldKeys(String hashKey, List<String> keys) {
        HashOperations<String, String, String> ops = redisTemplate.opsForHash();
        String[] fieldKeysArray = keys.toArray(new String[0]);
        return ops.delete(hashKey, fieldKeysArray);
    }

    /**
     * 删除 Redis 哈希表中指定的域
     *
     * @param hashKey  哈希表的 key
     * @param fieldKey 要删除的域
     * @return 被删除的域的数量
     */
    @NaslLogic
    public Long deleteHashfieldKey(String hashKey, String fieldKey) {
        HashOperations<String, String, String> ops = redisTemplate.opsForHash();
        return ops.delete(hashKey, fieldKey);
    }

    /**
     * 获取hashKey中limit条数据
     *
     * @param hashKey      数据结构类型
     * @param limit        获取的条数（默认为最大值）
     * @param keywordValue 搜索关键字，可空
     * @param keywordName  搜索字段，可空，根据值的中文排序
     * @return
     */
    @NaslLogic
    public List<String> getHashTopN(String hashKey, Integer limit, String keywordValue, String keywordName) {
        if (limit == null | limit == 0) {
            limit = Integer.MAX_VALUE;
        }
        List<String> allList = redisTemplate.<String, Object>opsForHash()
                .values(hashKey)
                .stream()
                .limit(Integer.MAX_VALUE)
                .map(Object::toString)
                .collect(Collectors.toList());
        if (StringUtils.isEmpty(keywordValue) || StringUtils.isEmpty(keywordName)) {
            return allList;
        } else {
            return allList.stream().filter(json -> {
                        try {
                            Map<String, Object> jsonMap = mapper.readValue(json, Map.class);
                            JSONObject jsonObject = new JSONObject(jsonMap);
                            String nameValue = jsonObject.getString(keywordName);
                            return !StringUtils.isEmpty(nameValue) && nameValue.contains(keywordValue);
                        } catch (Exception e) {
                            logger.error("json转换错误", e);
                            return false;
                        }
                    }).sorted((json1, json2) -> {
                        try {
                            // 创建中文拼音排序器
                            Collator collator = Collator.getInstance(Locale.CHINA);
                            // 获取两个json对象的中文名
                            Map<String, Object> jsonMap1 = mapper.readValue(json1, Map.class);
                            JSONObject jsonObject1 = new JSONObject(jsonMap1);
                            String name1 = jsonObject1.getString(keywordName);
                            Map<String, Object> jsonMap2 = mapper.readValue(json2, Map.class);
                            JSONObject jsonObject2 = new JSONObject(jsonMap2);
                            String name2 = jsonObject2.getString(keywordName);
                            // 使用Collator比较中文名
                            return collator.compare(name1, name2);
                        } catch (Exception e) {
                            logger.error("name对比错误", e);
                            return 0;
                        }
                    })
                    .limit(limit).collect(Collectors.toList());
        }
    }

    /**
     * 获取hashKey中limit条数据
     *
     * @param hashKey    数据结构类型
     * @param limit      获取的条数（默认为最大值）
     * @param keywordMap <搜索关键字，搜索字段>。可空，根据第一个值的中文排序
     * @return
     */
    @NaslLogic
    public List<String> getHashTopNByKeyMap(String hashKey, Integer limit, Map<String, String> keywordMap) {
        if (limit == null | limit == 0) {
            limit = Integer.MAX_VALUE;
        }
        List<String> allList = redisTemplate.<String, Object>opsForHash()
                .values(hashKey)
                .stream()
                .limit(Integer.MAX_VALUE)
                .map(Object::toString)
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(keywordMap)) {
            return allList;
        } else {
            return allList.stream().filter(json -> {
                        try {
                            Map<String, Object> jsonMap = mapper.readValue(json, Map.class);
                            JSONObject jsonObject = new JSONObject(jsonMap);
                            return keywordMap.entrySet().stream().allMatch(entry -> {
                                String jsonValue = jsonObject.getString(entry.getKey());
                                return !StringUtils.isEmpty(jsonValue) &&
                                        jsonValue.contains(entry.getValue());
                            });
                        } catch (Exception e) {
                            logger.error("json转换错误", e);
                            return false;
                        }
                    }).sorted((json1, json2) -> {
                        try {
                            // 创建中文拼音排序器
                            Collator collator = Collator.getInstance(Locale.CHINA);
                            // 获取两个json对象的中文名
                            Map<String, Object> jsonMap1 = mapper.readValue(json1, Map.class);
                            JSONObject jsonObject1 = new JSONObject(jsonMap1);
                            String keywordName = keywordMap.entrySet().iterator().next().getKey();
                            String name1 = jsonObject1.getString(keywordName);
                            Map<String, Object> jsonMap2 = mapper.readValue(json2, Map.class);
                            JSONObject jsonObject2 = new JSONObject(jsonMap2);
                            String name2 = jsonObject2.getString(keywordName);
                            // 使用Collator比较中文名
                            return collator.compare(name1, name2);
                        } catch (Exception e) {
                            logger.error("name对比错误", e);
                            return 0;
                        }
                    })
                    .limit(limit).collect(Collectors.toList());
        }
    }

    /**
     * 检查 Redis 哈希表中是否存在指定的域
     *
     * @param hashKey  哈希表的 key
     * @param fieldKey 要检查的域
     * @return 如果存在，返回 true；否则，返回 false
     */
    @NaslLogic
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
    @NaslLogic
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
    @NaslLogic
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
    @NaslLogic
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
    @NaslLogic
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
    @NaslLogic
    public Double hashIncrementByFloat(String hashKey, String fieldKey, Double increment) {
        HashOperations<String, String, String> ops = redisTemplate.opsForHash();
        return ops.increment(hashKey, fieldKey, increment);
    }
}
