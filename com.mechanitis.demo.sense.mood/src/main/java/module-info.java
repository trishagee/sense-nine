module com.mechanitis.demo.sense.mood {
    requires com.mechanitis.demo.sense.service;
    requires reactor.core;
    requires org.reactivestreams;

    // these should be marked unused in some version of IntelliJ IDEA
    exports com.mechanitis.demo.sense.mood;
    opens com.mechanitis.demo.sense.mood;
}