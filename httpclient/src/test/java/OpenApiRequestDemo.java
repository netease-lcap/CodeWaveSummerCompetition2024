import com.netease.http.config.NosConfig;
import com.netease.http.dto.RequestParamAllBodyType;
import com.netease.http.httpclient.LCAPHttpClient;
import com.netease.http.util.NosUtil;
import com.netease.lowcode.core.util.json.JSONObject;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 签名规则
 * <p>
 * 1. 签名串一共有3行，每一行为一个参数。行尾以 \n（换行符，ASCII编码值为0x0A）结束，包括最后一行。如果参数本身以\n结束，也需要附加一个\n。
 * <p>
 * 编码后的URL\n<br/>
 * 请求时间戳\n<br/>
 * 请求报文主体\n<br/>
 * <p>
 * 示例：
 * <p>
 * /api/openapi/hello?param1=%E5%8F%82%E6%95%B01&param2=%E5%8F%82%E6%95%B02-1<br/>
 * 1619402824748<br/>
 * {"name":"张三","age":22,"birth":1619402819245,"enabled":true}<br/>
 * <p>
 * 2. 对1步骤生成的字符串+appSecret使用HMAC_SHA256算法生成签名
 * <p>
 * 3. 发送的请求头中附带上appId、timestamp、signature参数（请勿附带appSecret参数）
 * <p>
 * 4. 获取返回结果
 */
public class OpenApiRequestDemo {

    public static final String APP_ID = "ZQ_PMS";

    public static final String APP_SECRET = "bc25e4c6428d47ec94a051a5614cecc1";

    private static final RestTemplate RESTTEMPLATE = new RestTemplate();

    public static void testDownload() {
        URI uri = UriComponentsBuilder
                .fromUriString("https://crpdev.fin.netease.com/api/openapi/contract/attachment/download")
                .queryParam("uid", "10000566")
                .encode()
                .build()
                .toUri();
        HttpHeaders authHeader = OpenApiTokens.builder()
                .uri(uri)
                .body(null)
                .appId(APP_ID)
                .signWith(APP_SECRET)
                .issuedAt(new Date())
                .compact();
        Map<String, String> header = new HashMap<>();
        header.put("appid", Objects.requireNonNull(authHeader.get(OpenApiTokens.AUTH_HEADER_APP_ID)).get(0));
        header.put("timestamp", Objects.requireNonNull(authHeader.get(OpenApiTokens.AUTH_HEADER_TIMESTAMP)).get(0));
        header.put("signature", Objects.requireNonNull(authHeader.get(OpenApiTokens.AUTH_HEADER_SIGNATURE)).get(0));
        System.out.println(JSONObject.valueToString(header));
        NosUtil.nosConfig = new NosConfig();
        NosUtil.nosConfig.nosAccessKey = "e09bc35070b3406eb4179c332da67146";
        NosUtil.nosConfig.nosSecretKey = "624e6295a44d44f7880ae968b10d0892";
        NosUtil.nosConfig.nosAddress = "nos-eastchina1.126.net";
        NosUtil.nosConfig.nosBucket = "lcpapp-static";
        NosUtil.nosConfig.sinkType = "nos";
        LCAPHttpClient lcapHttpClient = new LCAPHttpClient();
        lcapHttpClient.downloadFileUploadNos(uri.toString(), null, header);
    }

    /**
     * GET请求或非application/json类型的，body给null即可
     */
    public static void testGetDemo() {

        URI uri = UriComponentsBuilder
                .fromUriString("https://crpdev.fin.netease.com/api/openapi/contract/attachment/download")
                .queryParam("uid", "10000566")
                .encode()
                .build()
                .toUri();

        HttpHeaders authHeader = OpenApiTokens.builder()
                .uri(uri)
                .body(null)
                .appId(APP_ID)
                .signWith(APP_SECRET)
                .issuedAt(new Date())
                .compact();

        HttpEntity<BodyDemo> requestEntity = new HttpEntity<>(authHeader);
        ResponseEntity<String> resultResponseEntity = RESTTEMPLATE.exchange(uri, HttpMethod.GET, requestEntity, String.class);
        System.out.println(resultResponseEntity.getBody());
    }

    public static void main(String[] args) throws Exception {
        RequestParamAllBodyType requestParam = new RequestParamAllBodyType();
        requestParam.setUrl("http://www.baidu.com");
        requestParam.setHttpMethod("get");
        Map<String, String> head = new HashMap<>();
        head.put("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        requestParam.setHeader(head);
        System.out.println(JSONObject.valueToString(head));
//        Map<String, String> body = new HashMap<>();
//        body.put("param1", "value1");
//        body.put("param3", "value3");
        String body = "param1=value1&param2=value2";
        requestParam.setBody(body);
//        requestParam.setIsIgnoreCrt(true);
        LCAPHttpClient lcapHttpClient = new LCAPHttpClient();
        String url = lcapHttpClient.exchangeAllBodyType(requestParam);
        System.out.println(url);
    }


    public static class BodyDemo {
        private String name;
        private Integer age;
        private Date birth;
        private Boolean enabled;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }

        public Date getBirth() {
            return birth;
        }

        public void setBirth(Date birth) {
            this.birth = birth;
        }

        public Boolean getEnabled() {
            return enabled;
        }

        public void setEnabled(Boolean enabled) {
            this.enabled = enabled;
        }
    }
}
