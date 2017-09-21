package com.mechanitis.demo.sense.service;

import javax.websocket.DeploymentException;
import java.io.IOException;
import java.util.function.Function;

public class Service implements Runnable {
//    private static final Logger LOGGER = Logger.getLogger(Service.class.getName());

    private ClientEndpoint clientEndpoint;
    private BroadcastingServerEndpoint broadcastingServerEndpoint;

    public Service(String endpointToConnectTo, String serviceEndpointPath, int servicePort,
                   Function<String, String> messageHandler) {
        broadcastingServerEndpoint
                = new BroadcastingServerEndpoint(serviceEndpointPath, servicePort);
        clientEndpoint = new ClientEndpoint(endpointToConnectTo, messageHandler);
        clientEndpoint.addListener(broadcastingServerEndpoint);
    }

    @Override
    public void run() {
    }

    public void stop() throws Exception {
        clientEndpoint.close();
        broadcastingServerEndpoint.close();
    }

    public static void main(String[] args) throws IOException, DeploymentException {
        new Service("ws://localhost:8081/tweets/", "/testing/", 8090,
                originalText -> originalText).run();
    }
}
