package com.mechanitis.demo.sense.service;

import java.util.concurrent.Flow;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public class Service implements Runnable {
//    private static final Logger LOGGER = Logger.getLogger(Service.class.getName());

    private final Flow.Subscriber<String> subscriber;
    private final Flow.Publisher<String> publisher;

    public Service(String endpointToConnectTo, String serviceEndpointPath, int servicePort,
                   UnaryOperator<Flow.Publisher<String>> publisherFactory) {
        this.publisher = publisherFactory.apply(new ClientEndpoint(endpointToConnectTo));
        subscriber = new BroadcastingServerEndpoint(serviceEndpointPath, servicePort);
    }

    public Service(String endpointToConnectTo, String serviceEndpointPath, int servicePort,
                   Function<String, String> mapper) {
        subscriber = new BroadcastingServerEndpoint(serviceEndpointPath, servicePort);
        Flow.Processor<String, String> processor = new StringMapperProcessor(mapper);
        new ClientEndpoint(endpointToConnectTo).subscribe(processor);
        this.publisher = processor;
    }

    @Override
    public void run() {
        publisher.subscribe(subscriber);
    }

}
