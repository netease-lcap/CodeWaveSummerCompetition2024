/** 
 * @param {string} key <false> 本地存储的key
 * @param {string} value <false> 本地存储的value
 */
export default (key,value) => {
    localStorage.setItem(key, value);
}