import React from "react";
import protobuf from "protobufjs";
import proto from "./messages.proto";

import registry, {
  RA_VERIFICATION
} from "./metadata/messageTypes";

let PROTO, WEB_SOCKET;


/**
 * @desc load .proto file
 */
protobuf.load(proto)
  .then(root => {
    PROTO = root;
  }).catch(error => {
    console.log("error", error);
  });


class GDPRDemo extends React.Component {
  constructor() {
    super();
    this.setupWebSocket();
  }

  setupWebSocket() {
    if (WEB_SOCKET) return;

    WEB_SOCKET = new WebSocket("wss://echo.websocket.org"); // test ws 
    // WEB_SOCKET = new WebSocket("ws://localhost:8080/com.sgxtrial/websocketendpoint");

    WEB_SOCKET.onopen = evt => {
      console.log("Connection open ...");
      this.assembleMessage(RA_VERIFICATION);
    };

    WEB_SOCKET.onmessage = evt => {
      console.log("Received Message: " + evt.data);
      WEB_SOCKET.close();
    };

    WEB_SOCKET.onclose = evt => {
      console.log("Connection closing...");
    };
  }

  assembleMessage(type) {
    const { name, getPayload } = registry[type];

    /**
     * @desc assemble wrapped message
     */
    const wrappedMsgDef = PROTO.lookupType(`Messages.${name}`);
    const wrappedPayload = getPayload();
    const wrappedMsg = wrappedMsgDef.create(wrappedPayload);
    console.log("wrappedMsg", wrappedMsg);

    /**
     * @desc assemble all-in-one message
     */
    const allInOneMsgDef = PROTO.lookupType("Messages.AllInOneMessage");
    const allInOnePayload = {
      type,
      [name]: wrappedMsg
    };
    const allInOneMsg = allInOneMsgDef.create(allInOnePayload);
    console.log("allInOneMsg", allInOneMsg);

    /**
     * @desc encode message and send to web socket
     */
    const buffer = allInOneMsgDef.encode(allInOneMsg).finish();
    // WEB_SOCKET.send(buffer);
    WEB_SOCKET.send("Hello WebSockets!");
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
