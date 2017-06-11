module com.mechanitis.demo.sense.client {
    requires com.mechanitis.demo.sense.service;

    requires java.logging;
    requires javafx.base;
    requires javafx.controls;
    requires javafx.fxml;

    exports com.mechanitis.demo.sense.client to javafx.graphics;

    opens com.mechanitis.demo.sense.client to javafx.fxml;
    opens com.mechanitis.demo.sense.client.mood to javafx.fxml;
    opens com.mechanitis.demo.sense.client.user to javafx.fxml, javafx.base;
}