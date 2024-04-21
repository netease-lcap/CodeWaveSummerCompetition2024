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