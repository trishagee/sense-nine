package com.mechanitis.demo.sense.service;

import java.util.concurrent.Flow;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class Service implements Runnable {
//    private static final Logger LOGGER = Logger.getLogger(Service.class.getName());

    private final ClientEndpoint publisher;
    private final BroadcastingServerEndpoint broadcastingServerEndpoint;
    private BiConsumer<Flow.Publisher<String>, Flow.Subscriber<String>> businessLogic;

    public Service(String endpointToConnectTo, String serviceEndpointPath, int servicePort,
                   BiConsumer<Flow.Publisher<String>, Flow.Subscriber<String>> businessLogic) {
        this.businessLogic = businessLogic;
        broadcastingServerEndpoint
                = new BroadcastingServerEndpoint(serviceEndpointPath, servicePort);
        publisher = new ClientEndpoint(endpointToConnectTo);
    }

    public Service(String endpointToConnectTo, String serviceEndpointPath, int servicePort,
                   Function<String, String> messageHandler) {
        broadcastingServerEndpoint
                = new BroadcastingServerEndpoint(serviceEndpointPath, servicePort);
        publisher = new ClientEndpoint(endpointToConnectTo);
    }

    @Override
    public void run() {
        businessLogic.accept(publisher, broadcastingServerEndpoint);
    }

}
