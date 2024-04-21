package com.wjm.api;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.netease.lowcode.core.annotation.NaslLogic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.List;

/**
 * @program: region
 * @author: Mr.Wang
 * @create: 2024/04/20
 **/
public class RegionUtil {

    /**
     * 提供输入省份，给出其地级市列表
     *
     * @param provinceName 省份名
     * @return
     */
    @NaslLogic
    public static List<Region> getPrefectureCity(String provinceName) {
        if (isEmpty(provinceName)) {
            throw new IllegalArgumentException("省份名不能为空");
        }
        String fileName = "region.json";
        String fileContent = readResourceFile(fileName);
        JSONObject regionObject = JSON.parseObject(fileContent);
        JSONArray provinces = regionObject.getJSONArray("districts");
        List<Region> prefectureCities = null;
        for (Object province : provinces) {
            if (provinceName.equals(((JSONObject) province).getString("name"))) {
                prefectureCities = ((JSONObject) province).getJSONArray("districts").toJavaList(Region.class);
            }
        }
        return prefectureCities;
    }


    /**
     * 提供输入地级市，给出其区县列表
     *
     * @param prefectureCity 地级市名
     * @return
     */
    @NaslLogic
    public static List<Region> getCountry(String prefectureCity) {
        if (isEmpty(prefectureCity)) {
            throw new IllegalArgumentException("地级市名不能为空");
        }
        String fileName = "region.json";
        String fileContent = readResourceFile(fileName);
        JSONObject regionObject = JSON.parseObject(fileContent);
        List<Region> counties = null;
        JSONArray provinces = regionObject.getJSONArray("districts");
        for (Object province : provinces) {
            JSONArray prefectureCities = ((JSONObject) province).getJSONArray("districts");
            for (Object prefecture : prefectureCities) {
                if (prefectureCity.equals(((JSONObject) prefecture).getString("name"))) {
                    counties = ((JSONObject) prefecture).getJSONArray("districts").toJavaList(Region.class);
                }
            }
        }
        return counties;
    }


    /**
     * 提供输入省份和地级市,判断两者是否有所属关系
     *
     * @param provinceName       省份名
     * @param prefectureCityName 地级市名
     * @return
     */
    @NaslLogic
    public static Boolean estimateProvinceAndCity(String provinceName, String prefectureCityName) {
        if (isEmpty(provinceName)) {
            throw new IllegalArgumentException("省份名不能为空");
        }
        if (isEmpty(prefectureCityName)) {
            throw new IllegalArgumentException("地级市名不能为空");
        }
        String fileName = "region.json";
        HashSet<String> cities = new HashSet<>();
        String fileContent = readResourceFile(fileName);
        JSONObject regionObject = JSON.parseObject(fileContent);
        JSONArray provinces = regionObject.getJSONArray("districts");
        JSONArray prefectureCities = null;
        for (Object province : provinces) {
            if (provinceName.equals(((JSONObject) province).getString("name"))) {
                prefectureCities = ((JSONObject) province).getJSONArray("districts");
            }
            if (prefectureCities != null) {
                for (Object prefectureCity : prefectureCities) {
                    String name = ((JSONObject) prefectureCity).getString("name");
                    cities.add(name);
                }
            }
        }
        return cities.contains(prefectureCityName);
    }

    /**
     * 提供输入地级市和区县，判断两者是否有所属关系
     *
     * @param prefectureCityName 地级市名
     * @param countyName         区县名
     * @return
     */
    @NaslLogic
    public static Boolean estimateCityAndCounty(String prefectureCityName, String countyName) {
        if (isEmpty(prefectureCityName)) {
            throw new IllegalArgumentException("地级市名不能为空");
        }
        if (isEmpty(countyName)) {
            throw new IllegalArgumentException("区县名不能为空");
        }
        String fileName = "region.json";
        HashSet<String> counties = new HashSet<>();
        String fileContent = readResourceFile(fileName);
        JSONObject regionObject = JSON.parseObject(fileContent);
        JSONArray provinces = regionObject.getJSONArray("districts");
        for (Object province : provinces) {
            JSONArray prefectureCities = ((JSONObject) province).getJSONArray("districts");
            for (Object prefecture : prefectureCities) {
                if (prefectureCityName.equals(((JSONObject) prefecture).getString("name"))) {
                    JSONArray arrays = ((JSONObject) prefecture).getJSONArray("districts");
                    if (arrays != null) {
                        for (Object county : arrays) {
                            String name = ((JSONObject) county).getString("name");
                            counties.add(name);
                        }
                    }
                }
            }
        }
        return counties.contains(countyName);
    }

    /**
     * 提供输入省份和区县，判断两者是否有所属关系
     *
     * @param provinceName 省份名
     * @param countyName   区县名
     * @return
     */
    @NaslLogic
    public static Boolean estimateProvinceAndCounty(String provinceName, String countyName) {
        if (isEmpty(provinceName)) {
            throw new IllegalArgumentException("省份名不能为空");
        }
        if (isEmpty(countyName)) {
            throw new IllegalArgumentException("区县名不能为空");
        }
        String fileName = "region.json";
        HashSet<String> counties = new HashSet<>();
        String fileContent = readResourceFile(fileName);
        JSONObject regionObject = JSON.parseObject(fileContent);
        JSONArray provinces = regionObject.getJSONArray("districts");
        for (Object province : provinces) {
            if (provinceName.equals(((JSONObject) province).getString("name"))) {
                JSONArray prefectureCities = ((JSONObject) province).getJSONArray("districts");
                for (Object prefecture : prefectureCities) {
                    JSONArray arrays = ((JSONObject) prefecture).getJSONArray("districts");
                    for (Object county : arrays) {
                        String name = ((JSONObject) county).getString("name");
                        counties.add(name);
                    }
                }
            }
        }
        return counties.contains(countyName);
    }

    private static String readResourceFile(String fileName) {
        StringBuilder content = new StringBuilder();
        try (InputStream inputStream = RegionUtil.class.getClassLoader().getResourceAsStream(fileName);
             InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content.toString();
    }

    private static Boolean isEmpty(String s) {
        return s == null || s.length() == 0;
    }

}