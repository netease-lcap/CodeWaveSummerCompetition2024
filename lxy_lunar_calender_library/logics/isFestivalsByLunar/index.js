/** 
 * @param {string} str <false> 阴历日期
 * @returns {boolean} result 
 */
import {getNumberDate,Lunar} from "../../lib"
import ww from "chinese-workday"
export default (str)=>{
   const data = Lunar.fromYmd(...getNumberDate(str).split(",")).getSolar()
   const isHoliday = ww.isHoliday;
   return isHoliday(data)
}