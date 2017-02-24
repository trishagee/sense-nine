package com.mechanitis.demo.sense.service.flow;

import java.util.concurrent.Flow;

public interface Subscriber<T> extends Flow.Subscriber<T>, org.reactivestreams.Subscriber<T> {
    @Override
    default void onSubscribe(Flow.Subscription subscription) {
        onSubscribe(new Subscription(subscription));
    }

    @Override
    default void onSubscribe(org.reactivestreams.Subscription subscription) {
        onSubscribe(new Subscription(subscription));
    }

    void onSubscribe(Subscription subscription);

    class Subscription implements Flow.Subscription, org.reactivestreams.Subscription {
        private Flow.Subscription flowSubscription;
        private org.reactivestreams.Subscription reactiveStreamsSubscription;

        Subscription(Flow.Subscription flowSubscription) {
            this.flowSubscription = flowSubscription;
        }

        Subscription(org.reactivestreams.Subscription reactiveStreamsSubscription) {
            this.reactiveStreamsSubscription = reactiveStreamsSubscription;
        }

        @Override
        public void request(long n) {
            ifTrueCall(flowSubscription != null, () -> flowSubscription.request(n));
            ifTrueCall(reactiveStreamsSubscription != null, () -> reactiveStreamsSubscription.request(n));
        }

        @Override
        public void cancel() {
            ifTrueCall(flowSubscription != null, () -> flowSubscription.cancel());
            ifTrueCall(reactiveStreamsSubscription != null, () -> reactiveStreamsSubscription.cancel());
        }

        private void ifTrueCall(boolean criteria, Runnable callThis) {
            if (criteria) {
                callThis.run();
            }
        }

        @Override
        public String toString() {
            return "Subscription{" +
                   "flowSubscription=" + flowSubscription +
                   ", reactiveStreamsSubscription=" + reactiveStreamsSubscription +
                   '}';
        }
    }

}
