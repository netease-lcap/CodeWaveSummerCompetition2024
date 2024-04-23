const $libraryName = 'library_fdddf_cookieutil'

const UtilsLogics = {}
import setCookie from './setCookie'
import getCookie from './getCookie'
import removeCookie from './removeCookie'
// LOGIC IMPORTS

UtilsLogics.install = function (Vue, option = {}) {
    Vue.prototype.$library = Vue.prototype.$library || {}
    Vue.prototype.$library[`${$libraryName}`] = {}
    Vue.prototype.$library[`${$libraryName}`].setCookie=setCookie
    Vue.prototype.$library[`${$libraryName}`].getCookie=getCookie
    Vue.prototype.$library[`${$libraryName}`].removeCookie=removeCookie
    // LOGIC USE
}

export default UtilsLogics