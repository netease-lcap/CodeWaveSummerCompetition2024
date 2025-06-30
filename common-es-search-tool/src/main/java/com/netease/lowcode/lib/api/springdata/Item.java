package com.netease.lowcode.lib.api.springdata;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "item", type = "docs", shards = 1, replicas = 0)
public class Item {

    /**
     * @Description: @Id注解必须是springframework包下的
     * org.springframework.data.annotation.Id
     * @Author: https://blog.csdn.net/chen_2890
     */
    @Id
    private Long id;

    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String title; //标题

    @Field(type = FieldType.Keyword)
    private String category;// 分类

    @Field(type = FieldType.Keyword)
    private String brand; // 品牌

    @Field(type = FieldType.Double)
    private Double price; // 价格

    @Field(index = false, type = FieldType.Keyword)
    private String images; // 图片地址l;

    public Item() {
        super();
    }

    public Item(Long id, String title, String category, String brand, Double price, String images) {
        super();
        this.id = id;
        this.title = title;
        this.category = category;
        this.brand = brand;
        this.price = price;
        this.images = images;
    }

    @Override
    public String toString() {
        return "com.netease.lowcode.lib.api.springdata.Item [id=" + id + ", title=" + title + ", category=" + category + ", brand=" + brand + ", price="
                + price + ", images=" + images + "]";
    }
}