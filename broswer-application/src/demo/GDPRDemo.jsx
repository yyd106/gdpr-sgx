import React from "react";
import protobuf from "protobufjs";
import proto from "./messages.proto";


import registry from "./metadata/messageRegistry";
import {
  RA_VERIFICATION, RA_MSG0, RA_MSG1, RA_MSG3, RA_MSG2, RA_ATT_RESULT
} from "./metadata/messageTypes";

import getEcMsg from "./utils/mock";

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

      const initMsg = this.assemble(RA_VERIFICATION);
      WEB_SOCKET.send(initMsg);
      console.log("======== Initial message sent ========\n\n\n\n\n");
    };

    WEB_SOCKET.onmessage = evt => {
      // console.log("Received Message: " + evt.data);

      //mock message
      const ecMsg = getEcMsg(PROTO);
      if (ecMsg) {
        this.handleMessage(ecMsg);
      } else {
        WEB_SOCKET.close();
        return;
      }
    };

    WEB_SOCKET.onclose = evt => {
      console.log("Connection closing...");
    };
  }

  assemble(type, ecPublicKey = {}) {
    const { defName, getPayload, fieldName } = registry[type];

    /**
     * @desc assemble wrapped message
     */
    const wrappedMsgDef = PROTO.lookupType(`Messages.${defName}`);
    const wrappedPayload = getPayload(ecPublicKey);
    const wrappedMsg = wrappedMsgDef.create(wrappedPayload);
    // console.log("wrappedMsg", wrappedMsg);

    /**
     * @desc assemble all-in-one message
     */
    const allInOneMsgDef = PROTO.lookupType("Messages.AllInOneMessage");
    const allInOnePayload = {
      type,
      [fieldName]: wrappedMsg
    };
    const allInOneMsg = allInOneMsgDef.create(allInOnePayload);
    // console.log("allInOneMsg", allInOneMsg);


    /**
     * @desc encode message and send to web socket
     */
    const buffer = allInOneMsgDef.encode(allInOneMsg).finish();
    const decoded = allInOneMsgDef.decode(buffer);
    console.log("Message to sent:", decoded);

    return buffer;
  }


  disassemble(buffer, type = "allInOneMsg") {
    const handler = registry[type] || {};
    const { defName = "AllInOneMessage" } = handler;

    const msgDef = PROTO.lookupType(`Messages.${defName}`);
    const message = msgDef.decode(buffer);

    return message;
  }


  handleMessage(buffer) {
    const message = this.disassemble(buffer);

    const { type } = message;
    const { defName } = registry[type];

    console.log("======== ", defName, "received ========");
    console.log("Received message:", message);

    let msgToSent;

    switch (type) {
      case RA_MSG0:
        msgToSent = this.assemble(RA_MSG0);
        break;

      case RA_MSG1:
        const { msg1 } = message;
        const { GaX, GaY } = msg1;
        const ecPublicKey = {
          X: GaX.join("").toString(),
          Y: GaY.join("").toString()
        }
        msgToSent = this.assemble(RA_MSG2, ecPublicKey);
        break;

      case RA_MSG3:
        msgToSent = this.assemble(RA_ATT_RESULT);
        break;

      default:
        break;
    }

    if (!msgToSent) return

    WEB_SOCKET.send(msgToSent);
    // console.log("Message content:", msgToSent);
    console.log("======== Message sent ========\n\n\n\n\n");
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
