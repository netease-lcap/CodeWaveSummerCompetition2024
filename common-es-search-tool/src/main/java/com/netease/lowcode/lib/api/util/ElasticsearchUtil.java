package com.netease.lowcode.lib.api.util;

import com.alibaba.fastjson.JSONObject;
import com.netease.lowcode.lib.api.dto.QueryResultDto;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchScrollRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.UpdateByQueryRequest;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptType;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ElasticsearchUtil {
    private static final Logger log = LoggerFactory.getLogger("LCAP_EXTENSION_LOGGER");


    public static void initClient(String esClientUris, String esClientUsername, String esClientPassword) {
        RestHighLevelClientFactory.initClient(esClientUris, esClientUsername, esClientPassword);
    }

    public static void updateByFields(String index, Map<String, List<String>> queryFields, Map<String, String> updateFields, String url) {
        try {
            UpdateByQueryRequest updateByQueryRequest = new UpdateByQueryRequest(index);
            // 构建查询条件
            queryFields.forEach((field, value) -> updateByQueryRequest.setQuery(QueryBuilders.termsQuery(field, value)));
            StringBuilder scriptBuilder = new StringBuilder("ctx._source.putAll(params);");
            Map<String, Object> params = new HashMap<>(updateFields);
            Script script = new Script(ScriptType.INLINE, "painless", scriptBuilder.toString(), params);
            updateByQueryRequest.setScript(script);
            RestHighLevelClient client = RestHighLevelClientFactory.getClient(url);
            client.updateByQuery(updateByQueryRequest, RequestOptions.DEFAULT);
        } catch (Exception e) {
            log.info("连接异常", e);
            throw new RuntimeException(e);
        }
    }

    public static QueryResultDto searchScroll(String url, String scrollTime, String scrollId) {
        QueryResultDto queryResultDto = null;
        try {
            // 创建一个搜索请求
            SearchScrollRequest searchScrollRequest = new SearchScrollRequest(scrollId);
            // 设置滚动时间
            searchScrollRequest.scroll(scrollTime);
            // 执行搜索请求
            SearchResponse searchResponse = RestHighLevelClientFactory.getClient(url).scroll(searchScrollRequest, RequestOptions.DEFAULT);
            SearchHit[] searchHits = searchResponse.getHits().getHits();
            log.info("result count:{}", searchHits.length);
            if (searchHits.length > 0) {
                queryResultDto = new QueryResultDto();
                queryResultDto.totalCount = searchResponse.getHits().getTotalHits();
                List<String> dataJsonString = new ArrayList<>();
                queryResultDto.dataJsonString = dataJsonString;
                for (SearchHit hit : searchHits) {
                    JSONObject jsonObject = JSONObject.parseObject(hit.getSourceAsString());
                    jsonObject.put("esId", hit.getId());
                    dataJsonString.add(JSONObject.toJSONString(jsonObject));
                }
                queryResultDto.scrollId = searchResponse.getScrollId();
            }
        } catch (Exception e) {
            log.info("连接异常", e);
            throw new RuntimeException(e);
        }
        return queryResultDto;
    }

    public static QueryResultDto search(String index, SearchSourceBuilder sourceBuilder, BoolQueryBuilder queryBuilder,
                                        Integer pageIndex, Integer pageSize, String url, String scrollTime) {
        QueryResultDto queryResultDto = null;
        try {
            // 创建一个搜索请求
            SearchRequest searchRequest = new SearchRequest();
            searchRequest.indices(index);
            if (scrollTime != null) {
                // 设置滚动时间
                searchRequest.scroll(scrollTime);
            }
            sourceBuilder.query(queryBuilder);
            searchRequest.source(sourceBuilder);
            log.info("sourceBuilder:{}", sourceBuilder);
            // 执行搜索请求
            SearchResponse searchResponse = RestHighLevelClientFactory.getClient(url).search(searchRequest, RequestOptions.DEFAULT);
            SearchHit[] searchHits = searchResponse.getHits().getHits();
            log.info("result count:{}", searchHits.length);
            if (searchHits.length > 0) {
                queryResultDto = new QueryResultDto();
                queryResultDto.totalCount = searchResponse.getHits().getTotalHits();
                queryResultDto.pageSize = pageSize;
                queryResultDto.pageIndex = pageIndex;
                queryResultDto.totalPage = (int) Math.ceil((double) queryResultDto.totalCount / pageSize);
                List<String> dataJsonString = new ArrayList<>();
                queryResultDto.dataJsonString = dataJsonString;
                for (SearchHit hit : searchHits) {
                    JSONObject jsonObject = JSONObject.parseObject(hit.getSourceAsString());
                    jsonObject.put("esId", hit.getId());
                    dataJsonString.add(JSONObject.toJSONString(jsonObject));
                }
                queryResultDto.scrollId = searchResponse.getScrollId();
            }
        } catch (Exception e) {
            log.info("连接异常", e);
            throw new RuntimeException(e);
        }
        return queryResultDto;
    }
}
