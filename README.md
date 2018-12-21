# gdpr-sgx
## compile and deploy
1. $ cd comsgxtrial
2. $ mvn package
3. $ ls target/
 com.sgxtrial.war
4. $ cp com.sgxtrial.war $TOMCAT/webapps/
5. $ restart tomcat, then visit http://localhost:8080/com.sgxtrial/webclient.html
## 
