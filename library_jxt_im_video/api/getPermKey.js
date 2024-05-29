const { GetPermissionKey } = require('./TokenBuilder.js');
const config = require('./config.json');

/**
 * 有效时间，单位秒，不能超过 86400，即 24 小时
 */
const ttlSec = 60 * 60 * 24;

var privilege = 1;

const permKey = GetPermissionKey(config.appKey, config.permKeySecret, config.channelName, config.uid, privilege, ttlSec);

console.log(permKey);
