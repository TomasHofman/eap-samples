/*
 * JBoss, Home of Professional Open Source
 * Copyright 2014, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package thofman.eap.samples.wsrm;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import org.apache.cxf.Bus;
import org.apache.cxf.bus.spring.SpringBusFactory;

/**
 * 
 */
@SuppressWarnings("serial")
@WebServlet("/ClientServlet")
public class ClientServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        SpringBusFactory busFactory = new SpringBusFactory();
        URL cxfConfig = getClass().getClassLoader().getResource("client-cxf.xml");
        Bus bus = busFactory.createBus(cxfConfig);
        SpringBusFactory.setDefaultBus(bus);

        QName serviceName = new QName("http://www.jboss.org/jbossas/quickstarts/wshelloworld/HelloWorld", "HelloWorldService");

        Service service = Service.create(new URL("http://localhost:8080/ws-rm/HelloWorldService?wsdl"), serviceName);
        HelloWorldService helloWorldService = service.getPort(HelloWorldService.class);
        assert (helloWorldService != null);
        String s = helloWorldService.sayHello();

        resp.setContentType("text/plain");
        PrintWriter writer = resp.getWriter();
        writer.println(s);
        writer.close();
    }

}
