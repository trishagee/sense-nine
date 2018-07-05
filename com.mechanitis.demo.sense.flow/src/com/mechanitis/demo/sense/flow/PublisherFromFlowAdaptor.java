package com.mechanitis.demo.sense.flow;

import org.reactivestreams.Publisher;

import java.util.concurrent.Flow;
import java.util.logging.Logger;

public class PublisherFromFlowAdaptor<T> implements Publisher<T> {
    private static final Logger LOGGER = Logger.getLogger(PublisherFromFlowAdaptor.class.getName());
    private final Flow.Publisher<T> delegate;

    private PublisherFromFlowAdaptor(Flow.Publisher<T> delegate) {
        this.delegate = delegate;
    }

    @Override
    public void subscribe(org.reactivestreams.Subscriber<? super T> s) {
        LOGGER.fine(() -> "PublisherFromFlowAdaptor subscribe s = [" + s + "]");
        delegate.subscribe(new SubscriberToFlowAdaptor<>(s));
    }

    public static <T> Publisher<T> adapt(Flow.Publisher<T> delegate) {
        return new PublisherFromFlowAdaptor<>(delegate);
    }

}
