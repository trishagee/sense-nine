package com.mechanitis.demo.sense.twitter;

import jakarta.websocket.ClientEndpoint;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Logger;

import static java.util.logging.Logger.getLogger;

@ClientEndpoint
public class MessageReceivedEndpoint {
    private static final Logger LOGGER = getLogger(MessageReceivedEndpoint.class.getName());
    private final CountDownLatch latch;

    public MessageReceivedEndpoint(CountDownLatch latch) {
        this.latch = latch;
    }

    @OnOpen
    public void onWebSocketConnect(Session session) {
        LOGGER.fine(() -> "MessageReceivedEndpoint connected: " + session.hashCode());
    }

    @OnMessage
    public void onWebSocketText(String message) {
        latch.countDown();
        LOGGER.fine(() -> "Received TEXT message: " + message);
    }

    @OnError
    public void onError(Throwable throwable) {
        LOGGER.severe(throwable.getMessage());
        throwable.printStackTrace();
    }

}
