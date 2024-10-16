package com.netease;

import com.netease.lowcode.custonapifilter.sign.CheckService;
import com.netease.lowcode.custonapifilter.sign.impl.NonceRedisCheckServiceImpl;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.ServiceLoader;

//@SpringBootTest(classes = SpringEnvironmentConfiguration.class)
//@RunWith(SpringRunner.class)
public class CheckTest {
    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    RedisConnectionFactory redisConnectionFactory;


    //    @Test
    public void queryDataTest() {

        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        operations.set("redis-key", "hello world");
        String redisKey = operations.get("redis-key");

        // 对redis的key的value进行加1
        redisTemplate.opsForValue().increment("increment-key");

        System.out.println("当前使用的redis客户端是: " + redisConnectionFactory.getConnection());
    }

    //    @Test
    public void testRedisConnector() {
        System.out.println(1);
//        redisConnector.getValue("a");
    }

    //    @Test
    public void testSomething() {
        ServiceLoader<CheckService> loader = ServiceLoader.load(CheckService.class);
        for (CheckService service : loader) {
            System.out.println(service);
        }
        // 进行测试逻辑
    }

//    @Test
    public void testSign() {
        NonceRedisCheckServiceImpl.RequestHeader requestHeader = new NonceRedisCheckServiceImpl.RequestHeader(
                "",
                "1704682887427",
                ""
        );
        System.out.println(checkSign(requestHeader));
    }

    private boolean checkSign(NonceRedisCheckServiceImpl.RequestHeader requestHeader) {
        byte[] publicKeyBytes = Base64.getDecoder().decode("");
        // 转换公钥字节数组为PublicKey对象
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
        KeyFactory keyFactory; // 或者其他算法
        Signature verifier;
        try {
            keyFactory = KeyFactory.getInstance("RSA");
            verifier = Signature.getInstance("SHA256withRSA");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        try {
            PublicKey publicKey = keyFactory.generatePublic(keySpec);
            verifier.initVerify(publicKey);
            verifier.update((requestHeader.getTimestamp() + requestHeader.getNonce()).getBytes());
            return verifier.verify(Base64.getDecoder().decode(requestHeader.getSign()));
        } catch (SignatureException | InvalidKeySpecException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }

//    public static void main(String[] args) throws Exception {
//        // 生成密钥对
//        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
//        keyGen.initialize(2048); // 指定密钥长度
//        KeyPair pair = keyGen.generateKeyPair();
//
//        // 获取公钥和私钥
//        PublicKey publicKey = pair.getPublic();
//        PrivateKey privateKey = pair.getPrivate();
//
//        // 将公钥和私钥以字符串形式输出
//        String publicKeyStr = Base64.getEncoder().encodeToString(publicKey.getEncoded());
//        String privateKeyStr = Base64.getEncoder().encodeToString(privateKey.getEncoded());
//
//        // 输出公钥和私钥
//        System.out.println("公钥: " + publicKeyStr);
//        System.out.println("私钥: " + privateKeyStr);
//    }
}
