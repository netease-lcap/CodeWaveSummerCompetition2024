/** 
 * @param {string} str1 <false> 阴历日期1
 * @param {string} str2 <false> 阴历日期2
 * @returns {string} result 
 */
import {getNumberDate,Lunar} from "../../lib"
import dayjs from "dayjs"
export default (str1,str2,type="d")=>{
   const date1 =  Lunar.fromYmd(...getNumberDate(str1).split(",")).getSolar()
   const date2 =  Lunar.fromYmd(...getNumberDate(str2).split(",")).getSolar()
   return dayjs(date1).diff(dayjs(date2),type)
}