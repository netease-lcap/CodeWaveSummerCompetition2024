/** 
 * @param {string} str <false> 阴历日期
 * @returns {string} result 
 */
import init,{getNumberDate,Lunar} from "../../lib"
export default (str)=>{
    const lunar = Lunar.fromYmd(...getNumberDate(str).split(",")).getSolar().toString()
    return lunar
}