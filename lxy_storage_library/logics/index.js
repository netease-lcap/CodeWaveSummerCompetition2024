const $libraryName = 'lxy_storage_library'

const UtilsLogics = {}
import setLocalStorage from './setLocalStorage'
import getLocalStorage from './getLocalStorage'
import removeLocalStorage from './removeLocalStorage'
// LOGIC IMPORTS

UtilsLogics.install = function (Vue, option = {}) {
    Vue.prototype.$library = Vue.prototype.$library || {}
    Vue.prototype.$library[`${$libraryName}`] = {}
    Vue.prototype.$library[`${$libraryName}`].setLocalStorage=setLocalStorage
    Vue.prototype.$library[`${$libraryName}`].getLocalStorage=getLocalStorage
    Vue.prototype.$library[`${$libraryName}`].removeLocalStorage=removeLocalStorage
    // LOGIC USE
}

export default UtilsLogics