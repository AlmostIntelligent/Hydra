package org.gethydrated.hydra.http;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.gethydrated.hydra.api.service.ServiceActivator;
import org.gethydrated.hydra.api.service.ServiceContext;

/**
 * 
 * @author Chris
 *
 */
public class JettyActivator implements ServiceActivator {

    private Server server;

    private Handler rootHandler = new AbstractHandler() {
        @Override
        public void handle(final String target, final Request baseRequest,
                final HttpServletRequest request,
                final HttpServletResponse response) throws IOException,
                ServletException {
            handleRoot(target, baseRequest, request, response);
        }
    };

    private Map<String, Handler> handlers = new HashMap<>();

    @Override
    public void start(final ServiceContext context) throws Exception {
        server = new Server();
        final SelectChannelConnector connector = new SelectChannelConnector();
        connector.setReuseAddress(false);
        connector.setPort(8080);
        server.setConnectors(new Connector[] {connector});
        server.setHandler(new AbstractHandler() {
            @Override
            public void handle(final String target, final Request baseRequest,
                    final HttpServletRequest request,
                    final HttpServletResponse response) throws IOException,
                    ServletException {
                String tar = target;
                while (tar.startsWith("/")) {
                    tar = tar.substring(1);
                }
                String id = tar;
                if (!tar.isEmpty() && tar.contains("/")) {
                    id = tar.substring(0, tar.indexOf("/"));
                }
                if (!tar.contains("/")) {
                    rootHandler.handle(target, baseRequest, request, response);
                }
                final Handler handler = handlers.get(id);
                if (handler != null) {
                    handler.handle(target, baseRequest, request, response);
                }
            }
        });
        server.start();
    }

    @Override
    public void stop(final ServiceContext context) throws Exception {
        server.stop();
    }

    private void handleRoot(final String target, final Request baseRequest,
            final HttpServletRequest request, final HttpServletResponse response)
            throws IOException {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        baseRequest.setHandled(true);
        response.getWriter().println("<h1>Hydra web server</h1>");
        response.getWriter().println("<br>");
    }
}
