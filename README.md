# gdpr-sgx
## compile and deploy
1. $ cd comsgxtrial
2. $ mvn package
3. $ ls target/
 com.sgxtrial.war
4. $ cp com.sgxtrial.war $TOMCAT/webapps/
5. $ restart tomcat, then visit http://localhost:8080/com.sgxtrial/webclient.html

## javascript files 
* current javascript libraries are in resources/js/
  * including protobuf_msg.js for serializing and deserializing Messages.Msg0-4.
  * others are for ECDH encryptions.
* to involve new javascript libraries from nodejs, you need to install browserify to make it work for browser javascript.

## java files
* EnclaveThreadManager is a file to init EnclaveBridge
* WebSocketServer.java is a file to handle websocket connections from browser.
  * OnMessages is called every time when new byte[] incoming, it should pass byte[] directly to enclave, and gets byte[] from enclave and send back to javascript.

