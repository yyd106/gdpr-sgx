public class EnclaveBridge {
    static {
        System.loadLibrary("EnclaveBridge");
    }

    public long messageHandlerOBJ;

    public EnclaveBridge() {
        messageHandlerOBJ = createMessageHandlerOBJ();
    }

    public native long createMessageHandlerOBJ();
    public native String[] handleMessages(long msgHandlerAddr, String type, byte[] msg);

}
