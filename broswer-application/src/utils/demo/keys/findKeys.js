const { switchEndian, toHex } = require("../../hexHelpers")
const {
  myPrivateKey,
  getMyPublicKey,
  getMySharedKey
} = require("./ecdh");


let MY_PRIVATE_KEY, MY_PUBLIC_KEY, MY_SHARED_KEY, SHARED_KEY, SHORT_KEY;

/**
 * @method findKeys
 * @param {Object} ecPublicKey 
 */
function findKeys(ecPublicKey = {}) {

  // typeof MY_PRIVATE_KEY: String
  MY_PRIVATE_KEY = MY_PRIVATE_KEY || myPrivateKey;

  // typeof MY_PUBLIC_KEY: Object
  MY_PUBLIC_KEY = MY_PUBLIC_KEY || getMyPublicKey(MY_PRIVATE_KEY);

  // typeof MY_SHARED_KEY: Object
  MY_SHARED_KEY = MY_SHARED_KEY || getMySharedKey(MY_PRIVATE_KEY, ecPublicKey);

  // typeof SHARED_KEY: String
  SHARED_KEY = SHARED_KEY || MY_SHARED_KEY.X + MY_SHARED_KEY.Y;

  // typeof SHORT_KEY: String
  SHORT_KEY = SHORT_KEY || switchEndian(toHex(MY_SHARED_KEY.X)).slice(0, 32);

  return {
    MY_PRIVATE_KEY,
    MY_PUBLIC_KEY,
    MY_SHARED_KEY,
    SHARED_KEY,
    SHORT_KEY
  }
}

module.exports = findKeys;
