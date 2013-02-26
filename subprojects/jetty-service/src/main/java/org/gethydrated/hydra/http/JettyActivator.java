package org.gethydrated.hydra.http;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.gethydrated.hydra.api.service.ServiceActivator;
import org.gethydrated.hydra.api.service.ServiceContext;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JettyActivator implements ServiceActivator {

    Server server;

    @Override
    public void start(ServiceContext context) throws Exception {
        server = new Server();
        SelectChannelConnector connector = new SelectChannelConnector();
        connector.setReuseAddress(false);
        connector.setPort(8080);
        server.setConnectors(new Connector[]{connector});
        server.setHandler(new AbstractHandler() {
            @Override
            public void handle(String target, Request baseRequest, HttpServletRequest request,
                               HttpServletResponse response) throws IOException, ServletException {
                response.setContentType("text/html;charset=utf-8");
                response.setStatus(HttpServletResponse.SC_OK);
                baseRequest.setHandled(true);
                response.getWriter().println("<h1>Hello World</h1>");
            }
        });
        server.start();
    }

    @Override
    public void stop(ServiceContext context) throws Exception {
        server.stop();
    }
}
