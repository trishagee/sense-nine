package com.mechanitis.demo.sense.service;

import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.Session;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Flow;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.String.format;
import static java.util.concurrent.Executors.newSingleThreadExecutor;
import static java.util.logging.Logger.getLogger;

public class BroadcastingServerEndpoint extends Endpoint implements Flow.Subscriber<String> {
    private static final Logger LOGGER = getLogger(BroadcastingServerEndpoint.class.getName());
    private final ErrorCollector errorCollector = new ErrorCollector();
    private final List<Session> sessions = new CopyOnWriteArrayList<>();
    private final ExecutorService executor = newSingleThreadExecutor();
    private WebSocketServer webSocketServer;

    public BroadcastingServerEndpoint(String serviceEndpointPath, int servicePort) {
        try {
            webSocketServer = new WebSocketServer(serviceEndpointPath, servicePort, this);
            executor.submit(webSocketServer);
        } catch (Exception e) {
            // This is where you'd do much more sensible error handling
            LOGGER.severe(e::getMessage);
        }
    }

    @Override
    public void onOpen(Session session, EndpointConfig config) {
        sessions.add(session);
    }

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        subscription.request(Long.MAX_VALUE);
    }

    @Override
    public void onNext(String message) {
        LOGGER.fine(() -> "Endpoint received: " + message);
        sessions.stream()
                .filter(Session::isOpen)
                .forEach(session -> sendMessageToClient(message, session));
    }

    @Override
    public void onError(Throwable throwable) {
        final StackWalker stackWalker = StackWalker.getInstance();

        errorCollector.fullStackLength = stackWalker.walk(Stream::count);

        errorCollector.applicationClasses = stackWalker.walk(
                stackFrameStream -> stackFrameStream
                        .map(StackWalker.StackFrame::getClassName)
                        .filter(className -> className.contains("mechanitis"))
                        .collect(Collectors.toList())
        );
    }

    @Override
    public void onComplete() {
        close();
    }

    private void sendMessageToClient(String message, Session session) {
        try {
            LOGGER.finest(() -> format("BroadcastingServerEndpoint sending '%s' to session: [%s]",
                                       message, session.getId()));
            session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void close() {
        try {
            webSocketServer.stop();
        } catch (Exception e) {
            // This is where you'd do much more sensible error handling
            LOGGER.severe(e::getMessage);
        }
        executor.shutdownNow();
    }

    ErrorCollector getErrorCollector() {
        return errorCollector;
    }

    static class ErrorCollector {
        private Long fullStackLength;
        private List<String> applicationClasses;

        Long getFullStackLength() {
            return fullStackLength;
        }

        List<String> getApplicationClasses() {
            return applicationClasses;
        }

    }
}
