public class EnclaveBridge {
    static {
        System.loadLibrary("EnclaveBridge");
    }

    public long messageHandlerOBJ;

    public EnclaveBridge() {
        messageHandlerOBJ = createMessageHandlerOBJ();
    }

    public native long createMessageHandlerOBJ();
    public native String handleVerification(long msgHandlerAddr);
    public native String handleMSG0(long msgHandlerAddr, String msg0);
    public native String handleMSG2(long msgHandlerAddr, String msg2);
    public native String handleAttestationResult(long msgHandlerAddr, String msg);

    public static void main(String[] args) {
        new EnclaveBridge().handleVerification();
    }
}
