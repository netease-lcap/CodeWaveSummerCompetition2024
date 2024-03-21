/**
 * 判断是否为移动设备
 * @returns {boolean} result 是否为移动设备
 */
export default () => {
  const userAgent = navigator.userAgent.toLowerCase();
  const mobileRegex = /mobile|android|ipad|iphone/;
  return mobileRegex.test(userAgent);
};
