//定义参数 类型 window.localStorage,window.sessionStorage,
export default {
  type: "localStorage", // 本地存储类型 localStorage/sessionStorage
  prefix: "yseventeen", // --暂时没用 名称前缀 建议：项目名 + 项目版本
  expire: 60 * 60, //过期时间 单位：秒
//   isEncrypt: true, // 默认加密 为了调试方便, 开发过程中可以不加密
};
