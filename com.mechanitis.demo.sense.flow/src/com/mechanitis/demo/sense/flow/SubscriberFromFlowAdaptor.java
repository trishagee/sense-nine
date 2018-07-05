package com.mechanitis.demo.sense.flow;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.concurrent.Flow;

import static com.mechanitis.demo.sense.flow.SubscriptionAdaptor.toFlowSubscription;

public class SubscriberFromFlowAdaptor<T> implements org.reactivestreams.Subscriber<T> {
    private final Flow.Subscriber<T> delegate;

    private SubscriberFromFlowAdaptor(Flow.Subscriber<T> delegate) {
        this.delegate = delegate;
    }

    @Override
    public void onSubscribe(Subscription subscription) {
        delegate.onSubscribe(toFlowSubscription(subscription));
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

    public static <T>Subscriber<T> adapt(Flow.Subscriber<T> subscriber) {
        return new SubscriberFromFlowAdaptor<>(subscriber);
    }
}
