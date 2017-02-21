package com.mechanitis.demo.sense.service;

import java.util.function.Supplier;

public class StubService implements Runnable {
    private final Supplier<String> messageGenerator;

    public StubService(String path, int port, Supplier<String> messageGenerator) {
        this.messageGenerator = messageGenerator;
    }

    @Override
    public void run() {
        System.out.printf("Stub Service: '%s'%n", messageGenerator.get());
        System.out.println("Service terminated");
    }
}
