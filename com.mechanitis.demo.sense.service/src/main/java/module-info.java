module com.mechanitis.demo.sense.service {
    requires java.logging;
    requires jetty.server;
    requires jetty.servlet;
    requires javax.websocket.api;
    requires javax.websocket.server.impl;

    exports com.mechanitis.demo.sense.service;
}