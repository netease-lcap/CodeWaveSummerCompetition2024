package com.fdddf.tilemap;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.netease.lowcode.core.annotation.NaslLogic;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.awt.*;
import java.net.URL;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;

@Component
public class ImageTiler {

    @Resource
    private TileConfig cfg;
    private static final Logger logger = LoggerFactory.getLogger(ImageTiler.class);

    private final ExecutorService executor = Executors.newFixedThreadPool(10);

    private static final int THREAD_COUNT = 50; // 设定线程数量

    private static final int MIN_ZOOM_LEVEL = 3;

    private AmazonOSS oss;

    public ImageTiler(TileConfig cfg) {
        this.cfg = cfg;
        this.setupOSS();
    }

    private void setupOSS() {
        this.oss = new AmazonOSS(cfg.ossAccessKeyId, cfg.ossAccessKeySecret,
                cfg.ossEndpoint, cfg.ossBucketName, cfg.ossBucketDomain);
    }

    /**
     * 瓦片图片切图
     * @param request TileRequest
     * @param callback Function<TileResponse,Boolean>
     */
    @NaslLogic
    public Boolean tileImage(TileRequest request, Function<TileResponse, Boolean> callback) {
        executor.submit(() -> {
            TileResponse resp = new TileResponse();
            try {
                resp = doTileImage(request.url, request.tileSize, request.outputDirectory);
                resp.success = true;
            } catch (Exception e) {
                resp.success = false;
                resp.failedReason = e.getMessage();
                e.printStackTrace();
                logger.error("tileImage error: "+ Arrays.toString(e.getStackTrace()), e);
            }
            resp.id = request.id;
            callback.apply(resp);
        });
        return true;
    }

    /**
     * 检查图片信息是否可以切图
     * @param request TileRequest
     * @return true or false
     */
    @NaslLogic
    public Boolean validate(TileRequest request) {
        if (request.url == null || request.url.isEmpty()) {
            throw new TileRuntimeException("url is empty");
        }
        if (request.tileSize == null || request.tileSize <= 0) {
            throw new TileRuntimeException("tileSize is invalid，suggest 256");
        }
        if (request.outputDirectory == null || request.outputDirectory.isEmpty()) {
            throw new TileRuntimeException("outputDirectory is empty");
        }

        if (!(request.outputDirectory.startsWith("file://") || !request.outputDirectory.contains("/"))) {
            throw new TileRuntimeException("outputDirectory is invalid, " +
                    "it should starts with file:// or just a folder name for OSS usage");
        }

        try {
            BufferedImage mapImage;
            if (request.url.startsWith("http")) {
                mapImage = ImageIO.read(new URL(request.url));
            } else {
                mapImage = ImageIO.read(new File(request.url));
            }
            int width = mapImage.getWidth();
            int height = mapImage.getHeight();

            int minSquareSize = (int) Math.pow(2, MIN_ZOOM_LEVEL) * request.tileSize;
            if (Math.min(width, height) < minSquareSize) {
                throw new TileRuntimeException("image is too small, suggest image size is " + minSquareSize + "x" + minSquareSize);
            }
            return true;

        } catch (IOException e) {
            throw new TileRuntimeException("url is invalid");
        }
    }

