const $libraryName = "browser_utils";

const UtilsLogics = {};
import isOnline from "./isOnline";
import isMobile from "./isMobile";
import getBrowserName from './getBrowserName'
import getBrowserVersion from './getBrowserVersion'
import getOsName from './getOsName'
import isCookieEnabled from './isCookieEnabled'
import getWindowInnerWidth from './getWindowInnerWidth'
import getWindowInnerHeight from './getWindowInnerHeight'
import getUserAgent from './getUserAgent'
// LOGIC IMPORTS

UtilsLogics.install = function (Vue, option = {}) {
  Vue.prototype.$library = Vue.prototype.$library || {};
  Vue.prototype.$library[`${$libraryName}`] = {};
  Vue.prototype.$library[`${$libraryName}`].isOnline = isOnline;
  Vue.prototype.$library[`${$libraryName}`].isMobile = isMobile;
  Vue.prototype.$library[`${$libraryName}`].getBrowserName=getBrowserName;
    Vue.prototype.$library[`${$libraryName}`].getBrowserVersion=getBrowserVersion;
    Vue.prototype.$library[`${$libraryName}`].getOsName=getOsName;
    Vue.prototype.$library[`${$libraryName}`].isCookieEnabled=isCookieEnabled;
    Vue.prototype.$library[`${$libraryName}`].getWindowInnerWidth=getWindowInnerWidth
    Vue.prototype.$library[`${$libraryName}`].getWindowInnerHeight=getWindowInnerHeight
    Vue.prototype.$library[`${$libraryName}`].getUserAgent=getUserAgent
    // LOGIC USE
};

export default UtilsLogics;
