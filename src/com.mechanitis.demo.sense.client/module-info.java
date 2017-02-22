module com.mechanitis.demo.sense.client {
    requires com.mechanitis.demo.sense.service;

    requires java.logging;

    requires javafx.graphics;
    requires javafx.fxml;
    requires javafx.controls;
    requires javafx.base;

    /* Websocket dependencies */
    requires javax.websocket.api;
    requires javax.websocket.client.impl;

    requires websocket.common;
    requires websocket.client;
    requires websocket.api;

    requires jetty.util;
    requires jetty.io;
    requires jetty.client;
    requires jetty.http;

    exports com.mechanitis.demo.sense.client;
    //not sure this is what I want
    exports com.mechanitis.demo.sense.client.user;

    opens com.mechanitis.demo.sense.client to javafx.fxml;
    opens com.mechanitis.demo.sense.client.mood to javafx.fxml;
    opens com.mechanitis.demo.sense.client.user to javafx.fxml;
}