package com.mechanitis.demo.sense.service;

import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Flow;
import java.util.logging.Logger;

import static java.lang.String.format;
import static java.util.concurrent.TimeUnit.SECONDS;

@javax.websocket.ClientEndpoint
public class ClientEndpoint implements Flow.Publisher<String> {
    private static final Logger LOGGER = Logger.getLogger(ClientEndpoint.class.getName());

    private final List<Flow.Subscriber<? super String>> subscribers = new CopyOnWriteArrayList<>();
    private final URI serverEndpoint;
    private Session session;

    public ClientEndpoint(String serverEndpoint) {
        this.serverEndpoint = URI.create(serverEndpoint);
    }

    @Override
    public void subscribe(Flow.Subscriber<? super String> subscriber) {
        subscribers.add(subscriber);
    }

    @OnMessage
    public void onWebSocketText(String message) throws IOException {
        subscribers.forEach(subscriber -> subscriber.onNext(message));
    }

    @OnError
    public void onError(Throwable error) {
        LOGGER.warning("Error received: " + error.getMessage());
        close();
        naiveReconnectRetry();
    }

    @OnClose
    public void onClose() {
        LOGGER.warning(() -> format("Session to %s closed, retrying...", serverEndpoint));
        naiveReconnectRetry();
    }

    //    @OnOpen
    public void connect() {
        LOGGER.fine(() -> format("Connecting to %s", serverEndpoint));
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        try {
            session = container.connectToServer(this, serverEndpoint);
            LOGGER.info(() -> "Connected to: " + serverEndpoint);
        } catch (DeploymentException | IOException e) {
            LOGGER.warning(() -> format("Error connecting to %s: %s", serverEndpoint, e.getMessage()));
        }
    }

    void close() {
        if (session != null) {
            try {
                session.close();
            } catch (IOException e) {
                LOGGER.warning(format("Error closing session: %s", e.getMessage()));
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
