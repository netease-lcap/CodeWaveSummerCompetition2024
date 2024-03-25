import Cookies from "js-cookie"

/**
 * @param {string} name <true> cookie name
 * @param {string} path <false> cookie path
 * @param {string} domain <false> domain
 * @returns
 */
export default (name, path, domain)=>{
    const options = {
        path: path || '/',
        domain: domain || undefined,
    }
    Cookies.remove(name, options)
}