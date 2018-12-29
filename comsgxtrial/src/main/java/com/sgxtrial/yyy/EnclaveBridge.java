package com.sgxtrial.yyy;


public class EnclaveBridge {
    static {
        System.loadLibrary("EnclaveBridge");
    }

    private long messageHandlerOBJ;

    public EnclaveBridge() {
        messageHandlerOBJ = createMessageHandlerOBJ();
    }

    private native long createMessageHandlerOBJ();
    private native byte[] handleMessages(long msgHandlerAddr, byte[] msg);

    public byte[] callEnclave(byte[] msg) {
        return handleMessages(messageHandlerOBJ, msg);
    }

}
