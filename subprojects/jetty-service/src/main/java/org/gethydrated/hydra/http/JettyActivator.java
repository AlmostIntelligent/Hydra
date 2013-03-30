package org.gethydrated.hydra.http;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.gethydrated.hydra.api.HydraException;
import org.gethydrated.hydra.api.service.SID;
import org.gethydrated.hydra.api.service.ServiceActivator;
import org.gethydrated.hydra.api.service.ServiceContext;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class JettyActivator implements ServiceActivator {

    Server server;

    Handler rootHandler = new AbstractHandler() {
        @Override
        public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
            handleRoot(target, baseRequest, request, response);
        }
    };

    Map<String, Handler> handlers = new HashMap<>();

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
                String tar = target;
                while (tar.startsWith("/")) {
                    tar = tar.substring(1);
                }
                String id = tar;
                if(!tar.isEmpty() && tar.contains("/")) {
                    id = tar.substring(0, tar.indexOf("/"));
                }
                if(!tar.contains("/")) {
                    rootHandler.handle(target, baseRequest, request, response);
                }
                Handler handler = handlers.get(id);
                if(handler != null) {
                    handler.handle(target, baseRequest, request, response);
                }
            }
        });
        server.start();
        publishSelf(context);
    }

    @Override
    public void stop(ServiceContext context) throws Exception {
        server.stop();
    }

    @SuppressWarnings("unchecked")
    private void publishSelf(ServiceContext context) throws Exception {
        SID broker;
        try {
            broker = context.getGlobalService("http-broker");
        } catch (HydraException e) {
            broker = context.startService("http::broker");
        }
        broker.tell("http", context.getSelf());
        //context.monitor(broker);
    }

    private void handleRoot(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        baseRequest.setHandled(true);
        response.getWriter().println("<h1>Hydra web server</h1>");
        response.getWriter().println("<br>");
    }
}
