package com.mechanitis.demo.sense.service;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Function;
import java.util.logging.Logger;

import static java.lang.String.format;
import static java.util.concurrent.TimeUnit.SECONDS;
import static java.util.logging.Logger.getLogger;

@javax.websocket.ClientEndpoint
public class ClientEndpoint {
    private static final Logger LOGGER = getLogger(ClientEndpoint.class.getName());

    private final List<MessageListener> listeners = new CopyOnWriteArrayList<>();
    private final URI serverEndpoint;
    private final Function<String, String> messageHandler;
    private Session session;

    public ClientEndpoint(String serverEndpoint, Function<String, String> messageHandler) {
        this.serverEndpoint = URI.create(serverEndpoint);
        this.messageHandler = messageHandler;
        connect();
    }

    public void addListener(MessageListener listener) {
        listeners.add(listener);
    }

    @OnMessage
    public void onWebSocketText(String input) throws IOException {
        String output = messageHandler.apply(input);
        listeners.forEach(messageListener -> messageListener.onMessage(output));
    }

    @OnError
    public void onError(Throwable error) {
        LOGGER.warning(() -> "Error received: " + error.getMessage());
        close();
        naiveReconnectRetry();
    }

    @OnClose
    public void onClose() {
        LOGGER.warning(() -> format("Session to %s closed, retrying...", serverEndpoint));
        naiveReconnectRetry();
    }

    private void connect() {
        LOGGER.fine(() -> format("Connecting to %s", serverEndpoint));
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        try {
            session = container.connectToServer(this, serverEndpoint);
            LOGGER.info(() -> "Connected to: " + serverEndpoint);
        } catch (DeploymentException | IOException e) {
            LOGGER.warning(() -> format("Error connecting to %s: %s",
                            serverEndpoint, e.getMessage()));
        }
    }

    void close() {
        if (session != null) {
            try {
                session.close();
            } catch (IOException e) {
                LOGGER.warning(() -> format("Error closing session: %s", e.getMessage()));
            }
        }
    }

    private void naiveReconnectRetry() {
        try {
            SECONDS.sleep(5);
            connect();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
