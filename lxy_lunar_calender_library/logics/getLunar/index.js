/** 
 * @param {string} dateData <false> 日期
 * @param {boolean} isFull <false> 是否完整
 * @returns {string} result 
 */
import init from "../../lib"
export default (dateData,isFull)=>{
    const lunar = init(dateData)
    return  isFull ? lunar.toFullString() : lunar.toString()
}