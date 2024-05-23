/** 
 * @param {File} uploadPic <false> 上传图片
 * @param {string} ossEndpoint <false> OSS 服务器上传地址
 * @param {string} ossRegion <false> OSS 服务器上传 Region
 * @param {string} ossAccessKeyId <false> OSS 服务器上传 AccessKeyId
 * @param {string} ossAccessKeySecret <false> OSS 服务器上传 AccessKeySecret
 * @param {string} ossBucket <false> OSS 服务器上传 Bucket
 * @param {number} maxZoomLevel <false> 最大缩放级别
 * @param {number} tileSize <false> 瓦片大小
 * @param {number} compressionQuality <false> 压缩质量
 * @param {number} maxWidth <false> 最大宽度
 * @param {number} maxHeight <false> 最大高度
 * @param {number} minWidth <false> 最小宽度
 * @param {number} minHeight <false> 最小高度
 * @param {number} maxSize <false> 最大尺寸
 * @returns {string} result 
 */

import OSS from 'ali-oss';

export default async (uploadPic, ossEndpoint, ossRegion, ossAccessKeyId, ossAccessKeySecret, ossBucket, maxZoomLevel, tileSize, compressionQuality, maxWidth, maxHeight, minWidth, minHeight, maxSize) => {
    const client = new OSS({
        region: ossRegion,
        accessKeyId: ossAccessKeyId,
        accessKeySecret: ossAccessKeySecret,
        bucket: ossBucket,
    });

    const prefix = `${uploadPic.name.split('.')[0]}`;
    const result = await cutTilesAndUpload(client, uploadPic, prefix, maxZoomLevel, tileSize, compressionQuality, maxWidth, maxHeight, minWidth, minHeight, maxSize);
    if (result === -1) {
        throw new Error("Failed to slice and upload the image.");
    }

    const url = `${ossEndpoint}/${prefix}/{z}-{x}-{y}.webp`;
    return url;
}

function ossUpload(client, path, blob) {
    return new Promise(async (resolve) => {
        console.log("Uploading to oss", path, blob);
        try {
            const result = await client.put(path, blob);
            console.log('Upload Success', result);
            return result;
        } catch (error) {
            console.error('Upload Error', error);
            throw error;
        }
    });
}

async function loadImage(file) {
    if (!(file instanceof File)) {
        throw new Error('上传的必须是文件对象');
    }

    return new Promise((resolve, reject) => {
        const img = new Image();
        img.onload = () => {
            // 释放对象 URL 以释放内存，即使加载失败也要释放
            URL.revokeObjectURL(img.src);
            resolve(img);
        };
        img.onerror = (error) => {
            URL.revokeObjectURL(img.src);
            reject(error);
        };
        // 创建对象 URL 以加载图片
        img.src = URL.createObjectURL(file);
        img.crossOrigin = 'Anonymous';
    });
}

async function cutTilesAndUpload(client, uploadPic, prefix, maxZoomLevel, tileSize, compressionQuality, maxWidth, maxHeight, minWidth, minHeight, maxSize) {
    const img = await loadImage(uploadPic);
    const originalWidth = img.width;
    const originalHeight = img.height;
    const imageSize = originalWidth * originalHeight * 4; // Estimate image size
    const imageExtension = uploadPic.name.split('.').pop().toLowerCase();

    if (originalWidth > maxWidth || originalHeight > maxHeight) {
        console.log('Image too large');
        return -1;
    } else if (originalWidth < minWidth || originalHeight < minHeight) {
        console.log("Image is too small, we don't need tiled loader for this image.");
        return -1;
    } else if (imageSize > maxSize) {
        console.log('Image size is too large');
        return -1;
    } else if (['png', 'jpg', 'jpeg'].indexOf(imageExtension) === -1) {
        console.log('Image format not supported');
        return -1;
    }

    // Web Worker to slice picture
    const workerScript = `
        self.onmessage = async (e) => {
            console.log("Worker received", e.data);
            const { prefix, imageData, z, x, y, tileSize, compressionQuality } = e.data;
            
            const tileCanvas = new OffscreenCanvas(tileSize, tileSize);
            const tileCtx = tileCanvas.getContext('2d');

            // Clear the canvas to transparent
            tileCtx.clearRect(0, 0, tileSize, tileSize);

            // Put the image data onto the tile canvas
            tileCtx.putImageData(imageData, 0, 0);
            
            const tilePath = prefix + \`/\${z}-\${x}-\${y}.webp\`;
            tileCanvas.convertToBlob({ type: 'image/webp', quality: compressionQuality }).then(blob => {
                console.log("Blob created");
                self.postMessage({ tilePath, blob });
            }).catch(err => {
                console.error(err);
            });
        };
    `;

    const workerBlob = new Blob([workerScript], { type: 'application/javascript' });
    const workerUrl = URL.createObjectURL(workerBlob);

    const worker = new Worker(workerUrl);
    const uploadQueue = [];

    worker.onmessage = async (e) => {
        const { tilePath, blob } = e.data;
        uploadQueue.push(() => ossUpload(client, tilePath, blob));

        if (uploadQueue.length === 1) {
            while (uploadQueue.length > 0) {
                await uploadQueue[0]();
                uploadQueue.shift();
            }
        }
    };

    for (let z = maxZoomLevel; z >= 0; z--) {
        const scale = 1.0 / Math.pow(2, maxZoomLevel - z);
        const scaledWidth = Math.floor(originalWidth * scale);
        const scaledHeight = Math.floor(originalHeight * scale);

        const offscreenCanvas = new OffscreenCanvas(scaledWidth, scaledHeight);
        const ctx = offscreenCanvas.getContext('2d');
        ctx.drawImage(img, 0, 0, scaledWidth, scaledHeight);

        const rows = Math.ceil(scaledHeight / tileSize);
        const cols = Math.ceil(scaledWidth / tileSize);

        for (let y = 0; y < rows; y++) {
            for (let x = 0; x < cols; x++) {
                const imageData = ctx.getImageData(x * tileSize, y * tileSize, tileSize, tileSize);
                worker.postMessage({ prefix, imageData, z, x, y, tileSize, compressionQuality });
            }
        }
    }

    return 0;
}