/** 
 * @param {string} dateData <false> 日期
 * @returns {boolean} result 
 */
import ww from "chinese-workday"
export default (dateData)=>{
    const isHoliday = ww.isHoliday;
    return isHoliday(dateData)
}