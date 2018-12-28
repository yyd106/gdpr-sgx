export const RA_MSG0 = 0;
export const RA_MSG1 = 1;
export const RA_MSG2 = 2;
export const RA_MSG3 = 3;
export const RA_ATT_RESULT = 4;
export const RA_VERIFICATION = 5;
export const RA_APP_ATT_OK = 6;

const registry = {
  [RA_MSG0]: "MessageMSG0",
  [RA_MSG1]: "MessageMSG1",
  [RA_MSG2]: "MessageMSG2",
  [RA_MSG3]: "MessageMSG3",
  [RA_ATT_RESULT]: "AttestationMessage",
  [RA_VERIFICATION]: "InitialMessage",
  [RA_APP_ATT_OK]: "SecretMessage",
}

export default registry;
