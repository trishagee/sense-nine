package com.mechanitis.demo.sense.mood;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.concurrent.Flow;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SuppressWarnings("unchecked")
class MoodServiceTest {
    private static final String TWITTER_MESSAGE_TEMPLATE = "tweet = {\"created_at\":\"Tue Jan 27 12:37:11 +0000 2015\"," +
            "\"id\":560053908144275456,\"id_str\":\"560053908144275456\"," +
            "\"text\":\"%s\",\"source\":\"twitter\"}";
    private final Flow.Subscriber<String> subscriber = mock(Flow.Subscriber.class);
    // JEP 213: Diamond now supported on anonymous inner classes
    private final StubPub<String> publisher = new StubPub<>() {
        void publish(String message) {
            super.subscriber.onNext(message);
        }
    };

    @BeforeEach
    void setup() {
        setMockSubscriberToRequestAll();
    }

    @Test
    @DisplayName("should correctly identify happy messages")
    void shouldFindHappyMessages() {
        MoodService.filterMessagesForMoods(publisher, subscriber);
        publisher.publish(format(TWITTER_MESSAGE_TEMPLATE, "I am so happy today"));

        verify(subscriber).onNext("HAPPY");
    }

    @Test
    @DisplayName("should correctly identify happy messages that are not lower case")
    void shouldIdentifyThoseThatAreNotLowerCase() {
        MoodService.filterMessagesForMoods(publisher, subscriber);
        publisher.publish(format(TWITTER_MESSAGE_TEMPLATE, "I am so Awesome today"));

        verify(subscriber).onNext("HAPPY");
    }

    @Test
    @DisplayName("should correctly identify sad messages")
    void ShouldIdentifySadMessages() {
        MoodService.filterMessagesForMoods(publisher, subscriber);
        publisher.publish(format(TWITTER_MESSAGE_TEMPLATE, "I am so sad today"));

        verify(subscriber).onNext("SAD");
    }

    @Test
    @DisplayName("should correctly identify mixed messages")
    void shouldIdentifyMixedMessages() {
        MoodService.filterMessagesForMoods(publisher, subscriber);
        publisher.publish(format(TWITTER_MESSAGE_TEMPLATE, "I am so sad today it almost makes me happy"));
        verify(subscriber).onNext("SAD");
        verify(subscriber).onNext("HAPPY");
    }

    @Test
    @DisplayName("should correctly identify mixed messages with multiple moods")
    void shouldIdentifyMultipleMoods() {
        MoodService.filterMessagesForMoods(publisher, subscriber);
        publisher.publish(format(TWITTER_MESSAGE_TEMPLATE, "Yesterday I was sad sad sad, but today is awesome"));
        verify(subscriber, times(3)).onNext("SAD");
        verify(subscriber).onNext("HAPPY");
    }

    @Test
    @DisplayName("should not have any mood for messages that are neither happy or sad")
    void shouldNotHaveMoodsForOtherMessages() {
        MoodService.filterMessagesForMoods(publisher, subscriber);
        publisher.publish(format(TWITTER_MESSAGE_TEMPLATE, "I don't care"));

        verify(subscriber, never()).onNext(any());
    }

    private void setMockSubscriberToRequestAll() {
        doAnswer(invocation -> {
            invocation.<Flow.Subscription>getArgument(0).request(Long.MAX_VALUE);
            return null;
        }).when(subscriber).onSubscribe(any());
    }

    private abstract class StubPub<T> implements Flow.Publisher<T> {
        private Flow.Subscriber<? super T> subscriber;

        @Override
        public void subscribe(Flow.Subscriber<? super T> subscriber) {
            this.subscriber = subscriber;
            subscriber.onSubscribe(mock(Flow.Subscription.class));
        }

        abstract void publish(T input);
    }


    @Test
    void createImmutableList() {
        String message = "I am so so happy today, and I am not happy every day";
//        Map<String, Long> uniqueWords = Pattern.compile("\\s*[^\\p{IsAlphabetic}]+\\s*").splitAsStream(message).
//                                          map(String::toLowerCase).
//                                          collect(Collectors.groupingBy(Function.identity(),
//                                                  Collectors.counting()));
        Map<String, Long> wordCount = Pattern.compile("\\s*[^\\p{IsAlphabetic}]+\\s*").splitAsStream(message).
                                              map(String::toLowerCase).
                                              collect(Collectors.toUnmodifiableMap(Function.identity(),
                                                                                   word -> 1L,
                                                                                   (oldCount, newVal) -> oldCount + newVal));

    }
}