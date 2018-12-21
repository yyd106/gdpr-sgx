# gdpr-sgx
## compile and deploy
$ cd comsgxtrial
$ mvn package
$ ls target/
com.sgxtrial.war
$ cp com.sgxtrial.war $TOMCAT/webapps/
$ restart tomcat, then visit 
 http://localhost:8080/com.sgxtrial/webclient.html
## 
