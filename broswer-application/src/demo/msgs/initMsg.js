const {
  RA_VERIFICATION
} = require("../metadata/messageTypes");


function getInitMsg() {
  return {
    type: RA_VERIFICATION,
    size: 1
  };
}


module.exports = getInitMsg;
