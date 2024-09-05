import config from "../config.js";
import getStorage from "../getStorage";

/**
 * 
 * @returns {{key:string,val:string}[]} result
 */
export default () => {
  let len = window[config.type].length; // 获取长度
  let result = new Array(); // 定义数据集
  for (let i = 0; i < len; i++) {
    let getKey = window[config.type].key(i);
    let getVal = getStorage(getKey);
    result[i] = { key: getKey, val: getVal };
  }
  return result;
};
