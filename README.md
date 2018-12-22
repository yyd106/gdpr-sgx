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
* main logic for sending/receving Messages.MsgX is in webclient.html.
* Node.js javascript has some minor different Language Grammar than Browser javascript.
  * to involve new javascript libraries from nodejs in browser, you need to install browserify to make it work.
  * http://browserify.org/
* build protobuf for javascript
  * https://hk.saowen.com/a/9e4dfeeb783b55b3e1003f9aacf176ac7175b3979805a4317e27c7255a67a1b4
  * https://zhuanlan.zhihu.com/p/34664727

## java files
* EnclaveThreadManager is a file to init EnclaveBridge
* WebSocketServer.java is a file to handle websocket connections from browser.
  * OnMessages is called every time when new byte[] incoming, it should pass byte[] directly to enclave, and gets byte[] from enclave and send back to javascript.

