module com.mechanitis.demo.sense.service {
    requires java.logging;
    requires org.eclipse.jetty.server;
    requires org.eclipse.jetty.servlet;
    requires org.eclipse.jetty.websocket.javax.websocket.server;
    requires transitive javax.websocket.api;

    exports com.mechanitis.demo.sense.service;
}