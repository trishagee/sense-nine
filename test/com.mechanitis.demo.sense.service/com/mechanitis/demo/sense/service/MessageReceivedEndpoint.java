package com.mechanitis.demo.sense.service;

import javax.websocket.ClientEndpoint;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Logger;

import static java.util.logging.Logger.getLogger;

@ClientEndpoint
public class MessageReceivedEndpoint {
    private static final Logger LOGGER = getLogger(MessageReceivedEndpoint.class.getName());
    private CountDownLatch latch;

    public MessageReceivedEndpoint(CountDownLatch latch) {
        this.latch = latch;
    }

    @OnOpen
    public void onWebSocketConnect(Session session) {
        LOGGER.fine(() -> "MessageReceivedEndpoint connected: " + session.hashCode());
    }

    @OnMessage
    public void onWebSocketText(String message) throws IOException {
        latch.countDown();
        LOGGER.fine(() -> "Received TEXT message: " + message);
    }

    @OnError
    public void onError(Throwable throwable) {
        LOGGER.severe(throwable.getMessage());
        throwable.printStackTrace();
    }

}
