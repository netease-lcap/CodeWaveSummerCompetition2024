package com.moocsk.lowcode.youdao.translate.util;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.moocsk.lowcode.youdao.translate.model.Translate;
import com.moocsk.lowcode.youdao.translate.model.TranslateResult;
import com.moocsk.lowcode.youdao.translate.model.TranslateResultSingle;

/**
 * 模型转换类
 */
public class ModelUtil {
    /**
     * 转换翻译结果数组
     * 
     * @param array 翻译结果数组
     * @return 翻译结果 List
     */
    public static List<Translate> getTranslateModels(JSONArray array) {
        List<Translate> list = new ArrayList<Translate>();
        for (int i = 0; i < array.size(); i++) {
            JSONObject obj = (JSONObject) array.get(i);
            Translate translate = new Translate();
            String query = obj.getString("query");
            translate.query = query;
            String translation = obj.getString("translation");
            translate.translation = translation;
            list.add(translate);
        }
        return list;
    }

    /**
     * 从默认翻译结果中获取单条翻译结果
     * 
     * @param translateResult 翻译结果对象
     * @return 单条翻译结果
     */
    public static TranslateResultSingle getSingleTranslate(TranslateResult translateResult) {
        TranslateResultSingle result = new TranslateResultSingle();
        result.errorCode = translateResult.errorCode;
        result.errorMsg = translateResult.errorMsg;
        result.from = translateResult.from;
        result.to = translateResult.to;
        if (translateResult.errorCode.equals("0")) {
            Translate translate = translateResult.translateResults.get(0);
            result.query = translate.query;
            result.translation = translate.translation;
        }
        return result;
    }
}
