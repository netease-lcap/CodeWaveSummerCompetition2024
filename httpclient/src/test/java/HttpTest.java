import com.alibaba.fastjson.JSONObject;
import com.netease.http.config.NosConfig;
import com.netease.http.dto.RequestParam;
import com.netease.http.httpclient.HttpClientService;
import com.netease.http.httpclient.LCAPHttpClient;
import com.netease.http.spring.HttpSpringEnvironmentConfiguration;
import com.netease.http.util.NosUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.Resource;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


@SpringBootTest(classes = HttpSpringEnvironmentConfiguration.class)
@RunWith(SpringRunner.class)
public class HttpTest {
    @Resource
    private RestTemplate restTemplate;
    @Resource
    private LCAPHttpClient lcapHttpClient;
    @Resource
    private HttpClientService httpClientService;

    //    @Test
    public void testV2() {
        String url = "";
        Map<String, String> head = new HashMap<>();
        head.put("Content-Type", "application/json");
        head.put("User-Agent", "Apifox/1.0.0 (https://apifox.com)");
        Map<String, String> body = new HashMap<>();
        body.put("userName", "222");
        body.put("password", "11");
        String urlRes = lcapHttpClient.exchangeV2(url, HttpMethod.POST.name(), head, body);
        System.out.println(urlRes);
    }

    @Test
    public void testHttp() {
        String fileUrl = "";

//        RequestParamAllBodyType requestParam = new RequestParamAllBodyType();
//        requestParam.setUrl(fileUrl);
//        requestParam.setHttpMethod("get");
//        requestParam.setIsIgnoreCrt(false);
//        String url = lcapHttpClient.exchangeAllBodyType(requestParam);
        String a = "{\n" +
                "  \"header\": {\n" +
                "    \"Authorization\": \"Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICIwN3NweXdPSm1QRng2RGgxMTZLeFBDd3hHVEFYd3pwOXMwT2dPOUJTaHlNIn0.eyJleHAiOjE3MTkzMTY0NDEsImlhdCI6MTcxOTI4MDQ2MCwiYXV0aF90aW1lIjoxNzE5MjgwNDQxLCJqdGkiOiI1MzFiYTRiMS1mYThlLTQ0NWYtYTI1Mi1lZWQyNWQyOTg2YWQiLCJpc3MiOiJodHRwczovL29uZWlhbS5vbmVzdGFyLmlvOjgwODEvcmVhbG1zL0NPU0wtUmVhbG0tODA4MSIsImF1ZCI6WyJyZWFsbS1tYW5hZ2VtZW50IiwiYWNjb3VudCJdLCJzdWIiOiIzOTFiMTE0Ny1jYzIwLTQ3MWEtOTAwNS00NGJjNzc2YTRiMDAiLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiJjb3NsLTgwODEtY2xpZW50LXRlc3QtMSIsInNlc3Npb25fc3RhdGUiOiJhN2YyMjE0Ny1mNzY3LTQ4NjEtOGNiOC02YjkxZWIwNWIwNWQiLCJhY3IiOiIxIiwiYWxsb3dlZC1vcmlnaW5zIjpbIiIsImh0dHA6Ly8xMTQuMTE1LjE1Ni4xODI6MTQ5MDAiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbIm9mZmxpbmVfYWNjZXNzIiwidW1hX2F1dGhvcml6YXRpb24iLCJkZWZhdWx0LXJvbGVzLWNvc2wtcmVhbG0tODA4MSJdfSwicmVzb3VyY2VfYWNjZXNzIjp7InJlYWxtLW1hbmFnZW1lbnQiOnsicm9sZXMiOlsibWFuYWdlLXVzZXJzIiwidmlldy11c2VycyIsInF1ZXJ5LWdyb3VwcyIsInF1ZXJ5LXVzZXJzIl19LCJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50IiwibWFuYWdlLWFjY291bnQtbGlua3MiLCJ2aWV3LXByb2ZpbGUiXX19LCJzY29wZSI6ImVtYWlsIHByb2ZpbGUgb3BlbmlkIiwic2lkIjoiYTdmMjIxNDctZjc2Ny00ODYxLThjYjgtNmI5MWViMDViMDVkIiwiZWhyX3BrIjoiOTU2MzI1IiwiZW1haWxfdmVyaWZpZWQiOmZhbHNlLCJtYWluX2RlcGFydG1lbnRfaWQiOiIxMjMiLCJuYW1lIjoi6L2v5Lu257O757uf57u05oqk6YOoIiwicHJlZmVycmVkX3VzZXJuYW1lIjoibWF6bSIsImdpdmVuX25hbWUiOiLpqazlv5fmlY8iLCJzb3J0X2NvZGUiOiIyIiwiZW1haWwiOiI5MjQxMzI3NzVAcXEuY29tIiwiZ3JvdXAiOlsiL-S4rea1t-ayueeUsOacjeWKoeiCoeS7veaciemZkOWFrOWPuC_mlbDlrZfmioDmnK_kuK3lv4Mv6L2v5Lu257O757uf57u05oqk6YOoIl19.IGd0Wo-2kJ-IRezf73k3rbQPP7w5icx7C3qZVAhmQ3N9HW6zq7Oz7E_x6jzN-vvuzn4Fk3SyKmo-2izpWBNDMv5PuCGa1--hsHkjHdzg_Dl1x13ShQw_Bqb6zpj2ev_PQnrVWu-FW1nNfVHD60a_BLDW26VPDeiY_5Q_OieGB2r7bVPC8kC4clsVYcm1cZq-TTqpKxE3dx670hhiuwnRDy-x9-kwlD-XfTdXh-79ADLtGRf73ekizsyAyBlCrrc-bML5UNjgt-ZU_d2WOz1No7DL3h_lUY8Z4_G21YRff9ODRABlHajlomByFfQzadXYe6rhj8_vkfvGYdEjJlQIjg\",\n" +
                "    \"Content-Type\": \"application/x-www-urlencoded\"\n" +
                "  },\n" +
                "  \"httpMethod\": \"get\",\n" +
                "  \"body\": null,\n" +
                "  \"isIgnoreCrt\": true,\n" +
                "  \"url\": \"https://oneiam.onestar.io:8081/realms/COSL-Realm-8081/protocol/openid-connect/userinfo\";\n" +
                "}";
        RequestParam requestParam = JSONObject.parseObject(a, RequestParam.class);
        String url = lcapHttpClient.exchangeCrtForm(requestParam);
        System.out.println(url);
        requestParam.setIsIgnoreCrt(true);
    }

