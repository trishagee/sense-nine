package com.mechanitis.demo.sense.service.flow;

import java.util.concurrent.Flow;

import static com.mechanitis.demo.sense.service.flow.SubscriptionAdaptor.toSubscription;

public class SubscriberToFlowAdaptor<T> implements Flow.Subscriber<T> {
    private org.reactivestreams.Subscriber<T> delegate;

    SubscriberToFlowAdaptor(org.reactivestreams.Subscriber<T> delegate) {
        this.delegate = delegate;
    }

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        delegate.onSubscribe(toSubscription(subscription));
    }

    @Override
    public void onNext(T item) {
        delegate.onNext(item);

    }

    @Override
    public void onError(Throwable throwable) {
        delegate.onError(throwable);
    }

    @Override
    public void onComplete() {
        delegate.onComplete();
    }
}
