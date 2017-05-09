package com.mechanitis.demo.sense.service;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Flow;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import java.util.logging.Logger;

import static java.lang.String.format;
import static java.util.concurrent.TimeUnit.SECONDS;
import static java.util.logging.Logger.getLogger;

@javax.websocket.ClientEndpoint
public class ClientEndpoint implements Flow.Publisher<String> {
    private static final Logger LOGGER = getLogger(ClientEndpoint.class.getName());

    private final List<Subscription> subscriptions = new CopyOnWriteArrayList<>();
    private final URI serverEndpoint;
    private Session session;

    public ClientEndpoint(String serverEndpoint) {
        this.serverEndpoint = URI.create(serverEndpoint);
        connect();
    }

    @Override
    public void subscribe(Flow.Subscriber<? super String> subscriber) {
        Subscription subscription = new Subscription(subscriber);
        subscriber.onSubscribe(subscription);

        subscriptions.add(subscription);
    }

    @OnMessage
    public void onWebSocketText(String message) throws IOException {
        subscriptions.forEach(subscription -> subscription.onNext(message));
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

    private void close() {
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

    private class Subscription implements Flow.Subscription {
        private Flow.Subscriber<? super String> subscriber;
        private AtomicLong n = new AtomicLong(0);

        private Subscription(Flow.Subscriber<? super String> subscriber) {
            this.subscriber = subscriber;
        }

        @Override
        public void request(long n) {
            this.n.set(n);
        }

        @Override
        public void cancel() {
            this.n.set(0);
        }

        public void onNext(String message) {
            if (n.getAndDecrement() > 0) {
                subscriber.onNext(message);
            }
        }
    }
}
