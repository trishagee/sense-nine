package com.mechanitis.demo.sense.service;

import java.util.concurrent.Flow;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class Service implements Runnable {
//    private static final Logger LOGGER = Logger.getLogger(Service.class.getName());

    private final Flow.Subscriber<String> subscriber;
    private final Flow.Publisher<String> publisher;
    private final BiConsumer<Flow.Publisher<String>, Flow.Subscriber<String>> businessLogic;

    public Service(String endpointToConnectTo, String serviceEndpointPath, int servicePort,
                   BiConsumer<Flow.Publisher<String>, Flow.Subscriber<String>> businessLogic) {
        this.businessLogic = businessLogic;
        subscriber = new BroadcastingServerEndpoint(serviceEndpointPath, servicePort);
        publisher = new ClientEndpoint(endpointToConnectTo);
    }

    public Service(String endpointToConnectTo, String serviceEndpointPath, int servicePort,
                   Function<String, String> mapper) {
        subscriber = new BroadcastingServerEndpoint(serviceEndpointPath, servicePort);
        publisher = new ClientEndpoint(endpointToConnectTo);
        Flow.Processor<String, String> processor = new StringMapperProcessor(mapper);
        businessLogic = (stringPublisher, stringSubscriber) -> {
            stringPublisher.subscribe(processor);
            processor.subscribe(stringSubscriber);
        };
    }

    @Override
    public void run() {
        businessLogic.accept(publisher, subscriber);
    }

}
