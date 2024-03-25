const $libraryName = 'library_local_storage_complete_yseventeen'

const UtilsLogics = {}
import setStorage from './setStorage'
import getStorage from './getStorage'
import removeStorage from './removeStorage'
import clearStorage from './clearStorage'
import getAllStorage from './getAllStorage'
import getStorageKeyForIndex from './getStorageKeyForIndex'
import getStorageKeys from './getStorageKeys'
import getStorageArrLength from './getStorageArrLength'
import hasStorage from './hasStorage'
// LOGIC IMPORTS

UtilsLogics.install = function (Vue, option = {}) {
    Vue.prototype.$library = Vue.prototype.$library || {}
    Vue.prototype.$library[`${$libraryName}`] = {}
    Vue.prototype.$library[`${$libraryName}`].setStorage=setStorage
    Vue.prototype.$library[`${$libraryName}`].getStorage=getStorage
    Vue.prototype.$library[`${$libraryName}`].removeStorage=removeStorage
    Vue.prototype.$library[`${$libraryName}`].clearStorage=clearStorage
    Vue.prototype.$library[`${$libraryName}`].getAllStorage=getAllStorage
    Vue.prototype.$library[`${$libraryName}`].getStorageKeyForIndex=getStorageKeyForIndex
    Vue.prototype.$library[`${$libraryName}`].getStorageKeys=getStorageKeys
    Vue.prototype.$library[`${$libraryName}`].getStorageArrLength=getStorageArrLength
    Vue.prototype.$library[`${$libraryName}`].hasStorage=hasStorage
    // LOGIC USE
}

export default UtilsLogics