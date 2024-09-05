import config from "../config.js";
import setStorage from "../setStorage";
import removeStorage from "../removeStorage";
import utils from "../utils.js";

/**
 * @param {string} key <true> 存储的 key
 * @returns {string} result
 */

export default (key) => {
  key = utils.autoAddPrefix(key);
  if (
    !window[config.type].getItem(key) ||
    JSON.stringify(window[config.type].getItem(key)) === "null"
  ) {
    return null;
  }
  const storage = config.isEncrypt
    ? JSON.parse(utils.decrypt(window[config.type].getItem(key)))
    : JSON.parse(window[config.type].getItem(key));

  let nowTime = Date.now();
  // 过期删除
  if (storage.expire && nowTime > storage.time + storage.expire) {
    console.log("localStorage过期删除");
    removeStorage(key);
    return null;
  } else {
    // 未过期期间被调用则自动续期进行保活
    setStorage(key, storage.value, storage.expire / 1000 || config.expire);
    return storage.value;
  }
};
