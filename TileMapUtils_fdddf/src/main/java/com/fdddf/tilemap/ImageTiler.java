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
    private static final Logger logger = LoggerFactory.getLogger("LCAP_EXTENSION_LOGGER");

    private final ExecutorService executor = Executors.newFixedThreadPool(5);

    private static final int THREAD_COUNT = 50; // 设定线程数量

    private static final int MIN_ZOOM_LEVEL = 3;

    private static final float quality = 0.75f;

    private static final String[] imageTypes = {"jpg", "jpeg", "png"};

    private static final float memoryRate = 1.5f;

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
     *
     * @param request  TileRequest
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
                logger.error("tileImage error: " + Arrays.toString(e.getStackTrace()), e);
            } catch (OutOfMemoryError e) {
                resp.success = false;
                resp.failedReason = "OOM: no enough memory to process image";
                logger.error(resp.failedReason);
            }
            resp.id = request.id;
            callback.apply(resp);
        });
        return true;
    }

    /**
     * 检查图片信息是否可以切图
     *
     * @param request TileRequest
     * @return true or false
     */
    @NaslLogic
    public TileValidateResponse validate(TileRequest request) {
        TileValidateResponse resp;

        if (request.url == null || request.url.isEmpty()) {
            return new TileValidateResponse(ErrorCode.INVALID_URL);
        }
        if (request.tileSize == null || request.tileSize <= 0) {
            return new TileValidateResponse(ErrorCode.INVALID_TILE_SIZE);
        }
        if (request.outputDirectory == null || request.outputDirectory.isEmpty()) {
            return new TileValidateResponse(ErrorCode.INVALID_OUTPUT_DIRECTORY);
        }
        if (!(request.outputDirectory.startsWith("file://") || !request.outputDirectory.contains("/"))) {
            return new TileValidateResponse(ErrorCode.INVALID_OUTPUT_DIRECTORY);
        }

        BufferedImage mapImage;
        try {
            if (request.url.startsWith("http")) {
                mapImage = ImageIO.read(new URL(request.url));
            } else {
                mapImage = ImageIO.read(new File(request.url));
            }
        } catch (IOException e) {
            logger.error("io exception", e);
            throw new TileRuntimeException(e);
        }
        int width = mapImage.getWidth();
        int height = mapImage.getHeight();
        if (width > cfg.imageMaxWidth || height > cfg.imageMaxHeight) {
            resp = new TileValidateResponse(ErrorCode.INVALID_IMAGE_SIZE);
            resp.setMessage("image is too large, image size should less than " + cfg.imageMaxWidth + "x" + cfg.imageMaxHeight);
            return resp;
        }

        int minSquareSize = (int) Math.pow(2, MIN_ZOOM_LEVEL) * request.tileSize;
        if (Math.min(width, height) < minSquareSize) {
            resp = new TileValidateResponse(ErrorCode.INVALID_IMAGE_SIZE);
            resp.setMessage("image is too small, suggest image size is " + minSquareSize + "x" + minSquareSize);
            return resp;
        }
        // check image format, should be png or jpg
        String ext = FilenameUtils.getExtension(request.url).toLowerCase();
        if (ext.isEmpty() || !Arrays.asList(imageTypes).contains(ext)) {
            return new TileValidateResponse(ErrorCode.INVALID_IMAGE_TYPE);
        }
        // calculate runtime free memory and check if there is enough memory
        long mem = (long) width * height * 4; // rgba 4 bytes
        long freeMem = Runtime.getRuntime().freeMemory();
        long memWants = (long) (mem * memoryRate);
        if (freeMem < memWants) {
            resp = new TileValidateResponse(ErrorCode.INVALID_MEMORY_LIMIT);
            resp.setMessage("no enough memory( " + bytesToMiB(freeMem) + "<" + bytesToMiB(memWants) + ") to process the image");
            return resp;
        }
        String tips = String.format("image info: %dx%d, wants memory:%s, the app's free memory:%s",
                width, height, bytesToMiB(memWants), bytesToMiB(freeMem));

        logger.info(tips);
        resp = new TileValidateResponse(ErrorCode.SUCCESS);
        resp.tips = tips;
        return resp;
    }

    private static String bytesToMiB(long bytes) {
        return String.format("%.2f", bytes / 1024.0 / 1024.0) + " MiB";
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
        BufferedImage mapImage = null;
        BufferedImage squareImage = null;
        try {
            logger.info("url: {}, tileSize: {}, outputDirectory: {}", url, tileSize, outputDirectory);
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
                double ratio = (double) newSquareSize / squareSize;
                newImage = mapImage.getScaledInstance((int) (width * ratio), (int) (height * ratio), Image.SCALE_SMOOTH);
                mapImage.flush();
            }

            squareImage = new BufferedImage(newSquareSize, newSquareSize, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = squareImage.createGraphics();
            g2d.setColor(new Color(255, 255, 255));
            g2d.fillRect(0, 0, newSquareSize, newSquareSize);
            g2d.drawImage(newImage, 0, 0, null);
            g2d.dispose();

            newImage.flush();

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
                            } finally {
                                finalTile.flush();
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
        } finally {
            if (mapImage != null) {
                mapImage.flush();
            }
            if (squareImage != null) {
                squareImage.flush();
            }
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
            meta.setContentLength(is.available());
            oss.putFile(ossPath, is, meta);

            logger.info("Uploaded image to OSS: {}", ossPath);
        }
    }

}
