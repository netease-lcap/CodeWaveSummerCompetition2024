package com.fdddf.tilemap;

import com.netease.lowcode.core.annotation.NaslStructure;

import java.math.BigInteger;

@NaslStructure
public class TileResponse {

    /**
     * 瓦片id
     */
    public Long id;

    /**
     * 是否成功
     */
    public Boolean success;

    /**
     * 失败原因
     */
    public String failedReason;

    /**
     * 瓦片xyz url
     */
    public String url;

    /**
     * 瓦片缩放等级
     */
    public Long zoomLevel;

    /**
     * 瓦片大小
     */
    public Integer tileSize;

    /**
     * 原始图片宽
     */
    public Long originalImageWidth;

    /**
     * 原始图片高
     */
    public Long originalImageHeight;

    /**
     * 地图宽
     */
    public Long mapWidth;

    /**
     * 地图高
     */
    public Long mapHeight;

    @Override
    public String toString() {
        return "TileResponse{" +
                "id=" + id +
                ", success=" + success +
                ", failedReason='" + failedReason + '\'' +
                ", url='" + url + '\'' +
                ", zoomLevel=" + zoomLevel +
                ", tileSize=" + tileSize +
                ", originalImageWidth=" + originalImageWidth +
                ", originalImageHeight=" + originalImageHeight +
                ", mapWidth=" + mapWidth +
                ", mapHeight=" + mapHeight +
               '}';
    }
}
