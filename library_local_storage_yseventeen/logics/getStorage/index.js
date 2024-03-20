/**
 * @param {string} key <true> 存储的 key
 * @returns {string} result
 */

import config from "../config.js";
import setStorage from "../setStorage";
import removeStorage from "../removeStorage";

export default (key) => {
  // key 不存在判断
  if (
    !window[config.type].getItem(key) ||
    JSON.stringify(window[config.type].getItem(key)) === "null"
  ) {
    return null;
  }
  const storage = JSON.parse(window[config.type].getItem(key));
  let nowTime = Date.now();
  // 过期删除
  if (storage.expire && nowTime > storage.time + storage.expire) {
    console.log("localStorage过期删除")
    removeStorage(key);
    return null;
  } else {
    // 未过期期间被调用 则自动续期 进行保活
    setStorage(key, storage.value, storage.expire/1000 || config.expire);
    return storage.value;
  }
};
