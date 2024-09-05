/** 
 * 获取浏览器大版本，失败返回unknown
 * @returns {string} result 
 */ 
export default ()=>{
    let match = RegExp(/(opera|chrome|safari|firefox|msie|trident(?=\/))\/?\s*(\d+)/i).exec(navigator.userAgent) || [];
    if(match.length<2){
        return "unknown";
    }
    return match[2].toLowerCase();
}
