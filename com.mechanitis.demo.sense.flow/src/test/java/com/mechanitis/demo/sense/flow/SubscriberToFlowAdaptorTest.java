package com.mechanitis.demo.sense.flow;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.concurrent.Flow;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SuppressWarnings("unchecked")
class SubscriberToFlowAdaptorTest {

    @Test
    @DisplayName("should wrap Reactive Streams Subscriber as Flow.Subscriber")
    void shouldWrapReactiveStreamsSubscriber() {
        // given:
        Subscriber<String> reactiveSubscriber = mock(Subscriber.class);
        Flow.Subscriber<String> adaptedSubscriber = new SubscriberToFlowAdaptor<>(reactiveSubscriber);

        Flow.Subscription flowSubscription = mock(Flow.Subscription.class);

        // when:
        adaptedSubscriber.onSubscribe(flowSubscription);

        // then:
        verify(reactiveSubscriber).onSubscribe(any(Subscription.class));
    }

    @Test
    @DisplayName("should delegate onNext calls")
    void shouldDelegateOnNext() {
        // given:
        Subscriber<String> reactiveSubscriber = mock(Subscriber.class);
        Flow.Subscriber<String> adaptedSubscriber = new SubscriberToFlowAdaptor<>(reactiveSubscriber);

        // when:
        adaptedSubscriber.onNext("test message");

        // then:
        verify(reactiveSubscriber).onNext("test message");
    }

    @Test
    @DisplayName("should delegate onError calls")
    void shouldDelegateOnError() {
        // given:
        Subscriber<String> reactiveSubscriber = mock(Subscriber.class);
        Flow.Subscriber<String> adaptedSubscriber = new SubscriberToFlowAdaptor<>(reactiveSubscriber);

        Throwable testException = new RuntimeException("test error");

        // when:
        adaptedSubscriber.onError(testException);

        // then:
        verify(reactiveSubscriber).onError(testException);
    }

    @Test
    @DisplayName("should delegate onComplete calls")
    void shouldDelegateOnComplete() {
        // given:
        Subscriber<String> reactiveSubscriber = mock(Subscriber.class);
        Flow.Subscriber<String> adaptedSubscriber = new SubscriberToFlowAdaptor<>(reactiveSubscriber);

        // when:
        adaptedSubscriber.onComplete();

        // then:
        verify(reactiveSubscriber).onComplete();
    }

    @Test
    @DisplayName("should adapt subscription in onSubscribe")
    void shouldAdaptSubscription() {
        // given:
        Subscriber<String> reactiveSubscriber = mock(Subscriber.class);
        Flow.Subscriber<String> adaptedSubscriber = new SubscriberToFlowAdaptor<>(reactiveSubscriber);

        Flow.Subscription flowSubscription = mock(Flow.Subscription.class);

        // when:
        adaptedSubscriber.onSubscribe(flowSubscription);

        // then:
        verify(reactiveSubscriber).onSubscribe(any(Subscription.class));
    }
}
