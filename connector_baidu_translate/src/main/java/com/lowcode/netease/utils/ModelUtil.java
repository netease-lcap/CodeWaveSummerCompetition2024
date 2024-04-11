package com.lowcode.netease.utils;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.lowcode.netease.model.Translate;

/**
 * 模型转换类
 */
public class ModelUtil {

    /**
     * 转换翻译结果数组
     * @param array 翻译结果数组
     */
    public static List<Translate> getTranslateModels(JSONArray array) {
        List<Translate> list = new ArrayList<Translate>();
        for (int i = 0; i < array.size(); i++) {
            JSONObject obj = (JSONObject) array.get(i);
            Translate translate = new Translate();
            String src = obj.getString("src");
            translate.src = src;
            String dst = obj.getString("dst");
            translate.dst = dst;
            list.add(translate);
        }
        return list;
    }
    
}
