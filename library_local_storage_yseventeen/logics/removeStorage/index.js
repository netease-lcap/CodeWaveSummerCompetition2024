/**
 * @param {string} key <true> 存储的 key
 */

import config from "../config.js";

export default (key) => {
  window[config.type].removeItem(key);
};
