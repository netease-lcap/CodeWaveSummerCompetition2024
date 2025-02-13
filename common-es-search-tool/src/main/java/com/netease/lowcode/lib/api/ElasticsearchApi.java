package com.netease.lowcode.lib.api;


import com.alibaba.fastjson.JSONObject;
import com.netease.lowcode.core.annotation.NaslLogic;
import com.netease.lowcode.lib.api.config.CommonEsSearchConfig;
import com.netease.lowcode.lib.api.config.SortTypeEnum;
import com.netease.lowcode.lib.api.dto.QueryItemsListDto;
import com.netease.lowcode.lib.api.dto.QueryRequestParam;
import com.netease.lowcode.lib.api.dto.QueryResultDto;
import com.netease.lowcode.lib.api.util.ElasticsearchUtil;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ElasticsearchApi {
    private static final Logger log = LoggerFactory.getLogger("LCAP_EXTENSION_LOGGER");
    @Autowired
    private CommonEsSearchConfig commonEsSearchConfig;

    @PostConstruct
    public void init() {
        ElasticsearchUtil.initClient(commonEsSearchConfig.esClientHost, commonEsSearchConfig.esClientPort, commonEsSearchConfig.esClientUsername, commonEsSearchConfig.esClientPassword);
    }

    /**
     * 补充默认参数
     *
     * @param queryRequestParam
     */
    private void fillDefaultParam(QueryRequestParam queryRequestParam) {
        if (StringUtils.isEmpty(queryRequestParam.pageIndex)) {
            queryRequestParam.pageIndex = 1;
        }
        if (StringUtils.isEmpty(queryRequestParam.pageSize)) {
            queryRequestParam.pageSize = 20;
        }
        if (StringUtils.isEmpty(queryRequestParam.index)) {
            throw new IllegalArgumentException("索引名称不能为空");
        }
        if (queryRequestParam.sortInfo != null) {
            if (StringUtils.isEmpty(queryRequestParam.sortInfo.sortType) || StringUtils.isEmpty(queryRequestParam.sortInfo.sortParam)) {
                queryRequestParam.sortInfo = null;
            }
        }
        if (queryRequestParam.logicalOperator == null) {
            queryRequestParam.logicalOperator = 1;
        }

        if (queryRequestParam.logicalOperator != 1 && queryRequestParam.logicalOperator != 2) {
            throw new IllegalArgumentException("逻辑运算符logicalOperator不合法");
        }
        if (queryRequestParam.scrollTime == null || queryRequestParam.scrollTime <= 0) {
            queryRequestParam.scrollTime = null;
        }
        if (!CollectionUtils.isEmpty(queryRequestParam.queryItems) && !CollectionUtils.isEmpty(queryRequestParam.queryItemList)) {
            throw new IllegalArgumentException("queryItemList和queryItems不能同时传入");
        }
        if (!CollectionUtils.isEmpty(queryRequestParam.queryItems)) {
            QueryItemsListDto queryItemsListDto = new QueryItemsListDto();
            queryItemsListDto.queryItems = queryRequestParam.queryItems;
            queryRequestParam.queryItemList = new ArrayList<>();
            queryRequestParam.queryItemList.add(queryItemsListDto);
            // 1表示queryItems内多个条件之间为与；2表示queryItems内多个条件之间为或。所以这边需要旋转一下。
            // fillQueryBuilder执行时，1是外与内或。不旋转的话会执行内或。
            queryRequestParam.logicalOperator = queryRequestParam.logicalOperator == 1 ? 2 : 1;
            queryRequestParam.queryItems = null;
        }
    }

    /**
     * 修改文档
     *
     * @param index        索引名称
     * @param queryFields  查询内容。key为字段名，value为字段值
     * @param updateFields 更新内容。key为字段名，value为字段值
     * @return
     */
    @NaslLogic
    public Boolean updateDocumentByFields(String index, Map<String, List<String>> queryFields, Map<String, String> updateFields) {
        try {
            log.info("index:{},queryFields:{},updateFields:{}", index, JSONObject.toJSONString(queryFields), JSONObject.toJSONString(updateFields));
            ElasticsearchUtil.updateByFields(index, queryFields, updateFields, commonEsSearchConfig.esClientHost + ":" + commonEsSearchConfig.esClientPort);
        } catch (Exception e) {
            log.error("更新异常", e);
            throw new IllegalArgumentException(e.getMessage());
        }
        return true;
    }

    /**
     * 滚动搜索
     *
     * @param scrollTime 滚动搜索有效时间段，不传即不使用滚动搜索
     * @param scrollId   滚动id，滚动搜索时非首次使用必填
     * @return
     * @throws IllegalArgumentException
     */
    @NaslLogic
    public QueryResultDto queryScroll(Integer scrollTime, String scrollId) throws IllegalArgumentException {
        log.info("scrollId:{}", scrollId);
        LocalDateTime now1 = LocalDateTime.now();
        String url = commonEsSearchConfig.esClientHost + ":" + commonEsSearchConfig.esClientPort;
        if (scrollTime != null) {
        }
        if (scrollId == null || scrollTime == null || scrollTime == 0) {
            throw new IllegalArgumentException("scrollId和scrollTime不能为空");
        }
        String scrollTimeStr = scrollTime + "m";
        QueryResultDto queryResultDto = ElasticsearchUtil.searchScroll(url, scrollTimeStr, scrollId);
        LocalDateTime now2 = LocalDateTime.now();
        log.info("查询耗时：{}", Duration.between(now1, now2).toMillis());
        return queryResultDto;
    }

    /**
     * 普通搜索
     *
     * @param queryRequestParam
     * @return
     * @throws IllegalArgumentException
     */
    @NaslLogic
    public QueryResultDto query(QueryRequestParam queryRequestParam) throws IllegalArgumentException {
        return queryCommon(queryRequestParam);
    }


    public QueryResultDto queryCommon(QueryRequestParam queryRequestParam) throws IllegalArgumentException {
        try {
            log.info("queryRequestParam:{}", JSONObject.toJSONString(queryRequestParam));
            LocalDateTime now1 = LocalDateTime.now();
            fillDefaultParam(queryRequestParam);
            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
            // 分页
            if (queryRequestParam.pageSize <= 0 || queryRequestParam.pageIndex <= 0) {
                throw new IllegalArgumentException("pageSize和pageIndex必须大于0");
            }
            sourceBuilder.from((queryRequestParam.pageIndex - 1) * queryRequestParam.pageSize); // 起始索引
            sourceBuilder.size(queryRequestParam.pageSize); // 每页大小
            // Include和exclude
            if (!StringUtils.isEmpty(queryRequestParam.includeFields) && !StringUtils.isEmpty(queryRequestParam.excludeFields)) {
                sourceBuilder.fetchSource(queryRequestParam.includeFields.toArray(new String[]{}), queryRequestParam.excludeFields.toArray(new String[]{}));
            } else if (!StringUtils.isEmpty(queryRequestParam.includeFields)) {
                sourceBuilder.fetchSource(queryRequestParam.includeFields.toArray(new String[]{}), null);
            } else if (!StringUtils.isEmpty(queryRequestParam.excludeFields)) {
                sourceBuilder.fetchSource(null, queryRequestParam.excludeFields.toArray(new String[]{}));
            }
            // 排序
            if (queryRequestParam.sortInfo != null) {
                SortOrder sortOrder = SortTypeEnum.getEnumByValue(queryRequestParam.sortInfo.sortType);
                if (sortOrder == null) {
                    throw new IllegalArgumentException("排序类型不合法");
                }
                sourceBuilder.sort(queryRequestParam.sortInfo.sortParam, sortOrder);
            }
            BoolQueryBuilder queryBuilder = new BoolQueryBuilder();
            // 查询条件 1与2或
            if (queryRequestParam.queryItemList != null) {
                queryBuilder = fillQueryBuilder(queryRequestParam);
            }
            String url = commonEsSearchConfig.esClientHost + ":" + commonEsSearchConfig.esClientPort;
            String scrollTime = null;
            if (queryRequestParam.scrollTime != null) {
                scrollTime = queryRequestParam.scrollTime + "m";
            }
            // 普通搜索
            QueryResultDto resultDto = ElasticsearchUtil.search(queryRequestParam.index, sourceBuilder, queryBuilder, queryRequestParam.pageIndex, queryRequestParam.pageSize, url, scrollTime);
            LocalDateTime now2 = LocalDateTime.now();
            log.info("查询耗时：{}", Duration.between(now1, now2).toMillis());
            return resultDto;
        } catch (Exception e) {
            log.error("查询异常", e);
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    private Map<Object, Object> handleQueryItemsDto(String queryValue) {
        Map<Object, Object> map = new HashMap<>();
        String[] queryValues = queryValue.split(",");
        if (queryValues.length != 1 && queryValues.length != 2) {
            throw new IllegalArgumentException("查询内容queryValue不合法：" + queryValue);
        }
        if (queryValues.length == 2) {
            if (StringUtils.isEmpty(queryValues[0])) {
                queryValues[0] = null;
            }
            map.put(queryValues[0], queryValues[1]);
        } else if (queryValue.contains(",")) {
            map.put(queryValues[0], null);
        } else {
            throw new IllegalArgumentException("查询内容queryValue不合法：" + queryValue);
        }
        return map;
    }

    private BoolQueryBuilder fillQueryBuilder(QueryRequestParam queryRequestParam) {
        //1与2或
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        if (queryRequestParam.logicalOperator == 1) {
            queryRequestParam.queryItemList.forEach(queryItemListDto -> {
                BoolQueryBuilder queryBuilderItem = QueryBuilders.boolQuery();
                queryItemListDto.getQueryItems().forEach(queryItemDto -> {
                    //查询类型，精确1 termQuery、模糊2 wildcardQuery、范围3 rangeQuery
                    if (queryItemDto.queryType == 1) {
                        queryBuilderItem.should(QueryBuilders.termQuery(queryItemDto.getParameter(), queryItemDto.getQueryValue()));
                    } else if (queryItemDto.queryType == 2) {
                        queryBuilderItem.should(QueryBuilders.wildcardQuery(queryItemDto.getParameter(), "*" + queryItemDto.getQueryValue() + "*"));
                    } else if (queryItemDto.queryType == 3) {
                        Map<Object, Object> mapQueryValue = handleQueryItemsDto(queryItemDto.getQueryValue());
                        mapQueryValue.forEach((key, value) -> {
                            if (key != null && value != null) {
                                queryBuilderItem.should(QueryBuilders.rangeQuery(queryItemDto.getParameter()).gte(key).lte(value));
                            } else if (key != null) {
                                queryBuilderItem.should(QueryBuilders.rangeQuery(queryItemDto.getParameter()).gte(key));
                            } else if (value != null) {
                                queryBuilderItem.should(QueryBuilders.rangeQuery(queryItemDto.getParameter()).lte(value));
                            }
                        });
                    } else if (queryItemDto.queryType == 4) {
                        queryBuilder.should(QueryBuilders.boolQuery().mustNot(QueryBuilders.termQuery(queryItemDto.getParameter(), queryItemDto.getQueryValue())));
                    }
                });
                queryBuilder.must(queryBuilderItem);
            });
        } else {
            queryRequestParam.queryItemList.forEach(queryItemListDto -> {
                BoolQueryBuilder queryBuilderItem = QueryBuilders.boolQuery();
                queryItemListDto.getQueryItems().forEach(queryItemDto -> {
                    //查询类型，精确1 termQuery、模糊2 wildcardQuery、范围3 rangeQuery
                    if (queryItemDto.queryType == 1) {
                        queryBuilderItem.must(QueryBuilders.termQuery(queryItemDto.getParameter(), queryItemDto.getQueryValue()));
                    } else if (queryItemDto.queryType == 2) {
                        queryBuilderItem.must(QueryBuilders.wildcardQuery(queryItemDto.getParameter(), "*" + queryItemDto.getQueryValue() + "*"));
                    } else if (queryItemDto.queryType == 3) {
                        Map<Object, Object> mapQueryValue = handleQueryItemsDto(queryItemDto.getQueryValue());
                        mapQueryValue.forEach((key, value) -> {
                            if (key != null && value != null) {
                                queryBuilderItem.must(QueryBuilders.rangeQuery(queryItemDto.getParameter()).gte(key).lte(value));
                            } else if (key != null) {
                                queryBuilderItem.must(QueryBuilders.rangeQuery(queryItemDto.getParameter()).gte(key));
                            } else if (value != null) {
                                queryBuilderItem.must(QueryBuilders.rangeQuery(queryItemDto.getParameter()).lte(value));
                            }
                        });
                    } else if (queryItemDto.queryType == 4) {
                        queryBuilder.mustNot(QueryBuilders.termQuery(queryItemDto.getParameter(), queryItemDto.getQueryValue()));
                    }
                });
                queryBuilder.should(queryBuilderItem);
            });
        }
        return queryBuilder;
    }
}
