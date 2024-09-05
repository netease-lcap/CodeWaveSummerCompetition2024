package com.wjm.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.netease.lowcode.core.annotation.NaslConfiguration;
import com.netease.lowcode.core.annotation.NaslLogic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashSet;
import java.util.List;

/**
 * @program: region
 * @author: Mr.Wang
 * @create: 2024/04/20
 **/
@Component
public class RegionUtil {
  private static final Logger log = LoggerFactory.getLogger("LCAP_EXTENSION_LOGGER");
  /**
   * 自定义的行政区json文件
   */
  @NaslConfiguration
  @Value("${jsonFileUrl}")
  private String jsonFileUrl;

  private Region region;

  /**
   * 提供输入省份，给出其地级市列表
   *
   * @param provinceName 省份名
   * @return
   */
  @NaslLogic
  public List<Region> getPrefectureCity(String provinceName) {
    if (isEmpty(provinceName)) {
      throw new IllegalArgumentException("省份名不能为空");
    }
    List<Region> provinces = region.getDistricts();
    List<Region> prefectureCities = null;
    for (Region province : provinces) {
      if (province.getName().startsWith(provinceName)) {
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
  public List<Region> getCountry(String prefectureCity) {
    if (isEmpty(prefectureCity)) {
      throw new IllegalArgumentException("地级市名不能为空");
    }
    List<Region> counties = null;
    List<Region> provinces = region.getDistricts();
    for (Region province : provinces) {
      List<Region> prefectureCities = province.getDistricts();
      for (Region prefecture : prefectureCities) {
        if (prefecture.getName().startsWith(prefectureCity)) {
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
  public Boolean estimateProvinceAndCity(String provinceName, String prefectureCityName) {
    if (isEmpty(provinceName)) {
      throw new IllegalArgumentException("省份名不能为空");
    }
    if (isEmpty(prefectureCityName)) {
      throw new IllegalArgumentException("地级市名不能为空");
    }
    HashSet<String> cities = new HashSet<>();
    List<Region> provinces = region.getDistricts();
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
  public Boolean estimateCityAndCounty(String prefectureCityName, String countyName) {
    if (isEmpty(prefectureCityName)) {
      throw new IllegalArgumentException("地级市名不能为空");
    }
    if (isEmpty(countyName)) {
      throw new IllegalArgumentException("区县名不能为空");
    }
    HashSet<String> counties = new HashSet<>();
    List<Region> provinces = region.getDistricts();
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
  public Boolean estimateProvinceAndCounty(String provinceName, String countyName) {
    if (isEmpty(provinceName)) {
      throw new IllegalArgumentException("省份名不能为空");
    }
    if (isEmpty(countyName)) {
      throw new IllegalArgumentException("区县名不能为空");
    }
    HashSet<String> counties = new HashSet<>();
    List<Region> provinces = region.getDistricts();
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

  /**
   * 获取现有的行政区信息
   *
   * @return
   */
  @NaslLogic
  public Region getRegion() {
    return readResourceFile();
  }

  /**
   * 对中文字符进行UTF-8编码
   *
   * @param source 要转义的字符串
   * @return
   * @throws UnsupportedEncodingException
   */
  public static String tranformStyle(String source) throws UnsupportedEncodingException {
    char[] arr = source.toCharArray();
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < arr.length; i++) {
      char temp = arr[i];
      if (isChinese(temp)) {
        sb.append(URLEncoder.encode("" + temp, "UTF-8"));
        continue;
      }
      sb.append(arr[i]);
    }
    return sb.toString();
  }

  /**
   * 判断是不是中文字符
   *
   * @param c
   * @return
   */
  public static boolean isChinese(char c) {
    Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
    if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
            || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
            || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
            || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
            || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
            || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
      return true;
    }
    return false;
  }


  private Region readFile() {
    if (!isEmpty(this.jsonFileUrl)) {
      try {
        StringBuilder content = new StringBuilder();
        String encodedUrl = tranformStyle(jsonFileUrl);
        URL url = new URL(encodedUrl);
        URLConnection connection = url.openConnection();
        InputStream inputStream = connection.getInputStream();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String line;
        while ((line = bufferedReader.readLine()) != null) {
          content.append(line);
        }
        return JSON.parseObject(content.toString(), Region.class);
      } catch (IOException e) {
        log.error("获取json文件异常：", e);
        throw new RegionException(e);
      } catch (JSONException e) {
        log.error("自定义json文件格式异常：", e);
        throw new RegionException(e);
      }
    } else {
      return readResourceFile();
    }
  }

  private Region readResourceFile() {
    StringBuilder content = new StringBuilder();
    try (InputStream inputStream = RegionUtil.class.getClassLoader().getResourceAsStream("region.json");
         InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
         BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
      String line;
      while ((line = bufferedReader.readLine()) != null) {
        content.append(line).append("\n");
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return JSON.parseObject(content.toString(), Region.class);
  }

  @PostConstruct
  public void init() {
    region = readFile();
  }


  private static boolean isEmpty(String s) {
    return s == null || s.length() == 0;
  }
}