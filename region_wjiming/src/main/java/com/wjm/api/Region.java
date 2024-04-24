package com.wjm.api;

/**
 * @program: region
 * @author: Mr.Wang
 * @create: 2024/04/21
 **/

import com.netease.lowcode.core.annotation.NaslStructure;

import java.util.List;

@NaslStructure
public class Region {
    public String citycode;
    public String adcode;
    public String name;
    public String center;
    public String level;
    public String zipCode;
    public List<Region> districts;

    public String getCitycode() {
        return citycode;
    }

    public void setCitycode(String citycode) {
        this.citycode = citycode;
    }

    public String getAdcode() {
        return adcode;
    }

    public void setAdcode(String adcode) {
        this.adcode = adcode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCenter() {
        return center;
    }

    public void setCenter(String center) {
        this.center = center;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public List<Region> getDistricts() {
        return districts;
    }

    public void setDistricts(List<Region> districts) {
        this.districts = districts;
    }

    @Override
    public String toString() {
        return "Region{" +
                "citycode='" + citycode + '\'' +
                ", adcode='" + adcode + '\'' +
                ", name='" + name + '\'' +
                ", center='" + center + '\'' +
                ", level='" + level + '\'' +
                ", zipCode='" + zipCode + '\'' +
                ", districts=" + districts +
                '}';
    }
}