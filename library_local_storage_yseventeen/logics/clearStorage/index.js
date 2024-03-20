import config from "../config.js";

export default () => {
  window[config.type].clear();
};
