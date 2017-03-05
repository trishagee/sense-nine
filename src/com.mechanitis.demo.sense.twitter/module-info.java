module com.mechanitis.demo.sense.twitter {
    requires com.mechanitis.demo.sense.service;
    requires com.mechanitis.demo.sense.flow;

    requires java.logging;

    requires jetty.http;
    requires rxjava;


    exports com.mechanitis.demo.sense.twitter;
}
