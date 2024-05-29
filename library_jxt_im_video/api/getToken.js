const { GetToken } = require('./TokenBuilder.js');
const config = require('./config.json');

/**
 * 有效时间，单位秒，不能超过 86400，即 24 小时
 */
const ttlSec = 60 * 60 * 24;

const token = GetToken(config.appKey, config.appSecret, config.channelName, config.uid, ttlSec);

console.log(token);