    //    @Test
    public void testHttpBody() throws URISyntaxException {
        RequestParam requestParam = new RequestParam();
        requestParam.setUrl("");
        requestParam.setHttpMethod("POST");
        Map<String, String> head = new HashMap<>();
//        head.put("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        requestParam.setHeader(head);
        Map<String, String> body = new HashMap<>();
        body.put("username", "2131");
        body.put("password", "21312123=");
        requestParam.setBody(body);
//        requestParam.setIsIgnoreCrt(true);
        try {
            String url = lcapHttpClient.exchangeCrt(requestParam);
            System.out.println(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        requestParam.setIsIgnoreCrt(true);
//        url = lcapHttpClient.exchange(requestParam.getUrl(), requestParam.getHttpMethod(), requestParam.getHeader(), requestParam.getBody());
//        System.out.println(url);
    }

    //    @Test
    public void testDownload() {
        URI uri = UriComponentsBuilder
                .fromUriString(" ")
                .queryParam("uid", "3213131")
                .encode()
                .build()
                .toUri();
        HttpHeaders authHeader = OpenApiTokens.builder()
                .uri(uri)
                .body(null)
                .appId("APP_ID")
                .signWith("APP_SECRET")
                .issuedAt(new Date())
                .compact();
        Map<String, String> header = new HashMap<>();
        header.put("appid", Objects.requireNonNull(authHeader.get(OpenApiTokens.AUTH_HEADER_APP_ID)).get(0));
        header.put("timestamp", Objects.requireNonNull(authHeader.get(OpenApiTokens.AUTH_HEADER_TIMESTAMP)).get(0));
        header.put("signature", Objects.requireNonNull(authHeader.get(OpenApiTokens.AUTH_HEADER_SIGNATURE)).get(0));
        NosUtil.nosConfig = new NosConfig();
        NosUtil.nosConfig.nosAccessKey = "11";
        NosUtil.nosConfig.nosSecretKey = "11";
        NosUtil.nosConfig.nosAddress = "11";
        NosUtil.nosConfig.nosBucket = "11";
        NosUtil.nosConfig.sinkType = "11";
        String url = lcapHttpClient.downloadFileUploadNos(uri.toString(), null, header);
        System.out.println(url);
    }
}
