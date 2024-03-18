const $libraryName = 'lxy_location_library'

const UtilsLogics = {}
import toString from './toString'
import reload from './reload'
import origin from './origin'
import hash from './hash'
import search from './search'
import href from './href'
import protocol from './protocol'
import host from './host'
import replace from './replace'
// LOGIC IMPORTS

UtilsLogics.install = function (Vue, option = {}) {
    Vue.prototype.$library = Vue.prototype.$library || {}
    Vue.prototype.$library[`${$libraryName}`] = {}
    Vue.prototype.$library[`${$libraryName}`].toString=toString
    Vue.prototype.$library[`${$libraryName}`].reload=reload
    Vue.prototype.$library[`${$libraryName}`].origin=origin
    Vue.prototype.$library[`${$libraryName}`].hash=hash
    Vue.prototype.$library[`${$libraryName}`].search=search
    Vue.prototype.$library[`${$libraryName}`].href=href
    Vue.prototype.$library[`${$libraryName}`].protocol=protocol
    Vue.prototype.$library[`${$libraryName}`].host=host
    Vue.prototype.$library[`${$libraryName}`].replace=replace
    // LOGIC USE
}


export default UtilsLogics