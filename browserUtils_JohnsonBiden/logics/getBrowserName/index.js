/** 
 * 获取浏览器名称 可能值为 opera|chrome|safari|firefox|msie|trident|unknown
 * @returns {string} result 
 */ 
export default ()=>{
    let match = RegExp(/(opera|chrome|safari|firefox|msie|trident(?=\/))\/?\s*(\d+)/i).exec(navigator.userAgent) || [];
    if(match.length===0){
        return "unknown";
    }
    return match[1].toLowerCase();
}
