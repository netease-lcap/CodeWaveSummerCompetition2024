package com.fdddf.tilemap;

public class Test {

    private static final int TILE_SIZE = 256; // 瓦片大小
    private static final String OUTPUT_DIRECTORY = "file:///Users/***/Downloads/tilemaps/maps2/";
    private static final String imagePath = "/Users/***/Downloads/9fada83605de7c6cc0ac2f9d751e33e3a8235227.jpeg";
    public static void main(String[] args)
    {
        TileConfig cfg = new TileConfig();
        cfg.ossBucketDomain = "***.oss-cn-shanghai.aliyuncs.com";
        cfg.ossEndpoint = "oss-cn-shanghai.aliyuncs.com";
        cfg.ossBucketName = "***";
        cfg.ossAccessKeyId = "***";
        cfg.ossAccessKeySecret = "***";
        ImageTiler imageTiler = new ImageTiler(cfg);
        TileResponse response = imageTiler.doTileImage(imagePath, TILE_SIZE, OUTPUT_DIRECTORY);
        System.out.println(response);

        TileResponse response2 = imageTiler.doTileImage(imagePath, TILE_SIZE, "map512");
        System.out.println(response2);
    }
}
