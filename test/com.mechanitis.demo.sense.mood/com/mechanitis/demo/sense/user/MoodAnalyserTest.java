package com.mechanitis.demo.sense.user;

import com.mechanitis.demo.sense.mood.MoodAnalyser;
import io.reactivex.Flowable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MoodAnalyserTest {
    private static final String TWITTER_MESSAGE_TEMPLATE = "tweet = {\"created_at\":\"Tue Jan 27 12:37:11 +0000 2015\"," +
                                                           "\"id\":560053908144275456,\"id_str\":\"560053908144275456\"," +
                                                           "\"text\":\"%s\",\"source\":\"twitter\"}";


    @Test
    @DisplayName("should correctly identify happy messages")
    void shouldFindHappyMessages() {
        Flowable<String> mood = MoodAnalyser.analyseMood(createFlowableWithTwitterMessage("I am so happy today"));
        assertStreamContains(mood, "HAPPY");
    }

    @Test
    @DisplayName("should correctly identify happy messages that are not lower case")
    void shouldIdentifyThoseThatAreNotLowerCase() {
        Flowable<String> mood = MoodAnalyser.analyseMood(createFlowableWithTwitterMessage("I am so Awesome today"));
        assertStreamContains(mood, "HAPPY");
    }

    @Test
    @DisplayName("should correctly identify sad messages")
    void ShouldIdentifySadMessages() {
        Flowable<String> mood = MoodAnalyser.analyseMood(createFlowableWithTwitterMessage("I am so sad today"));
        assertStreamContains(mood, "SAD");
    }

    @Test
    @DisplayName("should correctly identify mixed messages")
    void shouldIdentifyMixedMessages() {
        Flowable<String> mood = MoodAnalyser.analyseMood(createFlowableWithTwitterMessage("I am so sad today it almost makes me happy"));
        assertStreamContains(mood, "SAD,HAPPY");
    }

    @Test
    @DisplayName("should correctly identify mixed messages with multiple moods")
    void shouldIdentifyMultipleMoods() {
        Flowable<String> mood = MoodAnalyser.analyseMood(createFlowableWithTwitterMessage("Yesterday I was sad sad sad, but today is awesome"));
        assertStreamContains(mood, "SAD,HAPPY");
    }

    @Test
    @DisplayName("should not have any mood for messages that are neither happy or sad")
    void shouldNotHaveMoodsForOtherMessages() {
        Flowable<String> mood = MoodAnalyser.analyseMood(createFlowableWithTwitterMessage("I don't care"));

        assertTrue(mood.isEmpty().blockingGet());
    }

    private static void assertStreamContains(Flowable<String> mood, String... expected) {
        int expectedLength = expected.length;
        AtomicInteger count = new AtomicInteger(0);
        AtomicBoolean failed = new AtomicBoolean(false);
        List<String> errorMessages = new CopyOnWriteArrayList<>();

        mood.subscribe(s -> {
            int i = count.getAndIncrement();
            if (i >= expectedLength) {
                errorMessages.add(format("Stream should not contain more items than expected. " +
                                         "Expected %d, got at least %d", expectedLength, i));
            }
            if (!expected[i].equals(s)) {
                errorMessages.add(format("Expected %s, got %s", expected[i], s));
            }
        }, t -> failed.set(true));
        assertFalse(failed.get(), errorMessages::toString);
        assertEquals(expectedLength, count.get());
    }

    private Flowable<String> createFlowableWithTwitterMessage(String message) {
        return Flowable.just(format(TWITTER_MESSAGE_TEMPLATE, message));
    }
}
