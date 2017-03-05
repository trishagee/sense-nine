package com.mechanitis.demo.sense.service;

import java.util.concurrent.Flow;
import java.util.function.BiConsumer;

public class Service implements Runnable {
//    private static final Logger LOGGER = Logger.getLogger(Service.class.getName());

    private final Flow.Publisher<String> clientEndpoint;
    private final Flow.Subscriber<String> broadcastingServerEndpoint;
    private final BiConsumer<Flow.Publisher<String>, Flow.Subscriber<String>> businessLogic;

    public Service(String endpointToConnectTo, String serviceEndpointPath, int servicePort,
                   BiConsumer<Flow.Publisher<String>, Flow.Subscriber<String>> businessLogic) {
        this.businessLogic = businessLogic;
        broadcastingServerEndpoint
                = new BroadcastingServerEndpoint(serviceEndpointPath, servicePort);
        clientEndpoint = new ClientEndpoint(endpointToConnectTo);
    }

    public Service(String endpointToConnectTo, String serviceEndpointPath, int servicePort) {
        this(endpointToConnectTo, serviceEndpointPath, servicePort,
                Flow.Publisher::subscribe);
    }

    @Override
    public void run() {
        businessLogic.accept(clientEndpoint, broadcastingServerEndpoint);
    }

}
