module com.mechanitis.demo.sense.twitter {
    requires java.logging;
    requires com.mechanitis.demo.sense.service;

    requires websocket.servlet;
    requires websocket.server;

    requires jetty.http;
    requires jetty.security;

    requires rxjava;
    requires reactive.streams;
    requires com.mechanitis.demo.sense.flow;

    exports com.mechanitis.demo.sense.twitter;
}