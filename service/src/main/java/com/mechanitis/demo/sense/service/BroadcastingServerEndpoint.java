package com.mechanitis.demo.sense.service;

import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.Session;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Flow;
import java.util.logging.Logger;

public class BroadcastingServerEndpoint<T> extends Endpoint implements Flow.Subscriber<T> {
    private static final Logger LOGGER = Logger.getLogger(BroadcastingServerEndpoint.class.getName());
    private final List<Session> sessions = new CopyOnWriteArrayList<>();

    @Override
    public void onOpen(Session session, EndpointConfig config) {
        sessions.add(session);
    }

    @Override
    public void onNext(T message) {
        LOGGER.info(() -> "Endpoint received: " + message);
        // TODO: do something with the message
    }

    @Override
    public void onSubscribe(Flow.Subscription subscription) {

    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void onComplete() {

    }
}
