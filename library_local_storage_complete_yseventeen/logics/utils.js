import CryptoJS from "crypto-js";
import config from "./config.js";

export default {
  isSupportStorage: () => {
    return typeof Storage !== "undefined" ? true : false;
  },
  // 名称前自动添加前缀
  autoAddPrefix: (key) => {
    const prefix = config.prefix ? config.prefix + "_" : "";
    if (key.startsWith(prefix)) {
      return key;
    } else {
      return prefix + key;
    }
  },
  encrypt: (data) => {
    if (typeof data === "object") {
      try {
        data = JSON.stringify(data);
      } catch (error) {
        console.log("encrypt error:", error);
      }
    }
    const dataHex = CryptoJS.enc.Utf8.parse(data);
    const encrypted = CryptoJS.AES.encrypt(dataHex, config.SECRET_KEY, {
      iv: config.SECRET_IV,
      mode: CryptoJS.mode.CBC,
      padding: CryptoJS.pad.Pkcs7,
    });
    return encrypted.ciphertext.toString();
  },
  decrypt: (data) => {
    const encryptedHexStr = CryptoJS.enc.Hex.parse(data);
    const str = CryptoJS.enc.Base64.stringify(encryptedHexStr);
    const decrypt = CryptoJS.AES.decrypt(str, config.SECRET_KEY, {
      iv: config.SECRET_IV,
      mode: CryptoJS.mode.CBC,
      padding: CryptoJS.pad.Pkcs7,
    });
    const decryptedStr = decrypt.toString(CryptoJS.enc.Utf8);
    return decryptedStr.toString();
  },
};
