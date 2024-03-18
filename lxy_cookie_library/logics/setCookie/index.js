/** 
 * @param {string} name <false> 名称
 * @param {string} value <false> 内容
 * @param {Long} daysToLive <false> 时间为天
 * @returns {string} result 
 */
export default (name,value,daysToLive)=>{
    let cookie = name + "=" + encodeURIComponent(value);
    if (typeof daysToLive === "number") {
        cookie += "; max-age=" + (daysToLive*24*60*60); // 将天数转换为秒数
    }
    document.cookie = cookie;
}