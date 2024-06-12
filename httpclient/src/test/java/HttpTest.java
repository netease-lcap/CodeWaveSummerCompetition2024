import com.netease.http.config.NosConfig;
import com.netease.http.dto.RequestParam;
import com.netease.http.dto.RequestParamAllBodyType;
import com.netease.http.dto.RequestParamAllBodyTypeInner;
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
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


//@SpringBootTest(classes = HttpSpringEnvironmentConfiguration.class)
//@RunWith(SpringRunner.class)
public class HttpTest {
    public static final String APP_ID = "ZQ_PMS";
    public static final String APP_SECRET = "bc25e4c6428d47ec94a051a5614cecc1";
    @Resource
    private RestTemplate restTemplate;
    @Resource
    private LCAPHttpClient lcapHttpClient;
    @Resource
    private HttpClientService httpClientService;

//    @Test
    public void testV2() {
        String url = "https://id-test.163yun.com/v3/account/codeWave/login";
        Map<String, String> head = new HashMap<>();
        head.put("Content-Type", "application/json");
        head.put("User-Agent", "Apifox/1.0.0 (https://apifox.com)");
        Map<String, String> body = new HashMap<>();
        body.put("userName", "17340526870");
        body.put("password", "89dea581d0d3dc1030a15fe6cc615d29");
        String urlRes = lcapHttpClient.exchangeV2(url, HttpMethod.POST.name(), head, body);
        System.out.println(urlRes);
    }

//    @Test
    public void testHttp() {
        String fileUrl = "https://dev.bjztest.neteasepmo.lcap.163yun.com/upload/app/TileMapUtils_%E4%BE%9D%E8%B5%96%E5%BA%93%E4%BD%BF%E7%94%A8%E6%96%87%E6%A1%A3%E8%AF%B4%E6%98%8E_20240521100623466.docx";

        RequestParamAllBodyType requestParam = new RequestParamAllBodyType();
        requestParam.setUrl(fileUrl);
        requestParam.setHttpMethod("get");
//        Map<String, String> head = new HashMap<>();
//        head.put("Authorization", "Basic MDJhZGUxZDZmZWQyNGIzY2JiNGQ5Yjg5MzUxY2RlNTY6NWRlOTliMzkwMmQzNGIzNzhmNDI2OTIxZmVhMjNjYzA=");
//        requestParam.setHeader(head);
//        String body = "param1=value1&param2=value2";
//        requestParam.setBody(body);
        requestParam.setIsIgnoreCrt(false);
        String url = lcapHttpClient.exchangeAllBodyType(requestParam);
        System.out.println(url);
        requestParam.setIsIgnoreCrt(true);
//        url = lcapHttpClient.exchangeAllBodyType(requestParam);
//        System.out.println(url);
    }

//    @Test
    public void testHttpBody() throws URISyntaxException {
        RequestParam requestParam = new RequestParam();
        requestParam.setUrl("http://wowwwrk.netease.com/itsm/v1/system/login/local");
        requestParam.setHttpMethod("POST");
        Map<String, String> head = new HashMap<>();
//        head.put("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        requestParam.setHeader(head);
        Map<String, String> body = new HashMap<>();
        body.put("username", "airconditioner");
        body.put("password", "U2FsdGVkX19pvpydcUlekP9nbbI8wGcj9fbvVjvenSo=");
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
        NosUtil.nosConfig = new NosConfig();
        NosUtil.nosConfig.nosAccessKey = "e09bc35070b3406eb4179c332da67146";
        NosUtil.nosConfig.nosSecretKey = "624e6295a44d44f7880ae968b10d0892";
        NosUtil.nosConfig.nosAddress = "nos-eastchina1.126.net";
        NosUtil.nosConfig.nosBucket = "lcpapp-static";
        NosUtil.nosConfig.sinkType = "nos";
        String url = lcapHttpClient.downloadFileUploadNos(uri.toString(), null, header);
        System.out.println(url);
    }

//    @Test
    public void testDownloadAll() {
        Map<String, String> header = new HashMap<>();
        header.put("access-token", "a3dfd639eba440cfbfff58282012d225");
        NosUtil.nosConfig = new NosConfig();
        NosUtil.nosConfig.nosAccessKey = "e09bc35070b3406eb4179c332da67146";
        NosUtil.nosConfig.nosSecretKey = "624e6295a44d44f7880ae968b10d0892";
        NosUtil.nosConfig.nosAddress = "nos-eastchina1.126.net";
        NosUtil.nosConfig.nosBucket = "lcpapp-static";
        NosUtil.nosConfig.sinkType = "nos";
        String url = lcapHttpClient.downloadFileUploadNos("http://61.164.40.52:40056/dev-dept-kpi-api/sysportal/ztTask/exportTaskPerson.action?data={\"openedby\":\"\",\"assigneddate\":\"\",\"finisheddate\":\"\",\"closedby\":\"\",\"closeddate\":\"\",\"name\":\"\",\"statusList\":\"\",\"assignedto\":\"\",\"finishedby\":\"\",\"pdtId\":\"\",\"project\":\"\"}",
                null, header);
        System.out.println(url);
    }

    //    @Test
    public void testDownload2() {
        RequestParamAllBodyTypeInner requestParam = new RequestParamAllBodyTypeInner();
        requestParam.setUrl("https://dev-test-baohuahua.app.codewave.163.com/upload/app/1704163660141_20240105111142008.jpg");
        requestParam.setHttpMethod(HttpMethod.GET.name());
        File file = httpClientService.downloadFile(requestParam, restTemplate, null);
        File file1 = new File("a" + file.getName());
        file.renameTo(file1);
        System.out.println(file);
    }

    //    @Test
    public void testDownloadFileUploadNos() {
        RequestParam requestParam = new RequestParam();
        Map<String, String> head = new HashMap<>();
        requestParam.setHeader(head);
        Map<String, String> body = new HashMap<>();
        requestParam.setBody(body);
        requestParam.setHttpMethod("post");

        body.put("formatCheck", "false");
        head.put("X-Token", "ubF9PgJPWExFY5HG0SzMjjNwvy63bazgqv6mhXo8UvNo6aPYazjRGs5scQk-C2X3");
        requestParam.setUrl("https://uat.cdp.changan.com.cn/trading-desk/api/brief/upload/new");
        String res = lcapHttpClient.uploadNosExchange("/upload/app/中文测试_20240110144435430.jpg",
                "https://dev-test-baohuahua.app.codewave.163.com/test_upload", requestParam);
        System.out.println(res);
    }
}
