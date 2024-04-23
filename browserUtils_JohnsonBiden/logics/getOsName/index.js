/** 
 * 获取操作系统名称 可能值为 Unknown、Windows、Mac、Android、Linux和iOS
 * @returns {string} result 
 */
export default ()=>{
    const userAgent = navigator.userAgent;
    let osName = "Unknown";

    if (userAgent.indexOf("Windows") !== -1) {
        osName = "Windows";
    } else if (userAgent.indexOf("Macintosh") !== -1) {
        osName = "Mac";
    } else if (userAgent.indexOf("Android") !== -1) {
        osName = "Android";
    } else if (userAgent.indexOf("Linux") !== -1) {
        osName = "Linux";
    } else if (userAgent.indexOf("iOS") !== -1) {
        osName = "iOS";
    }
    
    return osName;
}
