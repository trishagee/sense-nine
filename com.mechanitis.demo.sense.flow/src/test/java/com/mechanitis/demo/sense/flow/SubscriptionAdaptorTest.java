package com.mechanitis.demo.sense.flow;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Subscription;

import java.util.concurrent.Flow;

import static org.mockito.Mockito.*;

class SubscriptionAdaptorTest {

    @Test
    @DisplayName("should adapt Reactive Streams Subscription to Flow.Subscription")
    void shouldAdaptReactiveStreamsSubscription() {
        // given:
        Subscription reactiveSubscription = mock(Subscription.class);
        Flow.Subscription flowSubscription = SubscriptionAdaptor.toFlowSubscription(reactiveSubscription);

        // when:
        flowSubscription.request(10);

        // then:
        verify(reactiveSubscription).request(10);

        // when:
        flowSubscription.cancel();

        // then:
        verify(reactiveSubscription).cancel();
    }

    @Test
    @DisplayName("should adapt Flow.Subscription to Reactive Streams Subscription")
    void shouldAdaptFlowSubscription() {
        // given:
        Flow.Subscription flowSubscription = mock(Flow.Subscription.class);
        Subscription reactiveSubscription = SubscriptionAdaptor.toSubscription(flowSubscription);

        // when:
        reactiveSubscription.request(5);

        // then:
        verify(flowSubscription).request(5);

        // when:
        reactiveSubscription.cancel();

        // then:
        verify(flowSubscription).cancel();
    }

    @Test
    @DisplayName("should only call the wrapped Flow.Subscription")
    void shouldOnlyCallFlowSubscription() {
        // given:
        Flow.Subscription flowSubscription = mock(Flow.Subscription.class);
        Subscription reactiveSubscription = SubscriptionAdaptor.toSubscription(flowSubscription);

        // when:
        reactiveSubscription.request(3);

        // then:
        verify(flowSubscription).request(3);
        verify(flowSubscription, times(1)).request(anyLong());
    }

    @Test
    @DisplayName("should only call the wrapped Reactive Streams Subscription")
    void shouldOnlyCallReactiveStreamsSubscription() {
        // given:
        Subscription reactiveSubscription = mock(Subscription.class);
        Flow.Subscription flowSubscription = SubscriptionAdaptor.toFlowSubscription(reactiveSubscription);

        // when:
        flowSubscription.request(7);

        // then:
        verify(reactiveSubscription).request(7);
        verify(reactiveSubscription, times(1)).request(anyLong());
    }

    @Test
    @DisplayName("should handle cancel on Flow.Subscription wrapper")
    void shouldHandleCancelOnFlowWrapper() {
        // given:
        Subscription reactiveSubscription = mock(Subscription.class);
        Flow.Subscription flowSubscription = SubscriptionAdaptor.toFlowSubscription(reactiveSubscription);

        // when:
        flowSubscription.cancel();

        // then:
        verify(reactiveSubscription).cancel();
        verify(reactiveSubscription, times(1)).cancel();
    }

    @Test
    @DisplayName("should handle cancel on Reactive Streams Subscription wrapper")
    void shouldHandleCancelOnReactiveStreamsWrapper() {
        // given:
        Flow.Subscription flowSubscription = mock(Flow.Subscription.class);
        Subscription reactiveSubscription = SubscriptionAdaptor.toSubscription(flowSubscription);

        // when:
        reactiveSubscription.cancel();

        // then:
        verify(flowSubscription).cancel();
        verify(flowSubscription, times(1)).cancel();
    }
}
