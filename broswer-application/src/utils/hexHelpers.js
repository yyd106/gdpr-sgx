const bigInt = require("big-integer");

/**
 * @method switchEndian
 * @param {String} string needed to be transformed
 * @returns {String} result
 */
export function switchEndian(string) {
  if (string.length % 2) throw new Error();

  let result = []
  for (let i = 0; i < string.length; i = i + 2) {
    const item = string.slice(i, i + 2);
    result.push(item);
  }

  return result.reverse().join("");
}

/**
 * @method toHex
 * @param {String} input needed to be transformed
 * @returns {String} result
 */
export function toHex(input) {
  return bigInt(input).toString(16);
}

/**
 * @method hexStringToArray
 * @param {String} str
 * @returns {Array} result
 */
export function hexStringToArray(str, step) {
  if (str.length % step) throw new Error();

  let result = [];
  for (let i = 0; i < str.length; i = i + step) {
    const chunk = str.slice(i, i + step);
    const num = parseInt(chunk, 16);
    result.push(num);
  }

  return result;
}