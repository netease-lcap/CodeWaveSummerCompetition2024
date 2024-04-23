/** 
 * @param {string} dateData <false> 日期
 * @returns {string} result 
 */
import init,{LunarYear} from "../../lib"
export default (dateData)=>{
    return LunarYear.fromYear(init(dateData).getYear()).getLeapMonth()
}