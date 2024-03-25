import CryptoJS from "crypto-js";
//定义参数 类型 window.localStorage,window.sessionStorage,
export default {
  type: "localStorage", // 本地存储类型 localStorage/sessionStorage
  prefix: "newpayV2.3", // 名称前缀一般设置为:项目名 + 项目版本，末尾会自动加上"_"；
  expire: 60 * 60, //过期时间 单位：秒
  isEncrypt: true, // 默认加密
  SECRET_KEY: CryptoJS.enc.Utf8.parse("778998nt57121345"), //十六位十六进制数作为密钥
  SECRET_IV:  CryptoJS.enc.Utf8.parse("nt657ab258931477"),// / 十六位十六进制数作为密钥偏移量
};
