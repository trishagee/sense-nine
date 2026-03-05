package com.mechanitis.demo.sense.flow;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.concurrent.Flow;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SuppressWarnings("unchecked")
class PublisherFromFlowAdaptorTest {

    @Test
    @DisplayName("should adapt Flow.Publisher to Reactive Streams Publisher")
    void shouldAdaptFlowPublisher() {
        // given:
        Flow.Publisher<String> flowPublisher = mock(Flow.Publisher.class);
        org.reactivestreams.Publisher<String> adaptedPublisher = PublisherFromFlowAdaptor.adapt(flowPublisher);

        Subscriber<String> reactiveSubscriber = mock(Subscriber.class);

        // when:
        adaptedPublisher.subscribe(reactiveSubscriber);

        // then:
        verify(flowPublisher).subscribe(any(Flow.Subscriber.class));
    }

    @Test
    @DisplayName("should wrap subscriber when subscribing")
    void shouldWrapSubscriber() {
        // given:
        Flow.Publisher<String> flowPublisher = s -> {
            s.onSubscribe(mock(Flow.Subscription.class));
            s.onNext("test");
            s.onComplete();
        };

        org.reactivestreams.Publisher<String> adaptedPublisher = PublisherFromFlowAdaptor.adapt(flowPublisher);
        Subscriber<String> reactiveSubscriber = mock(Subscriber.class);

        // when:
        adaptedPublisher.subscribe(reactiveSubscriber);

        // then:
        verify(reactiveSubscriber).onSubscribe(any(Subscription.class));
        verify(reactiveSubscriber).onNext("test");
        verify(reactiveSubscriber).onComplete();
    }

    @Test
    @DisplayName("should propagate onError through adaptation")
    void shouldPropagateOnError() {
        // given:
        Throwable testException = new RuntimeException("test error");
        Flow.Publisher<String> flowPublisher = s -> {
            s.onSubscribe(mock(Flow.Subscription.class));
            s.onError(testException);
        };

        org.reactivestreams.Publisher<String> adaptedPublisher = PublisherFromFlowAdaptor.adapt(flowPublisher);
        Subscriber<String> reactiveSubscriber = mock(Subscriber.class);

        // when:
        adaptedPublisher.subscribe(reactiveSubscriber);

        // then:
        verify(reactiveSubscriber).onError(testException);
    }
}
