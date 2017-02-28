package com.mechanitis.demo.sense.flow;

import java.util.concurrent.Flow;
import java.util.logging.Logger;

import static com.mechanitis.demo.sense.flow.SubscriptionAdaptor.toSubscription;
import static java.lang.String.format;

class SubscriberToFlowAdaptor<T> implements Flow.Subscriber<T> {
    private static final Logger LOGGER = Logger.getLogger(SubscriberToFlowAdaptor.class.getName());
    private final org.reactivestreams.Subscriber<T> delegate;

    SubscriberToFlowAdaptor(org.reactivestreams.Subscriber<T> delegate) {
        this.delegate = delegate;
        LOGGER.fine(() -> "delegate = [" + delegate + "]");
    }

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        LOGGER.fine(() -> "subscription = [" + subscription + "]");
        delegate.onSubscribe(toSubscription(subscription));
    }

    @Override
    public void onNext(T item) {
        LOGGER.finest(() -> format("item = %s: [%s]", item.getClass(), item));
        delegate.onNext(item);
    }

    @Override
    public void onError(Throwable throwable) {
        LOGGER.fine(() -> "throwable = [" + throwable + "]");
        delegate.onError(throwable);
    }

    @Override
    public void onComplete() {
        LOGGER.fine(() -> "onComplete");
        delegate.onComplete();
    }
}
