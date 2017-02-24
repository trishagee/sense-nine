package com.mechanitis.demo.sense.service;

import javax.websocket.DeploymentException;
import java.io.IOException;
import java.util.concurrent.Flow;
import java.util.logging.Logger;

import static java.util.logging.Level.FINE;

public class Service implements Runnable {
    private static final Logger LOGGER = Logger.getLogger(Service.class.getName());

    private final String serviceEndpointPath;
    private final int servicePort;

    private WebSocketServer webSocketServer;
    private ClientEndpoint clientEndpoint;
    private BroadcastingServerEndpoint<String> broadcastingServerEndpoint;

    public Service(String endpointToConnectTo, String servicePath, int servicePort) {
        this.serviceEndpointPath = servicePath;
        this.servicePort = servicePort;
        clientEndpoint = new ClientEndpoint(endpointToConnectTo);
        broadcastingServerEndpoint = new BroadcastingServerEndpoint<>();
    }

    @Override
    public void run() {
        LOGGER.setLevel(FINE);
        try {
            clientEndpoint.connect();

            // run the Jetty server for the server endpoint that clients will connect to. Tne endpoint simply informs
            // all listeners of all messages
            webSocketServer = new WebSocketServer(serviceEndpointPath, servicePort, broadcastingServerEndpoint);
            webSocketServer.run();
        } catch (Exception e) {
            // This is where you'd do much more sensible error handling
            LOGGER.severe(e.getMessage());
        }
    }

    public void stop() throws Exception {
        clientEndpoint.close();
        webSocketServer.stop();
    }

    public static void main(String[] args) throws IOException, DeploymentException {
        new Service("ws://localhost:8081/tweets/", "/testing/", 8090).run();
    }

    public Flow.Publisher<String> getPublisher() {
        return clientEndpoint;
    }

    public Flow.Subscriber<String> getSubscriber() {
        return broadcastingServerEndpoint;
    }
}
