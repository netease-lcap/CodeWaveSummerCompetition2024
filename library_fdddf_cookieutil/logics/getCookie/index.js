import Cookies from "js-cookie"

/** 
 * @param {string} name <true> cookie名称
 * @returns {string} result
 */
export default (name)=>{
    return Cookies.get(name)
}
