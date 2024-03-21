import config from "../config.js";

/**
 * @returns {string} result
 */
export default () => {
  return window[config.type].length;;
};
