package com.mechanitis.demo.sense.service;

import com.mechanitis.demo.sense.service.config.SingletonEndpointConfigurator;
import jakarta.websocket.Endpoint;
import jakarta.websocket.server.ServerEndpointConfig;
import java.util.logging.Logger;

import static java.lang.String.format;
import static java.util.logging.Logger.getLogger;

/**
 * Starts a Jetty server designed to work with websockets.
 *
 * This server will only create a singleton instance of any endpoint.
 */
public class WebSocketServer implements Runnable {
    private static final Logger LOGGER = getLogger(WebSocketServer.class.getName());

    private final int port;
    private final String path;
    private final Endpoint endpoint;
//    private final Server server;

    WebSocketServer(String path, int port, Endpoint endpoint) {
        this.path = path;
        this.port = port;
        this.endpoint = endpoint;
//        server = new Server();
    }

    public void run() {
        LOGGER.info(() -> format("Starting new Web Socket Service: %s at port %d", path, port));
//        ServletContextHandler context = initialiseJettyServer(port);
//        try {
//            // create a configuration to ensure
//            // a) there's only a single instance of the Endpoint and
//            // b) set the correct URI
//            SingletonEndpointConfigurator serverEndpointConfigurator = new SingletonEndpointConfigurator(endpoint);
//            ServerEndpointConfig config = ServerEndpointConfig.Builder.create(endpoint.getClass(), path)
//                                                                      .configurator(serverEndpointConfigurator)
//                                                                      .build();
//            WebSocketServerContainerInitializer.configureContext(context).addEndpoint(config);
//
//            server.start();
//            server.join();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    void stop() throws Exception {
//        server.stop();
//        server.join();
    }

//    private ServletContextHandler initialiseJettyServer(int port) {
//        ServerConnector connector = new ServerConnector(server);
//        connector.setPort(port);
//        server.addConnector(connector);
//
//        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
//        context.setContextPath("/");
//        server.setHandler(context);
//        return context;
//    }

}

