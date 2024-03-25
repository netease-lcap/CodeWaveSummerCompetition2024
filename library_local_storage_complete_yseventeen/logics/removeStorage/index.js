import config from "../config.js";
import utils from "../utils.js";
/**
 * @param {string} key <true> 存储的 key
 */
export default (key) => {
  window[config.type].removeItem(utils.autoAddPrefix(key));
};
