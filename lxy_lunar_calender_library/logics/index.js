const $libraryName = 'lxy_lunar_calender_library'

const UtilsLogics = {}
import getLunar from './getLunar'
import getSolar from './getSolar'
import getMonthInChinese from './getMonthInChinese'
import getJieQi from './getJieQi'
import getLeapMonth from './getLeapMonth'
import getYearShengXiao from './getYearShengXiao'
import isFestivals from './isFestivals'
import getLunarDiff from './getLunarDiff'
import isFestivalsByLunar from './isFestivalsByLunar'
// LOGIC IMPORTS

UtilsLogics.install = function (Vue, option = {}) {
    Vue.prototype.$library = Vue.prototype.$library || {}
    Vue.prototype.$library[`${$libraryName}`] = {}
    Vue.prototype.$library[`${$libraryName}`].getLunar=getLunar
    Vue.prototype.$library[`${$libraryName}`].getSolar=getSolar
    Vue.prototype.$library[`${$libraryName}`].getMonthInChinese=getMonthInChinese
    Vue.prototype.$library[`${$libraryName}`].getJieQi=getJieQi
    Vue.prototype.$library[`${$libraryName}`].getLeapMonth=getLeapMonth
    Vue.prototype.$library[`${$libraryName}`].getYearShengXiao=getYearShengXiao
    Vue.prototype.$library[`${$libraryName}`].isFestivals=isFestivals
    Vue.prototype.$library[`${$libraryName}`].getLunarDiff=getLunarDiff
    Vue.prototype.$library[`${$libraryName}`].isFestivalsByLunar=isFestivalsByLunar
    // LOGIC USE
}

/* 阴转阳 */
console.log(getSolar("一九八六年四月廿一")) // 1986-05-29 
/* 阳转阴 */
console.log(getLunar("1998-10")); // 一九九八年八月十一
/* 转阴历月份 */
console.log(getMonthInChinese("1998-10")); // 八
/* 获取阴历节气信息 */
console.log(getJieQi("2024-2-4")); // 立春
/* 获取闰月 */
console.log(getLeapMonth("2024-2-4")); // 2
/* 获取生肖 */
console.log('获取生肖',getYearShengXiao("2024-2-4")); // 兔
/* 获取节日 */
console.log('是否节日',isFestivals("2024-2-12")); // 是
/* 阴历是否节日 */
console.log('阴历是否节日',isFestivalsByLunar("二零二四年正月初一")); // 是
/* 获取阴历天数差 */
console.log('天数差',getLunarDiff('一九八六年四月廿一','一九八六年四月廿九')); // 是


export default UtilsLogics