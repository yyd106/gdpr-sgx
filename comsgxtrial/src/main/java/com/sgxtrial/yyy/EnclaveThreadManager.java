package com.sgxtrial.yyy;

public class EnclaveThreadManager {
    // init enclave within static var, so enclave is with the main thread
    private static EnclaveThreadManager instance = new EnclaveThreadManager();
    private EnclaveBridge bridge;

    private EnclaveThreadManager() {
        // init enclave here for sharing enclave
        // or init multiple EnclaveThread for load balancing
        this.bridge = new EnclaveBridge();
    }

    // TODO: might need to remove synchronized
    public synchronized static EnclaveThreadManager getInstance() {
        return instance;
    }

    public EnclaveBridge getBridge() {
        return bridge;
    }

    public EnclaveThread createEnclaveThread() {
        // for per enclave per thread mode
        return new EnclaveThread();
    }

}
