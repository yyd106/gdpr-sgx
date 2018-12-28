import { RA_MSG0, RA_MSG1, RA_MSG3 } from "../../metadata/messageTypes";
import registry from "./messageRegistry";

const EC_PUBLIC_KEY = {
  X: "37139192525400767396954351293006657235352209374019103420088709115342038670395",
  Y: "16293427347722903506929843354920281780388370777564665954131038642838113957950"
};

const MSG_LIST = [RA_MSG0, RA_MSG1, RA_MSG3];

let index = -1;


const getEcMsg = (PROTO) => {
  index = index + 1;
  if (index >= MSG_LIST.length) return;

  const type = MSG_LIST[index];
  const { defName, getPayload, fieldName } = registry[type];

  /**
   * @desc assemble wrapped message
   */
  const wrappedMsgDef = PROTO.lookupType(`Messages.${defName}`);
  const wrappedPayload = getPayload(EC_PUBLIC_KEY);
  const wrappedMsg = wrappedMsgDef.create(wrappedPayload);

  /**
   * @desc assemble all-in-one message
   */
  const allInOneMsgDef = PROTO.lookupType("Messages.AllInOneMessage");
  const allInOnePayload = {
    type,
    [fieldName]: wrappedMsg
  };
  const allInOneMsg = allInOneMsgDef.create(allInOnePayload);

  const buffer = allInOneMsgDef.encode(allInOneMsg).finish();
  return buffer;
}


export default getEcMsg;