    /**
     * 瓦片地图切图
     *
     * @param url             图片地址 http开头或本地地址
     * @param tileSize        瓦片大小
     * @param outputDirectory 输出目录 /开头或file://开头
     * @return TileResponse   瓦片地图信息
     * @throws TileRuntimeException 异常
     */
    public TileResponse doTileImage(String url, Integer tileSize, String outputDirectory) throws TileRuntimeException {
        if (this.oss == null) {
            this.setupOSS();
        }
        TileResponse response = new TileResponse();
        response.tileSize = tileSize;
        try {
            BufferedImage mapImage;
            if (url.startsWith("http")) {
                mapImage = ImageIO.read(new URL(url));
            } else {
                mapImage = ImageIO.read(new File(url));
            }

            int width = mapImage.getWidth();
            int height = mapImage.getHeight();
            int squareSize = Math.max(width, height);
            response.originalImageWidth = (long) width;
            response.originalImageHeight = (long) height;

            System.out.printf("width: %d, height: %d\n", width, height);
            int zoomLevel = (int) Math.ceil(Math.log(squareSize / (double) tileSize) / Math.log(2));
            response.zoomLevel = (long) zoomLevel;

            System.out.printf("zoomLevel: %d\n", zoomLevel);
            int newSquareSize = (int) Math.pow(2, zoomLevel) * tileSize;
            response.mapWidth = (long) newSquareSize;
            response.mapHeight = (long) newSquareSize;

            Image newImage = mapImage;
            // scale the image to the new size
            if (squareSize != newSquareSize) {
                double ratio = (double)newSquareSize / squareSize;
                newImage = mapImage.getScaledInstance((int) (width * ratio), (int) (height * ratio), Image.SCALE_SMOOTH);
            }

            BufferedImage squareImage = new BufferedImage(newSquareSize, newSquareSize, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = squareImage.createGraphics();
            g2d.setColor(new Color(255, 255, 255));
            g2d.fillRect(0, 0, newSquareSize, newSquareSize);
            g2d.drawImage(newImage, 0, 0, null);
            g2d.dispose();

            ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);

            for (int zoom = 0; zoom <= zoomLevel; zoom++) {
                int rows, cols;
                rows = cols = (int) Math.pow(2, zoom);

                int currentTileSize = (newSquareSize / rows);

                for (int row = 0; row < rows; row++) {
                    for (int col = 0; col < cols; col++) {
                        int x = row * currentTileSize;
                        int y = col * currentTileSize;

                        BufferedImage tile = squareImage.getSubimage(x, y, currentTileSize, currentTileSize);
                        final int finalZoom = zoom;
                        final int finalRow = row;
                        final int finalCol = col;

                        executor.submit(() -> {
                            String ossPath = outputDirectory + "/" + finalZoom + "/" + finalRow + "/" + finalCol + ".png";
                            saveTileImage(ossPath, tile);
                        });
                    }
                }
            }

            executor.shutdown();
            while (!executor.isTerminated()) {
                // 等待所有任务完成
//                System.out.println("Waiting for all tasks to complete...");
            }

            if (outputDirectory.startsWith("file://")) {
                String[] dirs = FilenameUtils.getPath(outputDirectory).split("/");
                response.url = oss.getBucketDomain() + "/" + dirs[dirs.length - 1] + "/{z}/{x}/{y}.png";
            } else {
                response.url = oss.getBucketDomain() + "/" + outputDirectory + "/{z}/{x}/{y}.png";
            }

            System.out.println("Tiles generated completed.");

            return response;
        } catch (IOException e) {
            e.printStackTrace();
            throw new TileRuntimeException("Failed to generate tiles." + Arrays.toString(e.getStackTrace()), e);
        }
    }

    private void saveTileImage(String ossPath, BufferedImage tile) {
        try {
            // Check the output directory if it's local file system
            if (ossPath.startsWith("file://")) {
                String tmpfile = ossPath.substring(7);
                File output = new File(tmpfile);
                output.getParentFile().mkdirs();
                ImageIO.write(tile, "png", output);
            } else {
                ByteArrayOutputStream outstream = new ByteArrayOutputStream();
                ImageIO.write(tile, "png", outstream);
                byte[] buffer = outstream.toByteArray();
                InputStream is = new ByteArrayInputStream(buffer);
                // Upload the tile to OSS
                ObjectMetadata meta = new ObjectMetadata();
                meta.setContentType("image/png");
                meta.setContentLength(buffer.length);
                oss.putFile(ossPath, is, meta);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Failed to write tile: " + e.getMessage());
            throw new TileRuntimeException("Failed to save tile map.", e);
        }
    }

}
