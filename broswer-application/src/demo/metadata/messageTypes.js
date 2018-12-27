import getInitMsg from "../msgs/initMsg";
import getMsg0 from "../msgs/msg0";
import getMsg2 from "../msgs/msg2";
import getAttMsg from "../msgs/attMsg";


export const RA_MSG0 = 0;
export const RA_MSG1 = 1;
export const RA_MSG2 = 2;
export const RA_MSG3 = 3;
export const RA_ATT_RESULT = 4;
export const RA_VERIFICATION = 5;
export const RA_APP_ATT_OK = 6;


const registry = {
  [RA_MSG0]:
  {
    name: "MessageMSG0",
    getPayload: () => getMsg0()
  },
  [RA_MSG1]:
  {
    name: "MessageMSG1"
  },
  [RA_MSG2]:
  {
    name: "MessageMSG2",
    getPayload: ecPublicKey => getMsg2(ecPublicKey)
  },
  [RA_MSG3]:
  {
    name: "MessageMSG3"
  },
  [RA_ATT_RESULT]:
  {
    name: "AttestationMessage",
    getPayload: () => getAttMsg()
  },
  [RA_VERIFICATION]:
  {
    name: "InitialMessage",
    getPayload: () => getInitMsg()
  },
  [RA_APP_ATT_OK]:
  {
    name: "SecretMessage"
  },
}


export default registry;
