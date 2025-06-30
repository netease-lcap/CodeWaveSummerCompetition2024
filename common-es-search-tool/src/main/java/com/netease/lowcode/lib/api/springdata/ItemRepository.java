package com.netease.lowcode.lib.api.springdata;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ItemRepository extends ElasticsearchRepository<Item, Long> {
}
