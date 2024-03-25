import getAllStorage from "../getAllStorage";

/**
 * 
 * @returns {string[]} keys
 */
export default () => {
  let items = getAllStorage();
  let keys = [];
  for (let index = 0; index < items.length; index++) {
    keys.push(items[index].key);
  }
  return keys;
};
