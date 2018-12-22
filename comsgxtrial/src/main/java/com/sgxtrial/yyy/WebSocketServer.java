package com.sgxtrial.yyy;

import com.google.protobuf.InvalidProtocolBufferException;
import java.util.logging.*;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.nio.ByteBuffer;


@ServerEndpoint("/websocketendpoint")
public class WebSocketServer {
    private static final Logger logger = Logger.getLogger("WebSocketServer");

    private int messageSeq = 0;

    @OnOpen
    public void onOpen(){
        logger.info("websocket connected." + Thread.currentThread().toString());
    }

    @OnClose
    public void onClose(){
        logger.info("websocket closed");
    }

    @OnMessage
    public void onMessage(byte[] message, Session session){
        logger.info("receving msg" + Thread.currentThread().toString());
        // every time call enclave, message seq add 1.
        if (messageSeq == 0) {
            // EnclaveBridge bridge = EnclaveThreadManager.getInstance().getBridge();
            // bridge.sendMessage2(message);
            logger.info("receving msg1");
            Messages.MessageMsg0 msg0 = Messages.MessageMsg0.newBuilder().setEpid(123).setType(1).build();
            Messages.MessageMSG1 msg1 = Messages.MessageMSG1.newBuilder().setGaX(0, 456).build();
            sendMsgAsByteBuffer(session, msg0.toByteArray());
            sendMsgAsByteBuffer(session, msg1.toByteArray());
            logger.info("sending msg0 and msg1");
        } else if (messageSeq == 1) {
            logger.info("receving msg2");
            try {
                Messages.MessageMSG2 msg2 = Messages.MessageMSG2.parseFrom(ByteBuffer.wrap(message));
                logger.info("getting msg2");
                logger.info("" + msg2.getSignatureX(0));
                logger.info("sending msg3");
                Messages.MessageMSG3 msg3 = Messages.MessageMSG3.newBuilder().setGaxMsg3(0, 789).build();
                sendMsgAsByteBuffer(session, msg3.toByteArray());
            } catch (InvalidProtocolBufferException ie) {
                logger.fine(ie.toString());
            }
        } else if (messageSeq == 2) {
            logger.info("getting secret");
        }
        messageSeq++;
    }

    @OnError
    public void onError(Throwable e){
        logger.info(e.toString());
    }

    private void sendMsgAsByteBuffer(Session s, byte[] bytes) {
        try {
            s.getBasicRemote().sendBinary(ByteBuffer.wrap(bytes));
        } catch (java.io.IOException e) {
            logger.info(e.toString());
        }
    }

}
