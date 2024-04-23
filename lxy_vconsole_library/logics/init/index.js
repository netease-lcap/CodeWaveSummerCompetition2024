/** 
 * * @param {string} theme <false> 主题默认light,可选dark
 */
import Vconsole from 'vconsole'
export default (theme="light")=>{
    function parseUrl(url) {
        const parsedUrl = new URL(url);
        const params = new URLSearchParams(parsedUrl.search);
        return params;
    }
    const params = parseUrl(window.location.href);
    const isDebug =  params.get("debug")
    if(isDebug && isDebug.toLowerCase() !== "false"){
        new Vconsole({ theme })
    }
}