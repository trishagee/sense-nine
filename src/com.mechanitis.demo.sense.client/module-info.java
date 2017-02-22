module com.mechanitis.demo.sense.client {
    requires javafx.graphics;
    requires javafx.fxml;
    requires javafx.controls;
    requires javafx.base;

    exports com.mechanitis.demo.sense.client;
    opens com.mechanitis.demo.sense.client to javafx.fxml;
    opens com.mechanitis.demo.sense.client.mood to javafx.fxml;
    opens com.mechanitis.demo.sense.client.user to javafx.fxml;
}