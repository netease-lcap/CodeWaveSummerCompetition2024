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

    public static RestHighLevelClient initClient(String esClientHost, String esClientPort, String esClientUsername, String esClientPassword) {
        String url = esClientHost + ":" + esClientPort;
        RestHighLevelClient client = clientMap.get(url);
        if (client == null) {
            synchronized (RestHighLevelClientFactory.class) {
                logger.info("{}:{}", esClientHost, esClientPort);
                Integer esPort;
                try {
                    esPort = Integer.parseInt(esClientPort);
                } catch (Exception e) {
                    logger.error("", e);
                    return null;
                }

                // 获取ES Client
                RestClientBuilder clientBuilder = RestClient.builder(
                        new HttpHost(esClientHost, esPort, "http")
                );
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
                clientMap.put(url, client);}
        }
        return client;
    }
}
