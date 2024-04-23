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


@SpringBootTest(classes = HttpSpringEnvironmentConfiguration.class)
@RunWith(SpringRunner.class)
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
    public void testHttp() {
        RequestParamAllBodyType requestParam = new RequestParamAllBodyType();
        requestParam.setUrl("http://www.baidu.com");
        requestParam.setHttpMethod("get");
        Map<String, String> head = new HashMap<>();
//        head.put("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        requestParam.setHeader(head);
        String body = "param1=value1&param2=value2";
        requestParam.setBody(body);
        requestParam.setIsIgnoreCrt(false);
        String url = lcapHttpClient.exchangeAllBodyType(requestParam);
        System.out.println(url);
        requestParam.setIsIgnoreCrt(true);
        url = lcapHttpClient.exchangeAllBodyType(requestParam);
        System.out.println(url);
    }
}
