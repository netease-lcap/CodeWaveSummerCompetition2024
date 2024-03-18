/** 
 * @param {string} name <false> 这是一个描述
 * @returns {string} result 
 */
export default (name)=>{
    let cookies = document.cookie.split('; '); // 将cookie字符串分割成数组
    for (let i = 0; i < cookies.length; i++) {
        let parts = cookies[i].split('='); // 分割键和值
        if (parts[0] === name) { // 如果找到了cookie的名字
            return parts[1]; // 返回对应的值
        }
    }
    return ''; // 如果没有找到，返回空字符串
}