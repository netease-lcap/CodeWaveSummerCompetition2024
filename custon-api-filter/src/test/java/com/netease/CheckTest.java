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

//    public static void main(String[] args) {
//        //生成rsa密码对
//        KeyPairGenerator keyPairGenerator = null;
//        try {
//            keyPairGenerator = KeyPairGenerator.getInstance("RSA");
//            keyPairGenerator.initialize(2048);//设置密码长度
//            KeyPair keyPair = keyPairGenerator.generateKeyPair();
//            PublicKey publicKey = keyPair.getPublic();//获取公钥
//            PrivateKey privateKey = keyPair.getPrivate();//获取私钥
//            String publicKeyStr = Base64.getEncoder().encodeToString(publicKey.getEncoded());
//            String privateKeyStr = Base64.getEncoder().encodeToString(privateKey.getEncoded());
//            System.out.println("公钥: " + publicKeyStr);
//            System.out.println("私钥: " + privateKeyStr);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    public static void main(String[] args) {
//        long now = System.currentTimeMillis();
//        System.out.println(now - 1704769060306L);

        System.out.println(System.currentTimeMillis() - Long.parseLong("1709522196907"));
        long requesttime=1709522196907l;
        //时间戳转yyyy-mm-dd hh:mm:ss
        String time = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date(requesttime));
        System.out.println(time);
    }

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

    @Test
    public void testSign() {
        NonceRedisCheckServiceImpl.RequestHeader requestHeader = new NonceRedisCheckServiceImpl.RequestHeader(
                "HUYaJ/nMmJ42bToxKliRFh2lpueGOFu0C5HBJlDe4FmkQoUYWVzuFg7otyWvsjCpyPHp8AAII0bdsfeW8K0NJ6RrHuJdFxOnI7REzuacaP2tfMhXTSs30MJZPD9Ju4jJltHFylNfcRWpgELzE95yiK+/zTcnanWjVA6LUm2mDsnTMkDTCih15AkLDea0Uz8oCH9T3KsSn86UkxHSm6K6ErPrDBbAczXJpEnqK590qfzACgwejNmkRLiTHB3G4JHkwwQ84gkIy1V13qa1bJ2etlYl+OQPstQBhqMngrd91OOWXdvl3E00G/fGpBzG/AviH0dTUHcFXb7rUTWFESFW9A==",
                "1704682887427",
                "d47a4afb56941d640837af26824c6262"
        );
        System.out.println(checkSign(requestHeader));
    }

    private boolean checkSign(NonceRedisCheckServiceImpl.RequestHeader requestHeader) {
        byte[] publicKeyBytes = Base64.getDecoder().decode("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEArQEWUVOCXxtkYr8a/XFH/rkMfQ8CPBy7kT6cSZI8olBuGifHc38+ECKAGmbCRinySQ5KCxM9itaXQyAz+zbK/SY6iXrqi9xd0vIRsXVZddLdXNGjJtpmBnC/dxIf77JVIV/uT1Ft6IkNZh5dK38uIDecKKUUMiaG3TDFOOeTWI/esrF1ISnL4WB+oCR9la70EbxHdia1G2tQyFpUsl9YUhTcxCIfPzEDNpX3nF4rAzdWYO01fD0jkV4NYjG0xsPUz64PRkGE48+BpdSBV1vs4d/C7deR2hOpkEHYQtp4JDxY8MgqOySgagck9XzCiqECnlnbPEWTeOcLzYWqKn0cKQIDAQAB");
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


//    公钥: MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEArQEWUVOCXxtkYr8a/XFH/rkMfQ8CPBy7kT6cSZI8olBuGifHc38+ECKAGmbCRinySQ5KCxM9itaXQyAz+zbK/SY6iXrqi9xd0vIRsXVZddLdXNGjJtpmBnC/dxIf77JVIV/uT1Ft6IkNZh5dK38uIDecKKUUMiaG3TDFOOeTWI/esrF1ISnL4WB+oCR9la70EbxHdia1G2tQyFpUsl9YUhTcxCIfPzEDNpX3nF4rAzdWYO01fD0jkV4NYjG0xsPUz64PRkGE48+BpdSBV1vs4d/C7deR2hOpkEHYQtp4JDxY8MgqOySgagck9XzCiqECnlnbPEWTeOcLzYWqKn0cKQIDAQAB
//    私钥: MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCtARZRU4JfG2Rivxr9cUf+uQx9DwI8HLuRPpxJkjyiUG4aJ8dzfz4QIoAaZsJGKfJJDkoLEz2K1pdDIDP7Nsr9JjqJeuqL3F3S8hGxdVl10t1c0aMm2mYGcL93Eh/vslUhX+5PUW3oiQ1mHl0rfy4gN5wopRQyJobdMMU455NYj96ysXUhKcvhYH6gJH2VrvQRvEd2JrUba1DIWlSyX1hSFNzEIh8/MQM2lfecXisDN1Zg7TV8PSORXg1iMbTGw9TPrg9GQYTjz4Gl1IFXW+zh38Lt15HaE6mQQdhC2ngkPFjwyCo7JKBqByT1fMKKoQKeWds8RZN45wvNhaoqfRwpAgMBAAECggEAYhWPtaQG8izxiRqYDsC+9KetcOmldrdDkGFS8lM6lTvMcH/FELI/WZ51yoIf+u+lMc4sJINQDPsE30XOssqp40RdQ7ywZYMWnpYnedjyU16tqgYg3dJMrbIWm0dnW9pbcYSV89pS2dd6BT+JyzNbz2A35MNS4+v74kPYTR7d5wMZW0x35RtN57R/lYkm863iduXV+/9ILV71hF6k3pjoXOxUoycg9GyhsCEG2vdGkTsqkE+S4X2uz6IKQHpqD3bnGfNVeJteTRPPszoPkJF+fRzEmuIUbW16GlcVL2cJOcaG9n3oAkShzeBAVQruUj+sZt1HQ2nRmTIIJ49jQz4cQQKBgQDyRJ2xYWu+4HVJmAUS9ZWATLmcr2zEBxDueCTYqAx1FIcQqW5IEPThw98y6euK+Ms3odgOCNOveduGEHhXEfVqNrLjyxxodcoiIusgfENVyj/pRdHPpTk74iFdknyDTU9BqGfYEHLT//050kYgaSvQEZ8hKIW/+UWp5U1DZFR1rQKBgQC2z26pTwCH9AgtVCBuzwcHGN8xJMcyVfpm652fjlJJ2/0SW2LevCe/6Ozm9Bt4tbhZ3XdMid54DSRDHqbbK/B2VQCvi+U1SQyWnAGVefEMgVkCsd7LVQCE1Nb7PJ2dThhToYZyMI9ua0rVW01RtDnQ6qP5wyhvWqz5xsQp55Q37QKBgHy5rqW/jgT68ectiI82N/2Rgo2UEt7o9GKRIAcanS85nj+JuukHMct5V9OScl1dsOG3RNAU40aX6difahUJfdtsxbRYwQtWePYSHBLhZpkoQrcJ5vaMDWczCBJwdAH1LzSSSsQF09gpFmlKRCgzfQbly5L6lcE7yA2rq5dic8kRAoGATMMXZZKd13iaAmaj48Oc4NcJyTn3RuSha5weKMR7OzZbstyazIpXapck+umfq3Uqvtlm4cW2m9Rw2JpDWB7ALwAMdKoaieBtn8RMSfbVR/n8ZUxJEDpWKHCzAPX3/Piw8FUP/lzzrVryJlrbQaaa7GXKivLuxKtfnNZgwnPSYhUCgYANrCmk9XmynDuY8xQseI+SpljbP3GdpxgulD9FZAdKPlHndWVAmSmgIAef5rpp7wLjDRdm0wH1mIFZwaYZPdGsFZ92nNOZFLyVGjpa/bWkn7pJ+zI2dw17oVza1gS2QdoVosyKQ4LWccXF5UQsMo5LWpuLqTVDc2kfh1lCw/9tjA==
}
