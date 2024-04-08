import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.Optional;


/**
 * 银擎OpenApi鉴权 实例化
 *
 * @author wangtong07 2022/3/10 9:49
 */
public class OpenApiTokens {

    public static final String AUTH_HEADER_APP_ID = "appId";

    public static final String AUTH_HEADER_TIMESTAMP = "timestamp";

    public static final String AUTH_HEADER_SIGNATURE = "signature";

    private OpenApiTokens() {
        //NoArgsConstructor
    }

    public static OpenApiTokenBuilder builder() {
        return new OpenApiTokenBuilder();
    }

//    public static void main(String[] args) throws URISyntaxException {
//        String appId = Uid.uuid().replaceAll(DASH, "");
//        System.out.println(appId);
//    }

    public static class OpenApiTokenBuilder {

        private URI uri;

        private Object body;

        private String appId;

        private String appSecret;

        private Date issuedAt;

        public OpenApiTokenBuilder() {
            //NoArgsConstructor
        }


        /**
         * @param appId 系统应用唯一标识（签发者）
         * @return OpenApiToken
         */
        public OpenApiTokenBuilder appId(String appId) {
            Assert.notNull(appId, "appId argument cannot be null.");
            this.appId = appId;
            return this;
        }

        /**
         * @param uri 请求uri
         * @return OpenApiToken
         */
        public OpenApiTokenBuilder uri(URI uri) {
            Assert.notNull(uri, "uri argument cannot be null.");
            this.uri = uri;
            return this;
        }

        /**
         * @param body 请求体
         * @return OpenApiToken
         */
        public OpenApiTokenBuilder body(Object body) {
            this.body = body;
            return this;
        }

        /**
         * @param issuedAt 签发时间
         * @return OpenApiToken
         */
        public OpenApiTokenBuilder issuedAt(Date issuedAt) {
            Assert.notNull(issuedAt, "issuedAt argument cannot be null.");
            this.issuedAt = issuedAt;
            return this;
        }

        /**
         * @param appSecret 私钥
         * @return OpenApiToken
         */
        public OpenApiTokenBuilder signWith(String appSecret) {
            Assert.notNull(appSecret, "appSecret argument cannot be null.");
            this.appSecret = appSecret;
            return this;
        }

        /**
         * 生成Http头部鉴权
         *
         * @return HttpHeaders
         */
        public HttpHeaders compact() {
            HttpHeaders headers = new HttpHeaders();
            long timestamp = issuedAt.getTime();
            headers.add(AUTH_HEADER_APP_ID, appId);
            headers.add(AUTH_HEADER_TIMESTAMP, String.valueOf(timestamp));
            headers.add(AUTH_HEADER_SIGNATURE, OpenApiSigner.signature(uri, body, timestamp, appSecret));
            return headers;
        }
    }

    /**
     * 签名工具类
     */
    public static class OpenApiSigner {

        private static final String ALGORITHM = "HmacSHA256";

        private static final char[] DIGITS_LOWER = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

        private static final ObjectMapper DEFAULT_SERIALIZER = new ObjectMapper();

        static {
            DEFAULT_SERIALIZER.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            DEFAULT_SERIALIZER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        }

        private OpenApiSigner() {
            //NoArgsConstructor
        }

        /**
         * @param uri       请求地址
         * @param body      请求体
         * @param timestamp 时间戳毫秒值
         * @param appSecret app密钥
         * @return 签名摘要
         */
        public static String signature(@NonNull URI uri, Object body, @NonNull Long timestamp, @NonNull String appSecret) {
            String jsonBody = Optional.ofNullable(body).map(OpenApiSigner::serialize).orElse(null);
            return signature(uri, jsonBody, timestamp, appSecret);
        }

        /**
         * @param uri       请求地址
         * @param jsonBody  请求体序列化字符串
         * @param timestamp 时间戳毫秒值
         * @param appSecret app密钥
         * @return 签名摘要
         */
        public static String signature(@NonNull URI uri, String jsonBody, @NonNull Long timestamp, @NonNull String appSecret) {
            String reformatUri = uri.getRawPath() + "?" + uri.getRawQuery();
            return signature(parsePayload(reformatUri, timestamp, jsonBody), appSecret);
        }

        /**
         * 签名加密
         *
         * @param payload   需加密对象
         * @param appSecret app秘钥
         * @return 签名摘要
         */
        public static String signature(@NonNull String payload, @NonNull String appSecret) {
            try {
                Key keySpec = new SecretKeySpec(appSecret.getBytes(StandardCharsets.UTF_8), ALGORITHM);
                Mac mac = Mac.getInstance(ALGORITHM);
                mac.init(keySpec);
                char[] digest = encodeHex(mac.doFinal(payload.getBytes(StandardCharsets.UTF_8)));
                return new String(digest);
            } catch (Exception e) {
                throw new IllegalArgumentException("signature failed.", e);
            }
        }

        /**
         * 将需要签名部分连接格式化
         *
         * @param url       请求地址
         * @param timestamp 时间戳毫秒值
         * @param body      请求体
         * @return payload
         */
        private static String parsePayload(String url, long timestamp, String body) {
            String bodyStr = body == null ? "" : body + "\n";
            return url + "\n" + timestamp + "\n" + bodyStr;
        }

        /**
         * 请求体序列化
         *
         * @param obj 需要转json的对象
         * @return json
         */
        private static String serialize(@NonNull Object obj) {
            try {
                return DEFAULT_SERIALIZER.writeValueAsString(obj);
            } catch (JsonProcessingException e) {
                throw new IllegalArgumentException(String.format("serialize for class [%s] failed. ", obj.getClass().getName()), e);
            }
        }

        private static char[] encodeHex(byte[] data) {
            int l = data.length;
            char[] out = new char[l << 1];
            int i = 0;
            int j = 0;
            for (; i < l; ++i) {
                out[j++] = DIGITS_LOWER[(240 & data[i]) >>> 4];
                out[j++] = DIGITS_LOWER[15 & data[i]];
            }
            return out;
        }
    }
}
