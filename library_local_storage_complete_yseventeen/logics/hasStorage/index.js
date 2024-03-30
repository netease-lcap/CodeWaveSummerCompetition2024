import utils from "../utils.js";
import getAllStorage from "../getAllStorage";

/**
 * @param {string} key <true> 想要获取的客户端存储的key
 * @returns {boolean} result
 */
export default (key) => {
  key = utils.autoAddPrefix(key);
  let arr = getAllStorage().filter((item) => {
    return item.key === key;
  });
  return arr.length ? true : false;
};
