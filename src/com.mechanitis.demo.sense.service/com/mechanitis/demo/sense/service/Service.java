package com.mechanitis.demo.sense.service;

import java.util.function.Function;

public class Service implements Runnable {
//    private static final Logger LOGGER = Logger.getLogger(Service.class.getName());

    private final ClientEndpoint clientEndpoint;
    private final BroadcastingServerEndpoint broadcastingServerEndpoint;

    public Service(String endpointToConnectTo, String serviceEndpointPath, int servicePort,
                   Function<String, String> messageHandler) {
        broadcastingServerEndpoint
                = new BroadcastingServerEndpoint(serviceEndpointPath, servicePort);
        clientEndpoint = new ClientEndpoint(endpointToConnectTo, messageHandler);
    }

    @Override
    public void run() {
        clientEndpoint.subscribe(broadcastingServerEndpoint);
    }

}
