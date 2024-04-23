/** 
 * @param {string} key <true> 要删除的cookie名称
 */
export default (key)=>{
    //设置过期时间为0，删除cookie
    document.cookie = `${key}=; max-age=0; path=/;`;
}