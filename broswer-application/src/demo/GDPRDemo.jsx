import React from "react";
import proto from "./messages.proto";

// import registry from "./metadata/messageTypes";
import getInitMsg from "./msgs/initMsg";
import getMsg2 from "./msgs/msg2";
import getAttMsg from "./msgs/attMsg";

import { ecPublicKey } from "./utils/mocks";

// const protobuf = require("protobufjs");
// const promise = protobuf.load(proto);

class GDPRDemo extends React.Component {
  componentDidMount() {
    const initMsg = getInitMsg();
    console.log("initial message", initMsg);

    const msg2 = getMsg2(ecPublicKey);
    console.log("message 2", msg2);

    const attMsg = getAttMsg();
    console.log("attestation message", attMsg);
  }

  render() {
    return (
      <div className="text-center">
        <h5>Demo</h5>
      </div>
    );
  }
}

export default GDPRDemo;
