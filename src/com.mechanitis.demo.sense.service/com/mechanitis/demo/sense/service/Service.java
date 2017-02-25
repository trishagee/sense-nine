package com.mechanitis.demo.sense.service;

import javax.websocket.DeploymentException;
import java.io.IOException;
import java.util.concurrent.Flow;
import java.util.logging.Logger;

import static java.util.logging.Level.FINE;

public class Service implements Runnable {
    private static final Logger LOGGER = Logger.getLogger(Service.class.getName());

    private ClientEndpoint clientEndpoint;
    private BusinessLogic businessLogic;
    private BroadcastingServerEndpoint<String> broadcastingServerEndpoint;

    private Service(String endpointToConnectTo, String servicePath, int servicePort) {
        this(endpointToConnectTo, servicePath, servicePort, Flow.Publisher::subscribe);
    }

    public Service(String endpointToConnectTo, String servicePath, int servicePort, BusinessLogic businessLogic) {
        clientEndpoint = new ClientEndpoint(endpointToConnectTo);
        this.businessLogic = businessLogic;
        broadcastingServerEndpoint = new BroadcastingServerEndpoint<>(servicePath, servicePort);
    }

    @Override
    public void run() {
        LOGGER.setLevel(FINE);
        businessLogic.doTheThing(clientEndpoint, broadcastingServerEndpoint);
    }

    public void stop() throws Exception {
        clientEndpoint.close();
        broadcastingServerEndpoint.close();
    }

    public static void main(String[] args) throws IOException, DeploymentException {
        new Service("ws://localhost:8081/tweets/", "/testing/", 8090).run();
    }

    @FunctionalInterface
    public interface  BusinessLogic {
        void doTheThing(Flow.Publisher<String> publisher, Flow.Subscriber<String> subscriber);
    }
}
