module com.mechanitis.demo.sense.client {
    requires com.mechanitis.demo.sense.service;

    requires java.logging;

    requires javafx.graphics;
    requires javafx.fxml;
    requires javafx.controls;
    requires javafx.base;

    exports com.mechanitis.demo.sense.client;
    //not sure this is what I want
    exports com.mechanitis.demo.sense.client.user;
    exports com.mechanitis.demo.sense.client.mood;

    opens com.mechanitis.demo.sense.client to javafx.fxml;
    opens com.mechanitis.demo.sense.client.mood to javafx.fxml;
    opens com.mechanitis.demo.sense.client.user to javafx.fxml;
}