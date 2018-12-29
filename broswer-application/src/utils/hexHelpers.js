const bigInt = require("big-integer");

/**
 * @method switchEndian
 * @param {String} string needed to be transformed
 * @returns {String} result
 */
export function switchEndian(string) {
  if (string.length % 2) throw new Error;

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