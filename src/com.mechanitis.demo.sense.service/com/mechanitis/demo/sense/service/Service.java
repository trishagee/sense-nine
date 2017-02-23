package com.mechanitis.demo.sense.service;

import javax.websocket.DeploymentException;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Flow;
import java.util.function.Function;
import java.util.logging.Logger;

import static java.util.logging.Level.FINE;

public class Service implements Runnable {
    private static final Logger LOGGER = Logger.getLogger(Service.class.getName());

    private final String endpointToConnectTo;
    private final String serviceEndpointPath;
    private final int servicePort;

    private WebSocketServer webSocketServer;
    private ClientEndpoint clientEndpoint;
    private List<Flow.Processor<String, String>> processors = new CopyOnWriteArrayList<>();

    public Service(String endpointToConnectTo, String servicePath, int servicePort) {
        this.endpointToConnectTo = endpointToConnectTo;
        this.serviceEndpointPath = servicePath;
        this.servicePort = servicePort;
    }

    @Override
    public void run() {
        LOGGER.setLevel(FINE);
        try {
            BroadcastingServerEndpoint<String> broadcastingServerEndpoint = new BroadcastingServerEndpoint<>();
            clientEndpoint = new ClientEndpoint(endpointToConnectTo);

            // wire up the processors to subscribe to the ClientEndpoint and publish to the server endpoint
            processors.forEach(processor -> {
                clientEndpoint.subscribe(processor);
                processor.subscribe(broadcastingServerEndpoint);
            });

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

    public void addProcessor(Function<String, String> function) {
        processors.add(new ServiceProcessor(function));
    }

    public static void main(String[] args) throws IOException, DeploymentException {
        new Service("ws://localhost:8081/tweets/", "/testing/", 8090).run();
    }
}
