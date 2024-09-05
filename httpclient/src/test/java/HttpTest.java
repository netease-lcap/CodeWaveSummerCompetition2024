import com.netease.http.dto.RequestParam;
import com.netease.http.httpclient.HttpClientService;
import com.netease.http.httpclient.LCAPHttpClient;
import com.netease.http.spring.HttpSpringEnvironmentConfiguration;
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


//@SpringBootTest(classes = HttpSpringEnvironmentConfiguration.class)
//@RunWith(SpringRunner.class)
public class HttpTest {
    @Resource
    private RestTemplate restTemplate;
    @Resource
    private LCAPHttpClient lcapHttpClient;
    @Resource
    private HttpClientService httpClientService;

//    @Test
    public void testV2() {
        String url = "http://127.0.0.1:8090/expand/transfer/get_result?key=1";
        Map<String, String> head = new HashMap<>();
        head.put("Content-Type", "application/json");
        head.put("User-Agent", "Apifox/1.0.0 (https://apifox.com)");
        Map<String, String> body = new HashMap<>();
        body.put("userName", "222");
        body.put("password", "11");
        String urlRes = lcapHttpClient.exchangeV2(url, HttpMethod.GET.name(), head, body);
        System.out.println(urlRes);
    }

//    @Test
    public void testV22() {
        String url = "http://127.0.0.1:8090/api/office_to_pdf/pptToPdf";
        Map<String, String> head = new HashMap<>();
        head.put("Content-Type", "application/json");
        head.put("User-Agent", "Apifox/1.0.0 (https://apifox.com)");
        Map<String, String> body = new HashMap<>();
        body.put("fileUrl", "http://dev.bjztest.neteasepmo.lcap.163yun.com/upload/app/06889c61-5b85-4b8a-9eac-8d14f9159f8a/aaaa-50%E9%A1%B5_20240730105501059.pptx");
        body.put("type", "1");
        String urlRes = lcapHttpClient.exchangeV2(url, HttpMethod.POST.name(), head, body);
        System.out.println(urlRes);
    }

//    @Test
    public void testExchangeCrtForm() {
        RequestParam requestParam = new RequestParam();
        String url = "https://test.sovell.com/single-demeter/merger/api/operator/login";
        Map<String, String> head = new HashMap<>();
        head.put("Content-Type", "multipart/form-data");
        Map<String, String> body = new HashMap<>();
        body.put("username", "18888888888");
        body.put("password", "zMEidaK9kEra6Ub1ptl2Sw==");
        body.put("platform", "single_demeter");

        requestParam.setUrl(url);
        requestParam.setHttpMethod("get");
//        requestParam.setHeader(head);
        requestParam.setBody(body);
        String urlRes = lcapHttpClient.exchangeCrtForm(requestParam);
        System.out.println(urlRes);
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

    //        @Test
    public void testDownload() {
        URI uri = UriComponentsBuilder
                .fromUriString("http://www.baidu.com")
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
        String url = lcapHttpClient.downloadFileUploadNos(uri.toString(), null, header);
        System.out.println(url);
    }
}
