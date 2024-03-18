const $libraryName = 'lxy_cookie_library'

const UtilsLogics = {}
import setCookie from './setCookie'
import getCookie from './getCookie'
// LOGIC IMPORTS

UtilsLogics.install = function (Vue, option = {}) {
    Vue.prototype.$library = Vue.prototype.$library || {}
    Vue.prototype.$library[`${$libraryName}`] = {}
    Vue.prototype.$library[`${$libraryName}`].setCookie=setCookie
    Vue.prototype.$library[`${$libraryName}`].getCookie=getCookie
    // LOGIC USE
}

// setCookie("name","fa",2)
// setCookie("name1","fa",2)
// console.log(getCookie("name"));

export default UtilsLogics