import NERTC from 'nertc-web-sdk/NERTC';

/**
 * 转换视频帧率枚举值
 * @param {number} frameRate 视频帧率枚举值
 */
export const transFrameRate = (frameRate) => {
    switch (frameRate) {
        case '5f':
            return NERTC.CHAT_VIDEO_FRAME_RATE_5;
        case '10f':
            return NERTC.CHAT_VIDEO_FRAME_RATE_10;
        case '15f':
            return NERTC.CHAT_VIDEO_FRAME_RATE_15;
        case '20f':
            return NERTC.CHAT_VIDEO_FRAME_RATE_20;
        case '25f':
            return NERTC.CHAT_VIDEO_FRAME_RATE_25;
        case '30f':
            return NERTC.CHAT_VIDEO_FRAME_RATE_30;
        default:
            return NERTC.CHAT_VIDEO_FRAME_RATE_NORMAL;
    }
};

/**
 * 转换视频分辨率枚举值
 * @param {number} videoQuality
 */
export const transVideoQuality = (videoQuality) => {
    switch (videoQuality) {
        case '180P':
            return NERTC.VIDEO_QUALITY_180p;
        case '480P':
            return NERTC.VIDEO_QUALITY_480p;
        case '720P':
            return NERTC.VIDEO_QUALITY_720p;
        case '1080P':
            return NERTC.VIDEO_QUALITY_1080p;
        default:
            return NERTC.VIDEO_QUALITY_720p;
    }
};
