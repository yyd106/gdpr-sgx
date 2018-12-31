# gdpr-sgx

## Abbreviation
* ***root_dir***: gdpr-sgx root directory

## Build environment
### Install prerequisites
1. cd ***root_dir***
2. run command ```./scripts/install_deps.sh -r -s``` to install dependencies
3. note: in Step 2, **-r** means install **remote attestation** dependencies while **-s** does for **SGX SDK**

### Build remote attestation
1. cd ***root_dir***
2. use ```./scripts/build_targets.sh [options]``` to build targets
3. in Step 2, there are some options for the command
  * ***-r*** build **remote attestation** targets
  * ***-m*** if you provide **-r** option the default value for **-m** is **HW** which builds targets in Hardware mode otherwise you can use **SIM** to build in simulation mode
  * ***-s*** build **SGX SDK**
  * ***-t*** build **simple** remote attestation and SGX SDK(if -r and -s are provided) 
  * ***-j*** build ***enclave client .so library file***. The built targets will be moved to ***root_dir/target***
  * ***-h*** show usage information

## Web Server
### connect EnclaveBridge in webserver to cpp library
1. in EnclaveCPP, build libEnclaveBridge.so
2. Following https://wiki.apache.org/tomcat/HowTo#I.27m_encountering_classloader_problems_when_using_JNI_under_Tomcat
   > create a directory, ${tomcat}/shared/lib, copy libEnclaveBridge.so into it.
   > edit ${tomcat}/conf/catalina.properties, look for "shared.loader", edit this line to: shared.loader="${catalina.home}/shared/lib/"
3. restart tomcat, and the EnclaveBridge should be connected to .so.
   > if logs shows "java.lang.RuntimeException: no EnclaveBridge in java.library.path", edit ${tomcat}/bin/catalina.sh, look for "JAVA_OPTS", edit this line to: JAVA_OPTS="$JAVA_OPTS $JSSE_OPTS -Djava.library.path=$CATALINA_HOME/shared/lib"
   > restart tomcat, it should be working.

### compile and deploy
1. $ cd comsgxtrial
2. $ mvn package
3. $ ls target/
 com.sgxtrial.war
4. $ cp com.sgxtrial.war $TOMCAT/webapps/
5. $ restart tomcat, then visit http://localhost:8080/com.sgxtrial/webclient.html

### javascript files 
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

### java files
* EnclaveThreadManager is a file to init EnclaveBridge
* WebSocketServer.java is a file to handle websocket connections from browser.
  * OnMessages is called every time when new byte[] incoming, it should pass byte[] directly to enclave, and gets byte[] from enclave and send back to javascript.
