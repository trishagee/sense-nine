package com.mechanitis.demo.sense.flow;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.concurrent.Flow;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SuppressWarnings("unchecked")
class SubscriberFromFlowAdaptorTest {

    @Test
    @DisplayName("should adapt Flow.Subscriber to Reactive Streams Subscriber")
    void shouldAdaptFlowSubscriber() {
        // given:
        Flow.Subscriber<String> flowSubscriber = mock(Flow.Subscriber.class);
        Subscriber<String> adaptedSubscriber = SubscriberFromFlowAdaptor.adapt(flowSubscriber);

        Subscription subscription = mock(Subscription.class);

        // when:
        adaptedSubscriber.onSubscribe(subscription);

        // then:
        verify(flowSubscriber).onSubscribe(any(Flow.Subscription.class));
    }

    @Test
    @DisplayName("should delegate onNext calls")
    void shouldDelegateOnNext() {
        // given:
        Flow.Subscriber<String> flowSubscriber = mock(Flow.Subscriber.class);
        Subscriber<String> adaptedSubscriber = SubscriberFromFlowAdaptor.adapt(flowSubscriber);

        // when:
        adaptedSubscriber.onNext("test message");

        // then:
        verify(flowSubscriber).onNext("test message");
    }

    @Test
    @DisplayName("should delegate onError calls")
    void shouldDelegateOnError() {
        // given:
        Flow.Subscriber<String> flowSubscriber = mock(Flow.Subscriber.class);
        Subscriber<String> adaptedSubscriber = SubscriberFromFlowAdaptor.adapt(flowSubscriber);

        Throwable testException = new RuntimeException("test error");

        // when:
        adaptedSubscriber.onError(testException);

        // then:
        verify(flowSubscriber).onError(testException);
    }

    @Test
    @DisplayName("should delegate onComplete calls")
    void shouldDelegateOnComplete() {
        // given:
        Flow.Subscriber<String> flowSubscriber = mock(Flow.Subscriber.class);
        Subscriber<String> adaptedSubscriber = SubscriberFromFlowAdaptor.adapt(flowSubscriber);

        // when:
        adaptedSubscriber.onComplete();

        // then:
        verify(flowSubscriber).onComplete();
    }

    @Test
    @DisplayName("should adapt subscription in onSubscribe")
    void shouldAdaptSubscription() {
        // given:
        Flow.Subscriber<String> flowSubscriber = mock(Flow.Subscriber.class);
        Subscriber<String> adaptedSubscriber = SubscriberFromFlowAdaptor.adapt(flowSubscriber);

        Subscription reactiveSubscription = mock(Subscription.class);

        // when:
        adaptedSubscriber.onSubscribe(reactiveSubscription);

        // then:
        verify(flowSubscriber).onSubscribe(any(Flow.Subscription.class));
    }
}
