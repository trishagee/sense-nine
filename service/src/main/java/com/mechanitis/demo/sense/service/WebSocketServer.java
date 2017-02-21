package com.mechanitis.demo.sense.service;

import java.util.logging.Logger;

import static java.lang.String.format;
import static java.util.logging.Logger.getLogger;

/**
 * Starts a Jetty server designed to work with websockets.
 *
 * This server will only create a singleton instance of any endpoint.
 */
public class WebSocketServer implements Runnable {
    private static final Logger LOGGER = getLogger(WebSocketServer.class.getName());

    private final int port;
    private final String path;

    WebSocketServer(String path, int port) {
        this.path = path;
        this.port = port;
    }

    public void run() {
        LOGGER.info(() -> format("Starting new Web Socket Service: %s at port %d", path, port));
    }


}

