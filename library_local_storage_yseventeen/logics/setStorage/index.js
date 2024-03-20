/**
 * @param {string} key <true> 存储的 key
 * @param {string} value <true> 存储的值
 * @param {number} expire <false> 过期时间，单位：秒，默认 60*60
 */

import config from "../config.js";

export default (key, value, expire = 0) => {
  if (value === "" || value === null || value === undefined) {
    value = null;
  }

  if (isNaN(expire) || expire < 1) {
    console.log("Expire is not valid, expire reset to " + config.expire + "秒");
    expire = config.expire;
  }

  expire = expire * 1000;
  let data = {
    value: value, // 存储值
    time: Date.now(), //存值时间戳
    expire: expire, // 过期时间
  };

  window[config.type].setItem(key, JSON.stringify(data));
};
