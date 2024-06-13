import com.netease.http.config.NosConfig;
import com.netease.http.dto.RequestParam;
import com.netease.http.dto.RequestParamAllBodyType;
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

    @Test
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

        RequestParamAllBodyType requestParam = new RequestParamAllBodyType();
        requestParam.setUrl(fileUrl);
        requestParam.setHttpMethod("get");
        requestParam.setIsIgnoreCrt(false);
        String url = lcapHttpClient.exchangeAllBodyType(requestParam);
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
