package com.mechanitis.demo.sense.flow;

import org.reactivestreams.Publisher;

import java.util.concurrent.Flow;
import java.util.logging.Logger;

public class PublisherToFlowAdaptor<T> implements Flow.Publisher<T> {
    private static final Logger LOGGER = Logger.getLogger(PublisherToFlowAdaptor.class.getName());
    private final Publisher<T> delegate;

    private PublisherToFlowAdaptor(Publisher<T> delegate) {
        this.delegate = delegate;
    }

    @Override
    public void subscribe(Flow.Subscriber<? super T> s) {
        LOGGER.fine(() -> "PublisherToFlowAdaptor subscribe s = [" + s + "]");
        delegate.subscribe(SubscriberFromFlowAdaptor.adapt(s));
    }

    public static <T> Flow.Publisher<T> adapt(Publisher<T> delegate) {
        return new PublisherToFlowAdaptor<>(delegate);
    }

}
