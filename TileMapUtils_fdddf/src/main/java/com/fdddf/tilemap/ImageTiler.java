package com.fdddf.tilemap;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.netease.lowcode.core.annotation.NaslLogic;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
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

    private static final float quality = 0.75f;

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
            logger.info("url: {}, tileSize: {}, outputDirectory: {}", url, tileSize, outputDirectory);
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
            logger.info("width: {}, height: {}", width, height);
            int zoomLevel = (int) Math.ceil(Math.log(squareSize / (double) tileSize) / Math.log(2));
            response.zoomLevel = (long) zoomLevel;

            System.out.printf("zoomLevel: %d\n", zoomLevel);
            logger.info("zoomLevel: {}", zoomLevel);
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

            logger.info("begin to split image in for loop with zoom {}", zoomLevel);
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

                        // sale the tile as it's too big
                        if (currentTileSize > tileSize) {
                            int resizeSize = tileSize;
                            if (zoom <= MIN_ZOOM_LEVEL) {
                                resizeSize = (int) Math.pow(2, MIN_ZOOM_LEVEL) * tileSize;
                            }
                            tile = resizeImage(tile, resizeSize, resizeSize);
                        }

                        BufferedImage finalTile = tile;
                        executor.submit(() -> {
                            String ossPath = outputDirectory + "/" + finalZoom + "/" + finalRow + "/" + finalCol + ".jpeg";
                            logger.info("begin to upload image to oss: {}", ossPath);
                            try {
                                byte[] compressedImageBuffer = compressImageToByteArray(finalTile);
                                handleCompressedImage(compressedImageBuffer, ossPath);
                            } catch (IOException e) {
                                System.out.println("Error occurred while processing tile: " + ossPath);
                                logger.error("Error occurred while processing tile: " + ossPath, e);
                                throw new RuntimeException(e);
                            }
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
                response.url = oss.getBucketDomain() + "/" + dirs[dirs.length - 1] + "/{z}/{x}/{y}.jpeg";
            } else {
                response.url = oss.getBucketDomain() + "/" + outputDirectory + "/{z}/{x}/{y}.jpeg";
            }

            System.out.println("Tiles generated completed.");
            logger.info("Tiles generated completed.");

            return response;
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("Failed to generate tiles." + Arrays.toString(e.getStackTrace()));
            throw new TileRuntimeException("Failed to generate tiles." + Arrays.toString(e.getStackTrace()), e);
        }
    }

    private BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
        Image scaledImage = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);
        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = resizedImage.createGraphics();
        g2d.drawImage(scaledImage, 0, 0, null);
        g2d.dispose();
        return resizedImage;
    }

    public byte[] compressImageToByteArray(BufferedImage image) throws IOException {
        ByteArrayOutputStream outstream = new ByteArrayOutputStream();

        // Get an ImageWriter for JPEG format
        ImageWriter writer = ImageIO.getImageWritersByFormatName("jpg").next();

        // Create an ImageOutputStream from the ByteArrayOutputStream
        try (ImageOutputStream ios = ImageIO.createImageOutputStream(outstream)) {
            writer.setOutput(ios);

            // Set the compression quality
            ImageWriteParam param = writer.getDefaultWriteParam();
            if (param.canWriteCompressed()) {
                param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                param.setCompressionQuality(quality);  // Quality between 0 and 1
            }

            // Write the image to the ByteArrayOutputStream
            writer.write(null, new IIOImage(image, null, null), param);
        } finally {
            // Dispose the writer to free up resources
            writer.dispose();
        }

        // Get the byte array from the ByteArrayOutputStream
        return outstream.toByteArray();
    }

    private void handleCompressedImage(byte[] buffer, String ossPath) throws IOException {
        // Check the output directory if it's local file system
        if (ossPath.startsWith("file://")) {
            String tmpfile = ossPath.substring(7);
            File output = new File(tmpfile);
            output.getParentFile().mkdirs();

            try (InputStream is = new ByteArrayInputStream(buffer)) {
                BufferedImage compressedImage = ImageIO.read(is);
                ImageIO.write(compressedImage, "jpeg", output);
            }
        } else {
            // Create an InputStream from the byte array
            InputStream is = new ByteArrayInputStream(buffer);

            // Upload the tile to OSS
            ObjectMetadata meta = new ObjectMetadata();
            meta.setContentType("image/jpeg");
            meta.setContentLength(buffer.length);
            oss.putFile(ossPath, is, meta);

            logger.info("Uploaded image to OSS: {}", ossPath);
        }
    }

}
