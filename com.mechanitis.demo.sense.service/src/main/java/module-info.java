module com.mechanitis.demo.sense.service {
    requires java.logging;
    requires jetty.server;
    requires jetty.servlet;
    requires javax.websocket.server.impl;
    requires javax.websocket.api;

    exports com.mechanitis.demo.sense.service;
    exports com.mechanitis.demo.sense.service.internal;
}