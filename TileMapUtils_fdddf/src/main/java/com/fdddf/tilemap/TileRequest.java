package com.fdddf.tilemap;

import com.netease.lowcode.core.annotation.NaslStructure;

import java.math.BigInteger;

@NaslStructure
public class TileRequest {

    /**
     * 瓦片图ID，Function传值
     */
    public Long id;
    /**
     * 瓦片图原始URL
     */
    public String url;

    /**
     * 瓦片图大小
     */
    public Integer tileSize = 256;

    /**
     * 瓦片图输出目录名称，不包含`/`
     */
    public String outputDirectory;

    @Override
    public String toString() {
        return "TileRequest{" +
                "id=" + id +
                ", url='" + url + '\'' +
                ", tileSize=" + tileSize +
                ", outputDirectory='" + outputDirectory + '\'' +
                '}';
    }
}
