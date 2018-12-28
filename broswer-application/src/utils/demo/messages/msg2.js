import {
  MSG2_SIZE,
  SIGRL,
  SIZE_SIGRL,
  AES_CMAC_KDF_ID,
  SAMPLE_QUOTE_LINKABLE_SIGNATURE
} from "../../../metadata/ecConstants";
import {
  RA_MSG2
} from "../../../metadata/messageTypes";

const aesCmac = require("node-aes-cmac").aesCmac;
const EC = require('elliptic').ec;
const ec = new EC('p256');
const bigInt = require("big-integer");
const findKeys = require("../keys/findKeys");


const getMsg2 = ecPublicKey => {
  const {
    MY_PRIVATE_KEY,
    MY_PUBLIC_KEY,
    SHORT_KEY
  } = findKeys(ecPublicKey);

  /**
   * @desc get signature: sign publck keys with my private key
   */
  const signKey = ec.keyFromPrivate(MY_PRIVATE_KEY);
  const sign_x = signKey.sign(MY_PUBLIC_KEY.X + ecPublicKey.X);
  const sign_y = signKey.sign(MY_PUBLIC_KEY.Y + ecPublicKey.Y);
  const signatureX = sign_x.toDER();
  const signatureY = sign_y.toDER();

  /**
   * @desc get smac
   */
  const sMyPublicKey = bigInt(MY_PUBLIC_KEY.X + MY_PUBLIC_KEY.Y).toString(16);
  const smac = aesCmac(SHORT_KEY, sMyPublicKey);

  return {
    type: RA_MSG2,
    size: MSG2_SIZE,
    publicKeyGx: MY_PUBLIC_KEY.X,
    publicKeyGy: MY_PUBLIC_KEY.Y,
    quoteType: SAMPLE_QUOTE_LINKABLE_SIGNATURE,
    spid: 0,
    cmacKdfId: AES_CMAC_KDF_ID,
    signatureX,
    signatureY,
    smac,
    sizeSigrl: SIZE_SIGRL,
    sigrl: SIGRL
  }
}


export default getMsg2;
