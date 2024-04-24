package com.wjm.api;

import com.alibaba.fastjson2.JSON;
import com.netease.lowcode.core.annotation.NaslLogic;

import java.io.*;
import java.util.HashSet;
import java.util.List;

/**
 * @program: region
 * @author: Mr.Wang
 * @create: 2024/04/20
 **/
public class RegionUtil {
    static String fileName = "regionCopy.json";
    static String path;

    static {
        String fileContent = readResourceFile("region.json");
        path = System.getProperty("user.dir") + File.separator + fileName;
        File file = new File(path);
        try {
            if (!file.exists()) {
                file.createNewFile();
                BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                writer.write(fileContent);
                writer.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
        String fileContent = readFile();
        Region regionObject = JSON.parseObject(fileContent, Region.class);
        List<Region> provinces = regionObject.getDistricts();
        List<Region> prefectureCities = null;
        for (Region province : provinces) {
            if (provinceName.equals(province.getName())) {
                prefectureCities = province.getDistricts();
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
        String fileContent = readFile();
        Region regionObject = JSON.parseObject(fileContent, Region.class);
        List<Region> counties = null;
        List<Region> provinces = regionObject.getDistricts();
        for (Region province : provinces) {
            List<Region> prefectureCities = province.getDistricts();
            for (Region prefecture : prefectureCities) {
                if (prefectureCity.equals(prefecture.getName())) {
                    counties = prefecture.getDistricts();
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
        HashSet<String> cities = new HashSet<>();
        String fileContent = readFile();
        Region regionObject = JSON.parseObject(fileContent, Region.class);
        List<Region> provinces = regionObject.getDistricts();
        List<Region> prefectureCities = null;
        for (Region province : provinces) {
            if (provinceName.equals(province.getName())) {
                prefectureCities = province.getDistricts();
            }
            if (prefectureCities != null) {
                for (Region prefectureCity : prefectureCities) {
                    String name = prefectureCity.getName();
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
        HashSet<String> counties = new HashSet<>();
        String fileContent = readFile();
        Region regionObject = JSON.parseObject(fileContent, Region.class);
        List<Region> provinces = regionObject.getDistricts();
        for (Region province : provinces) {
            List<Region> prefectureCities = province.getDistricts();
            for (Region prefecture : prefectureCities) {
                if (prefectureCityName.equals(prefecture.getName())) {
                    List<Region> arrays = prefecture.getDistricts();
                    if (arrays != null) {
                        for (Region county : arrays) {
                            String name = county.getName();
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
        HashSet<String> counties = new HashSet<>();
        String fileContent = readFile();
        Region regionObject = JSON.parseObject(fileContent, Region.class);
        List<Region> provinces = regionObject.getDistricts();
        for (Region province : provinces) {
            if (provinceName.equals(province.getName())) {
                List<Region> prefectureCities = province.getDistricts();
                for (Region prefecture : prefectureCities) {
                    List<Region> arrays = prefecture.getDistricts();
                    for (Region county : arrays) {
                        String name = county.getName();
                        counties.add(name);
                    }
                }
            }
        }
        return counties.contains(countyName);
    }

    private static String readFile() {
        StringBuilder content = new StringBuilder();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content.toString();
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


    /**
     * 获取行政区信息
     * @return
     */
    @NaslLogic
    public static Region getRegion() {
        String fileContent = readFile();
        return JSON.parseObject(fileContent, Region.class);
    }

    /**
     * 修改行政区信息
     * @param region
     * @return
     */
    @NaslLogic
    public static Boolean editRegion(Region region) {
        if (region == null){
            throw new IllegalArgumentException("传入参数不能为空");
        }
        String regionJson = JSON.toJSONString(region);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            writer.write(regionJson);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    private static Boolean isEmpty(String s) {
        return s == null || s.length() == 0;
    }

}