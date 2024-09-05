import config from "../config.js";
/**
 * @param {number} index <true> 索引
 * @returns {string} result
 */
export default (index) => {
  return window[config.type].key(index);
};
