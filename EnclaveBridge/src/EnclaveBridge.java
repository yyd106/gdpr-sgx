public class EnclaveBridge {
    static {
        System.loadLibrary("EnclaveBridge");
    }

    public native String handleVerification();
    public native String handleMSG0(String msg0);
    public native String handleMSG2(String msg2);
    public native String handleAttestationResult(String msg);
}
