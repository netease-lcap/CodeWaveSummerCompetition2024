import com.netease.http.dto.RequestParamAllBodyTypeInner;
import com.netease.http.httpclient.HttpClientService;
import com.netease.http.httpclient.LCAPHttpClient;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 验证 exchangeInnerWithoutContentType：
 * 1. 未传 Content-Type 且有 body 时，请求原始报文不携带 Content-Type
 * 2. 显式传 Content-Type 时保留
 * 3. body 为空白字符串时按无 body 处理（无 Content-Length）
 * 对照组：老方法 exchangeInner 未传 Content-Type 时会被 RestTemplate 自动补上
 */
public class ExchangeWithoutContentTypeVerifyTest {

    private final HttpClientService httpClientService = new HttpClientService();
    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * 启动一次性本地服务，捕获请求原始报文（请求行 + 头 + body 字节）
     */
    private String captureRawRequest(String method, String body, Map<String, String> header,
                                     boolean useNewMethod) throws Exception {
        try (ServerSocket serverSocket = new ServerSocket(0)) {
            AtomicReference<String> raw = new AtomicReference<>();
            Thread server = new Thread(() -> {
                try (Socket socket = serverSocket.accept()) {
                    InputStream in = socket.getInputStream();
                    ByteArrayOutputStream buf = new ByteArrayOutputStream();
                    byte[] chunk = new byte[1024];
                    String s = "";
                    int headerEnd = -1;
                    // 读到请求头结束
                    while (headerEnd < 0) {
                        int n = in.read(chunk);
                        if (n < 0) {
                            break;
                        }
                        buf.write(chunk, 0, n);
                        s = new String(buf.toByteArray(), StandardCharsets.ISO_8859_1);
                        headerEnd = s.indexOf("\r\n\r\n");
                    }
                    // 按 Content-Length 继续 read body
                    if (headerEnd >= 0) {
                        Matcher m = Pattern.compile("(?i)Content-Length:\\s*(\\d+)")
                                .matcher(s.substring(0, headerEnd));
                        if (m.find()) {
                            int need = Integer.parseInt(m.group(1));
                            int have = buf.size() - (headerEnd + 4);
                            while (have < need) {
                                int n = in.read(chunk);
                                if (n < 0) {
                                    break;
                                }
                                buf.write(chunk, 0, n);
                                have += n;
                            }
                        }
                    }
                    raw.set(new String(buf.toByteArray(), StandardCharsets.ISO_8859_1));
                    OutputStream out = socket.getOutputStream();
                    out.write("HTTP/1.1 200 OK\r\nContent-Length: 2\r\nConnection: close\r\n\r\nok"
                            .getBytes(StandardCharsets.ISO_8859_1));
                    out.flush();
                } catch (Exception ignored) {
                }
            });
            server.setDaemon(true);
            server.start();

            RequestParamAllBodyTypeInner requestParam = new RequestParamAllBodyTypeInner();
            requestParam.setUrl("http://127.0.0.1:" + serverSocket.getLocalPort() + "/verify");
            requestParam.setHttpMethod(method);
            requestParam.setHeader(header);
            requestParam.setBody(body);
            if (useNewMethod) {
                httpClientService.exchangeInnerWithoutContentType(requestParam, restTemplate);
            } else {
                httpClientService.exchangeInner(requestParam, restTemplate, String.class);
            }
            server.join(10000);
            return raw.get();
        }
    }

    private boolean containsIgnoreCase(String raw, String s) {
        return raw != null && raw.toLowerCase().contains(s.toLowerCase());
    }

    /**
     * 对照组：老方法 exchange() 没传 Content-Type 时 RestTemplate 自动补（证明差异真实存在）
     */
    @Test
    public void oldMethodAutoAddsContentType() throws Exception {
        String raw = captureRawRequest("POST", "hello", new HashMap<>(), false);
        System.out.println("=== 老方法原始报文 ===\n" + raw);
        Assert.assertTrue(containsIgnoreCase(raw, "Content-Type:"));
    }

    /**
     * 对照组：老方法 exchange() 通过 LCAPHttpClient 调用，Map body 自动补 Content-Type
     */
    @Test
    public void oldExchangeAutoAddsContentType() throws Exception {
        LCAPHttpClient client = new LCAPHttpClient();
        ReflectionTestUtils.setField(client, "httpClientService", httpClientService);
        ReflectionTestUtils.setField(client, "restTemplate", restTemplate);

        try (ServerSocket ss = new ServerSocket(0)) {
            AtomicReference<String> raw = new AtomicReference<>();
            Thread server = new Thread(() -> {
                try (Socket socket = ss.accept()) {
                    InputStream in = socket.getInputStream();
                    ByteArrayOutputStream buf = new ByteArrayOutputStream();
                    byte[] chunk = new byte[1024];
                    String s = "";
                    int headerEnd = -1;
                    while (headerEnd < 0) {
                        int n = in.read(chunk);
                        if (n < 0) break;
                        buf.write(chunk, 0, n);
                        s = new String(buf.toByteArray(), StandardCharsets.ISO_8859_1);
                        headerEnd = s.indexOf("\r\n\r\n");
                    }
                    if (headerEnd >= 0) {
                        Matcher m = Pattern.compile("(?i)Content-Length:\\s*(\\d+)")
                                .matcher(s.substring(0, headerEnd));
                        if (m.find()) {
                            int need = Integer.parseInt(m.group(1));
                            int have = buf.size() - (headerEnd + 4);
                            while (have < need) {
                                int n = in.read(chunk);
                                if (n < 0) break;
                                buf.write(chunk, 0, n);
                                have += n;
                            }
                        }
                    }
                    raw.set(new String(buf.toByteArray(), StandardCharsets.ISO_8859_1));
                    socket.getOutputStream().write(
                            "HTTP/1.1 200 OK\r\nContent-Length: 2\r\nConnection: close\r\n\r\nok"
                                    .getBytes(StandardCharsets.ISO_8859_1));
                    socket.getOutputStream().flush();
                } catch (Exception ignored) {
                }
            });
            server.setDaemon(true);
            server.start();

            Map<String, String> header = new HashMap<>();
            Map<String, String> body = new HashMap<>();
            body.put("name", "111");
            client.exchange("http://54.255.226.73:8080/api/request/content-type", "get", null, body);
            server.join(10000);

            String r = raw.get();
            System.out.println("=== 老方法 exchange() 原始报文 ===\n" + r);
            Assert.assertNotNull(r);
            Assert.assertTrue(containsIgnoreCase(r, "Content-Type:"));
            Assert.assertTrue(r.contains("name"));
        }
    }

