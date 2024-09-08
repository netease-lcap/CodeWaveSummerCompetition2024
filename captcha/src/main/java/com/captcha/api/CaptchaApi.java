package com.captcha.api;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.captcha.config.RedisConfig;
import com.captcha.utils.CreateVerifyCode;
import com.netease.lowcode.core.annotation.NaslLogic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.SetParams;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.io.*;
import java.util.Base64;

@Component
public class CaptchaApi {

    private static final Logger logger = LoggerFactory.getLogger(CaptchaApi.class);

    @Resource
    private RedisConfig redisConfig;

    /**
     * 初始化验证码，获取captchaId（后续生成图形验证码和进行删除时需要使用captchaId）,
     * isDigit默认为false（true时验证码只有数字；false时验证码为大小写英文字母和数字）,
     * size默认为4（输入的数字等于验证码的长度）
     * @param isDigit
     * @param size
     * @return
     */
    @NaslLogic
    public String initCaptcha(Boolean isDigit, Integer size) {
        String captchaId = IdUtil.simpleUUID();
        String code;
        if (isDigit) {
            code = new CreateVerifyCode().randomDigit(size);
        } else {
            code = new CreateVerifyCode().randomStr(size);
        }
        // 缓存验证码
        try {
            Jedis jedis = new Jedis(redisConfig.redisHost, redisConfig.redisPort);
            jedis.auth(redisConfig.redisPassword);
            jedis.set(captchaId, code, new SetParams().ex(60));
        } catch (Exception e) {
            logger.error("初始化验证码失败", e);
        }
        logger.info("初始化验证码成功, captchaId:{}, code:{}", captchaId, code);
        return captchaId;
    }

    /**
     * 通过初始化得到的captchaId生成对应的验证码图片，将验证码图片返回成base64字符串
     * @param captchaId
     * @return
     */
    @NaslLogic
    public String drawCaptcha(String captchaId) {
        String base64Image = new String();
        try {
            Jedis jedis = new Jedis(redisConfig.redisHost, redisConfig.redisPort);
            jedis.auth(redisConfig.redisPassword);
            // 得到验证码 生成指定验证码
            String code = jedis.get(captchaId);
            CreateVerifyCode vCode = new CreateVerifyCode(116, 36, 4, 10, code);
            // 将 BufferedImage 转换为字节数组
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(vCode.getBuffImg(), "png", baos);
            byte[] imageBytes = baos.toByteArray();
            // 将字节数组编码为 Base64 字符串
            base64Image = Base64.getEncoder().encodeToString(imageBytes);
            logger.info("生成指定验证码成功, captchaId:{}, code:{}", captchaId, code);
        } catch (Exception e) {
            logger.error("生成指定验证码失败", e);
            return "error";
        }
        return base64Image;
    }

    /**
     * 匹配验证码是否正确（传入captchaId和验证码图片中的内容 code进行校验）
     * @param captchaId
     * @param code
     * @return
     */
    @NaslLogic
    public String checkCaptcha(String captchaId, String code) {
        if (StrUtil.isBlank(captchaId) || StrUtil.isBlank(code)) {
            return "请传入图形验证码所需参数captchaId或code";
        }
        try {
            Jedis jedis = new Jedis(redisConfig.redisHost, redisConfig.redisPort);
            jedis.auth(redisConfig.redisPassword);
            String redisCode = jedis.get(captchaId);
            if (StrUtil.isBlank(redisCode)) {
                return "验证码已过期，请重新获取";
            }

            if (!redisCode.toLowerCase().equals(code.toLowerCase())) {
                logger.info("验证码错误：code:" + code + "，redisCode:" + redisCode);
                return "图形验证码输入错误";
            }
            // 已验证清除key
            jedis.del(captchaId);
        } catch (Exception e) {
            logger.error("验证清除key失败", e);
        }
        logger.info("验证清除key成功, captchaId:{}, code:{}", captchaId, code);
        return "success";
    }

}
