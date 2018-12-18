package com.sgxtrial.yyy;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;


@ServerEndpoint("/websocketendpoint")
public class WebSocketServer {

    @OnOpen
    public void onOpen(){
        // on open, send msg0 and msg1 to js
        Messages.MessageMsg0 m0 = Messages.MessageMsg0.newBuilder().setEpid(1)
                .setStatus(1)
                .setType(1)
                .build();

    }

    @OnClose
    public void onClose(){
        // get password from enclave to write to DB
        System.out.println("Close Connection ...");
    }

    @OnMessage
    public String onMessage(String message, Session session){
        // if returns msg 2, pass to enclave, return msg3
        // else if returns msg4, pass to enclave, return enclave's response
        System.out.println("Message from the client: " + message);
        String echoMsg = "Echo from the server : " + message;
        return echoMsg;
    }

    @OnError
    public void onError(Throwable e){
        e.printStackTrace();
    }

}
