/** 
 * @param {string} key <true> cookie名称
 * @param {string} value <true> cookie值
 * @param {Long} expire `<false>` 过期时间,单位秒
 * @param {string} path `<false>` cookie作用域,默认为‘/’
 * @returns {string} result cookie值
 */
export default (key,value,expire,path)=>{
    let maxAge = ';';
    let scope = 'path=/;'
    if(expire){
        maxAge = "; max-age=" + expire + ";";
    }
    if(path){
        scope = "path=" + path + ";";
    }
    document.cookie = key + '=' + value + maxAge + scope;
    return value;
}