const crypto = require('crypto');
const { createHmac } = require('crypto');
const zlib = require('zlib');

/**
 * Generate a token with the given channel name, user ID, and time-to-live (TTL) in seconds.
 * @param {string} appKey: the app ID of your account in Netease Yunxin
 * @param {string} appSecret: the app secret of your account in Netease Yunxin
 * @param {string} channelName: the name of the channel for which the token is being generated
 * @param {number} uid: the user ID associated with the token
 * @param {number} ttlSec: the time-to-live (TTL) for the token in seconds, should be less than 86400 seconds (1 day)
 * @return: the token
 */
const GetToken = function (appKey, appSecret, channelName, uid, ttlSec) {
    if (!appKey || !appSecret) {
        throw new Error('appKey or appSecret is empty');
    }
    if (ttlSec <= 0) {
        throw new Error('ttlSec must be positive');
    }
    const curTimeMs = Date.now();
    return GetTokenWithCurrentTime(appKey, appSecret, channelName, uid, ttlSec, curTimeMs);
};

const GetTokenWithCurrentTime = function (appKey, appSecret, channelName, uid, ttlSec, curTimeMs) {
    if (ttlSec <= 0) {
        throw new Error('ttlSec must be positive');
    }
    const token = {
        signature: sha1(`${appKey}${uid}${curTimeMs}${ttlSec}${channelName}${appSecret}`),
        curTime: curTimeMs,
        ttl: ttlSec,
    };
    const signature = JSON.stringify(token);
    return Buffer.from(signature).toString('base64');
};

/**
 *
 * @param {string} appKey: the app ID of your account in Netease Yunxin
 * @param {string} permissionSecret:  the permission secret of your account in Netease Yunxin
 * @param {string} channelName: the name of the channel for which the token is being generated
 * @param {number} uid: the user ID associated with the token
 * @param {number} privilege: the privilege of the user. privilege is a 8-bit number, each bit represents a permission:
 *                     - bit 1: (0000 0001) = 1, permission to send audio stream
 *                     - bit 2: (0000 0010) = 2, permission to send video stream
 *                     - bit 3: (0000 0100) = 4, permission to receive audio stream
 *                     - bit 4: (0000 1000) = 8, permission to receive video stream
 *                     - bit 5: (0001 0000) = 16, permission to create room
 *                     - bit 6: (0010 0000) = 32, permission to join room
 *                     So, (0010 1100) = 32+8+4 = 44 means permission to receive audio&video stream and join room.
 *                     (0011 1111) = 63 means all permission allowed.
 * @param {number} ttlSec: the time-to-live (TTL) for the token in seconds, should be less than 86400 seconds (1 day)
 * @returns {string} the generated permission key
 */
const GetPermissionKey = function (appKey, permissionSecret, channelName, uid, privilege, ttlSec) {
    const curTime = Math.floor(Date.now() / 1000);
    return GetPermissionKeyWithCurrentTime(appKey, permissionSecret, channelName, uid, privilege, ttlSec, curTime);
};
const GetPermissionKeyWithCurrentTime = function (appKey, permSecret, channelName, uid, privilege, ttlSec, curTimeSec) {
    const permKey = {
        appkey: appKey,
        checksum: '',
        cname: channelName,
        curTime: curTimeSec,
        expireTime: ttlSec,
        privilege: privilege,
        uid: uid,
    };

    const checksum = hmacsha256(appKey, String(uid), String(curTimeSec), String(ttlSec), channelName, permSecret, String(privilege));
    permKey.checksum = checksum;

    const jsonStr = JSON.stringify(permKey);
    const compressedData = compress(jsonStr);
    return base64EncodeUrl(compressedData);
};

const hmacsha256 = function (appidStr, uidStr, curTimeStr, expireTimeStr, cname, permSecret, privilegeStr) {
    const contentToBeSigned = `appkey:${appidStr}\n` + `uid:${uidStr}\n` + `curTime:${curTimeStr}\n` + `expireTime:${expireTimeStr}\n` + `cname:${cname}\n` + `privilege:${privilegeStr}\n`;

    const keySpec = Buffer.from(permSecret, 'utf-8');
    const hmac = createHmac('sha256', keySpec);
    hmac.update(contentToBeSigned);
    const result = hmac.digest();

    return result.toString('base64');
};

const sha1 = function (input) {
    const sha1 = crypto.createHash('sha1');
    sha1.update(input, 'utf8');
    return sha1.digest('hex');
};

const base64EncodeUrl = function (input) {
    let base64 = Buffer.from(input).toString('base64');
    base64 = base64.replace(/\+/g, '*').replace(/\//g, '-').replace(/=/g, '_');
    return base64;
};

const compress = function (data) {
    return zlib.deflateSync(data);
};

module.exports = {
    GetToken: GetToken,
    GetTokenWithCurrentTime: GetTokenWithCurrentTime,
    GetPermissionKey: GetPermissionKey,
    GetPermissionKeyWithCurrentTime: GetPermissionKeyWithCurrentTime,
};
