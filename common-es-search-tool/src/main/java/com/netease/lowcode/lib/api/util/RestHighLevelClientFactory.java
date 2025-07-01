package com.netease.lowcode.lib.api.util;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;


public class RestHighLevelClientFactory {
    private static final Logger logger = LoggerFactory.getLogger("LCAP_EXTENSION_LOGGER");
    private static volatile Map<String, RestHighLevelClient> clientMap = new HashMap<>();

    public static RestHighLevelClient getClient(String url) {
        return clientMap.get(url);
    }

    public static RestHighLevelClient initClient(String esClientUris, String esClientUsername, String esClientPassword) {
        RestHighLevelClient client = clientMap.get(esClientUris);
        if (client == null) {
            synchronized (RestHighLevelClientFactory.class) {
                logger.info("es连接地址：{}", esClientUris);
                String[] urls = esClientUris.split(",");
                HttpHost[] hosts = new HttpHost[urls.length];
                for (int i = 0; i < urls.length; i++) {
                    String[] url = urls[i].split(":");
                    hosts[i] = new HttpHost(url[0], Integer.parseInt(url[1]), "http");
                }
                // 获取ES Client
                RestClientBuilder clientBuilder = RestClient.builder(hosts);
                if (esClientUsername != null && esClientPassword != null) {
                    // auth
                    CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
                    credentialsProvider.setCredentials(AuthScope.ANY,
                            new UsernamePasswordCredentials(esClientUsername, esClientPassword));
                    clientBuilder.setHttpClientConfigCallback(httpAsyncClientBuilder -> {
                        httpAsyncClientBuilder.disableAuthCaching();
                        return httpAsyncClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
                    });
                }
                client = new RestHighLevelClient(clientBuilder); // 你的实例化逻辑
                clientMap.put(esClientUris, client);
            }
        }
        return client;
    }
}
