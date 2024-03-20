const $libraryName = 'library_local_storage_yseventeen'

const UtilsLogics = {}
import setStorage from './setStorage'
import getStorage from './getStorage'
import removeStorage from './removeStorage'
import clearStorage from './clearStorage'
// LOGIC IMPORTS

UtilsLogics.install = function (Vue, option = {}) {
    Vue.prototype.$library = Vue.prototype.$library || {}
    Vue.prototype.$library[`${$libraryName}`] = {}
    Vue.prototype.$library[`${$libraryName}`].setStorage=setStorage
    Vue.prototype.$library[`${$libraryName}`].getStorage=getStorage
    Vue.prototype.$library[`${$libraryName}`].removeStorage=removeStorage
    Vue.prototype.$library[`${$libraryName}`].clearStorage=clearStorage
    // LOGIC USE
}

export default UtilsLogics