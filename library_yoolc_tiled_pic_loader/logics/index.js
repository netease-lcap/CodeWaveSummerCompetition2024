const $libraryName = 'library_yoolc_tiled_pic_loader'

const UtilsLogics = {}
import tiledPicSlicer from './tiledPicSlicer'
// LOGIC IMPORTS

UtilsLogics.install = function (Vue, option = {}) {
    Vue.prototype.$library = Vue.prototype.$library || {}
    Vue.prototype.$library[`${$libraryName}`] = {}
    Vue.prototype.$library[`${$libraryName}`].tiledPicSlicer=tiledPicSlicer
    // LOGIC USE
}

export default UtilsLogics