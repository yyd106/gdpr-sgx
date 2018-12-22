package com.sgxtrial.yyy;

import java.util.Map;

public class EnclaveBridge {

    public native void sgxCreateEnclave(String enclaveName, boolean sgxDebugFlag);
    public native void doAttestation(String eid, Map<String, String> p);
    public native byte[] sendMessage2(byte[] msg2);
    public native byte[] sendMessage4(byte[] msg4);

    static {
        System.loadLibrary("SgxBridgeImpl");
    }

    public EnclaveBridge() {
        sgxCreateEnclave("test123", true);
    }

}
