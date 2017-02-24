module com.mechanitis.demo.sense.service {
    requires java.logging;

    requires javax.websocket.api;
    requires javax.websocket.server.impl;
    requires javax.websocket.client.impl;
    requires javax.servlet.api;

    requires websocket.common;
    requires websocket.client;
    requires websocket.server;
    requires websocket.servlet;
    requires websocket.api;

    requires jetty.server;
    requires jetty.servlet;
    requires jetty.util;
    requires jetty.io;
    requires jetty.client;
    requires jetty.http;
    requires jetty.security;

    //eventually remove this when RxJava supports j.u.c.Flow
    requires reactive.streams;
    exports com.mechanitis.demo.sense.service.flow;

    exports com.mechanitis.demo.sense.service;
}