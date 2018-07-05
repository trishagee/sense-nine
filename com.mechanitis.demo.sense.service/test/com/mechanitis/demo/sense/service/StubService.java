package com.mechanitis.demo.sense.service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledFuture;
import java.util.function.Supplier;

import static java.util.concurrent.Executors.newScheduledThreadPool;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class StubService implements Runnable {
    private final BroadcastingServerEndpoint serverEndpoint;
    private final Supplier<String> messageGenerator;

    public StubService(String path, int port, Supplier<String> messageGenerator) {
        serverEndpoint = new BroadcastingServerEndpoint(path, port);
        this.messageGenerator = messageGenerator;
    }

    @Override
    public void run() {
        // periodically call the message generator
        ScheduledFuture<?> scheduledFuture = newScheduledThreadPool(1).scheduleAtFixedRate(
                () -> serverEndpoint.onNext(messageGenerator.get()),
                0, 500, MILLISECONDS);
        try {
            // sit around and run the message generator for eternity
            scheduledFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

}
