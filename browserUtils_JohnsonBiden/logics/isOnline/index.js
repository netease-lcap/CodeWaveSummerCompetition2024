/**
 * 判断是否联网，非实际测试，而是浏览器汇报的。
 * @returns {boolean} result 是否联网
 */
export default () => {
  return navigator.onLine;
};
