package com.mechanitis.demo.sense.service.flow;

import org.reactivestreams.Publisher;

import java.util.concurrent.Flow;

public class PublisherFromFlowAdaptor<T> implements Publisher<T> {
    private Flow.Publisher<T> delegate;

    private PublisherFromFlowAdaptor(Flow.Publisher<T> delegate) {
        this.delegate = delegate;
    }

    @Override
    public void subscribe(org.reactivestreams.Subscriber<? super T> s) {
        delegate.subscribe(new SubscriberToFlowAdaptor<>(s));
    }

    public static <T> Publisher<T> toPublisher(Flow.Publisher<T> delegate) {
        return new PublisherFromFlowAdaptor<T>(delegate);
    }

}
