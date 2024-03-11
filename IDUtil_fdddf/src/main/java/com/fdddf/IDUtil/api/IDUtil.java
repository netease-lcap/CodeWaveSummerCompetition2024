package com.fdddf.IDUtil.api;

import com.fdddf.IDUtil.YitIdGeneratorOptions;
import com.fdddf.IDUtil.IDUtilTime;
import com.fdddf.IDUtil.SnowflakeIdGenerator;
import com.fdddf.IDUtil.SnowflakeOptions;
import com.github.yitter.contract.IdGeneratorOptions;
import com.github.yitter.idgen.YitIdHelper;
import com.netease.lowcode.core.annotation.NaslLogic;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Date;
import java.util.Random;
import java.util.regex.*;
import java.util.UUID;
import java.text.SimpleDateFormat;

/**
 * 生成ID的工具类。
 */
@Component
public class IDUtil {

    /**
     * 生成一个全局唯一的标识符（UUID）。
     *
     * @return 返回一个随机生成的UUID字符串。
     */
    @NaslLogic
    public static String UUID() {
        // 通过UUID类的randomUUID方法生成一个随机UUID，并将其转换为字符串形式
        return UUID.randomUUID().toString();
    }

    /**
     * 生成一个简单的UUID字符串。
     * 该方法通过调用UUID.randomUUID()生成一个随机的UUID，然后将UUID的连字符("-")移除，返回一个格式简化了的UUID字符串。
     *
     * @return 返回一个不含连字符的UUID字符串。
     */
    @NaslLogic
    public static String UUIDSimple() {
        // 生成一个随机UUID
        UUID uuid = UUID.randomUUID();
        // 移除UUID中的连字符，返回简化后的字符串
        return uuid.toString().replace("-", "");
    }

    /**
     * 检查提供的字符串是否为有效的UUID。
     *
     * @param uuid 需要检查的字符串。
     * @return 字符串"1"表示是有效的UUID，字符串"0"表示不是有效的UUID。
     */
    @NaslLogic
    public static String isValidUUID(String uuid) {
        // UUID的正则表达式
        String regex = "[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}";
        // 使用正则表达式匹配UUID
        return Pattern.matches(regex, uuid) ? "1" : "0";
    }

    /**
     * 生成基于Snowflake算法的ID。
     * Snowflake算法是一种分布式ID生成算法，生成的ID由时间戳、机器标识、序列号等部分组成，具有全局唯一性。
     *
     * @param options 包含Snowflake算法所需的配置，如workerId和datacenterId。
     * @return 返回生成的Snowflake算法ID的字符串表示。
     */
    @NaslLogic
    public static String SnowflakeId(SnowflakeOptions options) {
        // 创建SnowflakeIdGenerator实例，初始化机器标识和数据中心标识
        int workerId = Integer.parseInt(options.workerId);
        int datacenterId = Integer.parseInt(options.datacenterId);
        SnowflakeIdGenerator generator = new SnowflakeIdGenerator(workerId, datacenterId);
        // 生成并返回一个ID的字符串形式
        return String.valueOf(generator.nextId());
    }


    /**
     * 生成基于当前时间的十六进制ID。
     * 该方法通过获取当前时间戳（毫秒级），然后将其转换为十六进制字符串来实现。
     *
     * @return 返回一个表示当前时间戳的十六进制字符串。
     */
    @NaslLogic
    public static String HexTimeId() {
        // 获取当前时间戳
        long timestamp = System.currentTimeMillis();
        // 将时间戳转换为十六进制字符串并返回
        return Long.toHexString(timestamp);
    }

    /**
     * 根据给定的开始时间生成一个包含时间戳和随机数的字符串。
     * @param t 包含开始时间和随机长度的对象。
     * @return 返回一个字符串，由当前时间与开始时间的差值（毫秒）和随机数组成。
     * @throws ParseException 如果开始时间的格式不正确，解析时会抛出此异常。
     */
    @NaslLogic
    public static String TimeId(IDUtilTime t) throws ParseException {
        // 将开始时间转换为时间戳
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date startDate = sdf.parse(t.startTime);
        long startTimeStamp = startDate.getTime();
        startTimeStamp = System.currentTimeMillis() - startTimeStamp;

        // 生成随机数
        int randomLength = Integer.parseInt(t.randomLength);
        if (randomLength > 0) {
            Random random = new Random();
            int randomNumber = random.nextInt((int) Math.pow(10, randomLength));
            return startTimeStamp + "" + randomNumber;
        }
        // 返回时间戳连接随机数
        return String.valueOf(startTimeStamp);
    }

    /**
     *
     * 使用优化的雪花算法用指定的配置选项生成一个唯一的ID
     * {@code @reference} <a href="https://github.com/yitter/IdGenerator">IdGenerator</a>
     * @param options 包含ID生成器配置的选项，如工作ID、序列号位长等。
     * @return 返回一个基于配置选项生成的唯一ID的字符串表示。
     */
    @NaslLogic
    public static String YitId(YitIdGeneratorOptions options) {
        // 将传入的配置选项中的工作ID解析为整数，并基于此创建初始配置对象
        int workerId = Integer.parseInt(options.WorkerId);
        IdGeneratorOptions originOptions = new IdGeneratorOptions((short) workerId);

        // 设置ID生成器的配置参数
        originOptions.WorkerIdBitLength = Byte.parseByte(options.WorkerIdBitLength);
        originOptions.SeqBitLength = Byte.parseByte(options.SeqBitLength);
        originOptions.MaxSeqNumber = Short.parseShort(options.MaxSeqNumber);
        originOptions.MinSeqNumber = Short.parseShort(options.MinSeqNumber);
        originOptions.TopOverCostCount = Short.parseShort(options.TopOverCostCount);

        // 应用配置到ID生成器
        YitIdHelper.setIdGenerator(originOptions);

        // 返回下一个生成的ID的字符串形式
        return String.valueOf(YitIdHelper.nextId());
    }

}

