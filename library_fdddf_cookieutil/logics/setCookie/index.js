import Cookies from "js-cookie"
// import CookieAttributes from "js-cookie"

/** 
 * @param {string} name <true> cookie名称
 * @param {string} value <true> cookie值
 * @param {string} path <false> cookie路径
 * @param {string} domain <false> cookie域名
 * @param {string} expires <false> cookie过期时间
 * @param {boolean} secure <false> https
 * @param {string} sameSite <false> 是否跨站，strict
 * @returns
 */
export default (name, value, path, domain, expires, secure, sameSite)=>{
    const options = {
        path: path || "/",
        domain: domain || undefined,
        expires: expires || undefined,
        secure: secure || undefined,
        sameSite: sameSite || undefined
    }
    Cookies.set(name, value, options)
}