    /**
     * 新方法：有 body、没传 Content-Type → 不带 Content-Type，带 Content-Length 和 body
     */
    @Test
    public void newMethodNoContentType() throws Exception {
        String raw = captureRawRequest("POST", "hello", new HashMap<>(), true);
        System.out.println("=== 新方法原始报文 ===\n" + raw);
        Assert.assertNotNull(raw);
        Assert.assertFalse(containsIgnoreCase(raw, "Content-Type:"));
        Assert.assertTrue(containsIgnoreCase(raw, "Content-Length: 5"));
        Assert.assertTrue(raw.contains("hello"));
    }

    /**
     * 新方法：显式传 Content-Type → 保留
     */
    @Test
    public void newMethodKeepExplicitContentType() throws Exception {
        Map<String, String> header = new HashMap<>();
        header.put("Content-Type", "application/json");
        String raw = captureRawRequest("POST", "{\"a\":1}", header, true);
        System.out.println("=== 显式Content-Type原始报文 ===\n" + raw);
        Assert.assertTrue(containsIgnoreCase(raw, "Content-Type: application/json"));
    }

    /**
     * 新方法：GET + 空白 body → 无 Content-Length、无 Content-Type、无 body
     */
    @Test
    public void newMethodBlankBodyGet() throws Exception {
        String raw = captureRawRequest("GET", "  ", new HashMap<>(), true);
        System.out.println("=== 空body GET原始报文 ===\n" + raw);
        Assert.assertNotNull(raw);
        Assert.assertFalse(containsIgnoreCase(raw, "Content-Type:"));
        Assert.assertFalse(containsIgnoreCase(raw, "Content-Length:"));
        Assert.assertFalse(raw.contains("  "));
    }

    /**
     * 新方法：POST + 空 body → 无 Content-Type、无 Content-Length（JDK 对 POST 会强补 form-urlencoded）
     */
    @Test
    public void newMethodBlankBodyPost() throws Exception {
        String raw = captureRawRequest("POST", "", new HashMap<>(), true);
        System.out.println("=== 空body POST原始报文 ===\n" + raw);
        Assert.assertNotNull(raw);
        Assert.assertTrue(raw.startsWith("POST /verify HTTP/1.1"));
        Assert.assertFalse(containsIgnoreCase(raw, "Content-Type:"));
        Assert.assertFalse(containsIgnoreCase(raw, "Content-Length:"));
    }

    /**
     * 新方法：GET + 真实 body → 带 Content-Length 和 body，不带 Content-Type（Elasticsearch 等场景）
     */
    @Test
    public void newMethodGetWithBody() throws Exception {
        String raw = captureRawRequest("GET", "hello", new HashMap<>(), true);
        System.out.println("=== GET带body原始报文 ===\n" + raw);
        Assert.assertNotNull(raw);
        Assert.assertTrue(raw.startsWith("GET /verify HTTP/1.1"));
        Assert.assertFalse(containsIgnoreCase(raw, "Content-Type:"));
        Assert.assertTrue(containsIgnoreCase(raw, "Content-Length: 5"));
        Assert.assertTrue(raw.contains("hello"));
    }

    /**
     * 实际场景：GET + JSON body + 不传 Content-Type → 服务端能收到 body
     * 模拟：exchangeWithoutContentType("http://54.255.226.73:8080/api/request/content-type", "GET", {}, {"name":"111"})
     */
    @Test
    public void exchangeWithoutContentTypeGetWithJsonBody() throws Exception {
        String body = "{\"name\":\"111\"}";
        String raw = captureRawRequest("GET", body, new HashMap<>(), true);
        System.out.println("=== 实际场景：GET+JSON body+无CT ===\n" + raw);
        Assert.assertNotNull(raw);
        Assert.assertTrue(raw.startsWith("GET /verify HTTP/1.1"));
        // 关键断言：无 Content-Type
        Assert.assertFalse("不应有 Content-Type", containsIgnoreCase(raw, "Content-Type:"));
        // 有 Content-Length（body 长度 14）
        Assert.assertTrue("应有 Content-Length: 14", containsIgnoreCase(raw, "Content-Length: 14"));
        // body 原样发送
        Assert.assertTrue("body 应包含 {\"name\":\"111\"}", raw.contains(body));
    }
}
