package com.mechanitis.demo.sense.flow;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscription;

import java.util.concurrent.Flow;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SuppressWarnings("unchecked")
class PublisherToFlowAdaptorTest {

    @Test
    @DisplayName("should adapt Reactive Streams Publisher to Flow.Publisher")
    void shouldAdaptReactiveStreamsPublisher() {
        // given:
        Publisher<String> reactivePublisher = mock(Publisher.class);
        Flow.Publisher<String> adaptedPublisher = PublisherToFlowAdaptor.adapt(reactivePublisher);

        Flow.Subscriber<String> flowSubscriber = mock(Flow.Subscriber.class);

        // when:
        adaptedPublisher.subscribe(flowSubscriber);

        // then:
        verify(reactivePublisher).subscribe(any(org.reactivestreams.Subscriber.class));
    }

    @Test
    @DisplayName("should wrap subscriber when subscribing")
    void shouldWrapSubscriber() {
        // given:
        Publisher<String> reactivePublisher = s -> {
            s.onSubscribe(mock(Subscription.class));
            s.onNext("test");
            s.onComplete();
        };

        Flow.Publisher<String> adaptedPublisher = PublisherToFlowAdaptor.adapt(reactivePublisher);
        Flow.Subscriber<String> flowSubscriber = mock(Flow.Subscriber.class);

        // when:
        adaptedPublisher.subscribe(flowSubscriber);

        // then:
        verify(flowSubscriber).onSubscribe(any(Flow.Subscription.class));
        verify(flowSubscriber).onNext("test");
        verify(flowSubscriber).onComplete();
    }

    @Test
    @DisplayName("should propagate onError through adaptation")
    void shouldPropagateOnError() {
        // given:
        Throwable testException = new RuntimeException("test error");
        Publisher<String> reactivePublisher = s -> {
            s.onSubscribe(mock(Subscription.class));
            s.onError(testException);
        };

        Flow.Publisher<String> adaptedPublisher = PublisherToFlowAdaptor.adapt(reactivePublisher);
        Flow.Subscriber<String> flowSubscriber = mock(Flow.Subscriber.class);

        // when:
        adaptedPublisher.subscribe(flowSubscriber);

        // then:
        verify(flowSubscriber).onError(testException);
    }
}
