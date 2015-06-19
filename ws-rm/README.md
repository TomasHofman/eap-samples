JAX-WS Reliable Messaging Example
=================================

Intention is to create reliable messaging WS endpoint and client, which use RMTxStore for storing messages. 

1) run JBoss EAP

2) mvn clean install jboss-as:deploy

3) visit http://localhost:8080/ws-rm/ClientServlet - should return "Hello World" string
