module com.mechanitis.demo.sense.service {
    exports com.mechanitis.demo.sense.service;
    requires java.logging;

    requires javax.websocket.api;
    requires javax.websocket.server.impl;
    requires javax.websocket.client.impl;
    requires javax.servlet.api;

    requires jetty.server;
    requires jetty.servlet;
    requires jetty.util;

    requires websocket.common;
}