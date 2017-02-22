module com.mechanitis.demo.sense.service {
    requires java.logging;

    requires javax.websocket.api;
    requires javax.websocket.server.impl;
    requires javax.websocket.client.impl;
    requires javax.servlet.api;

    requires websocket.common;
    requires websocket.client;
    requires websocket.api;

    requires jetty.server;
    requires jetty.servlet;
    requires jetty.util;
    requires jetty.io;
    requires jetty.client;
    requires jetty.http;

    exports com.mechanitis.demo.sense.service;
}