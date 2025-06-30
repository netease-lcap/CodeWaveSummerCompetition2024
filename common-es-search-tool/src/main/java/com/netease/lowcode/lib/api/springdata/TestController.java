package com.netease.lowcode.lib.api.springdata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test1")
public class TestController {

    private ElasticsearchOperations elasticsearchOperations;
    @Autowired
    private ItemRepository itemRepository;

    public TestController(ElasticsearchOperations elasticsearchOperations) {
        this.elasticsearchOperations = elasticsearchOperations;
    }

    @PostMapping("/insert")
    public void insert(Item item) {
//		com.netease.lowcode.lib.api.springdata.Item item = new com.netease.lowcode.lib.api.springdata.Item(1L, "小米手机7", " 手机", "小米", 3499.00, "http://image.baidu.com/13123.jpg");
//        Item item = new Item(1L, "苹果XSMax", " 手机", "小米", 3499.00, "http://image.baidu.com/13123.jpg");
        itemRepository.save(item);
    }

}

