/** 
 * @param {string} key <true> cookie名称
 * @returns {string} result cookie值
 */
export default (key)=>{
    let cookieArr = document.cookie.split(";");
    if(cookieArr.length > 0){
        for (let index = 0; index < cookieArr.length; index++) {
            let pair = cookieArr[index].split('=');
            //匹配key
            if(key === pair[0].trim()){
                //解码返回cookie值
                return decodeURIComponent(pair[1]);
            }
            
        }
    }
    //空值
    return null;
}