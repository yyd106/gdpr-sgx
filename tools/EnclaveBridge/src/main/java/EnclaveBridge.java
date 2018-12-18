public class EnclaveBridge {
    static {
        System.loadLibrary("EnclaveBridge");
    }

    public long messageHandlerOBJ;

    public EnclaveBridge() {
        messageHandlerOBJ = createMessageHandlerOBJ();
    }

    public native long createMessageHandlerOBJ();
    public native byte[] handleVerification(long msgHandlerAddr);
    public native byte[] handleMSG0(long msgHandlerAddr, String msg0);
    public native byte[] handleMSG2(long msgHandlerAddr, String msg2);
    public native byte[] handleAttestationResult(long msgHandlerAddr, String msg);

}
