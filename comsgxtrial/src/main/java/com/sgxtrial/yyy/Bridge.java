package com.sgxtrial.yyy;

import java.util.Map;

public class Bridge {

    public native void sgxCreateEnclave(String enclaveName, boolean sgxDebugFlag);
    public native void doAttestation(String eid, Map<String, String> p);

    static {
        System.loadLibrary("SgxBridgeImpl");
    }

    public static void main(String[] args) {
        Bridge b = new Bridge();
        b.sgxCreateEnclave("Hello2222", true);
    }

}
