package com.mechanitis.demo.sense.user;

import com.mechanitis.demo.sense.mood.MoodAnalyser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MoodAnalyserTest {
    private static final String TWITTER_MESSAGE_TEMPLATE = "tweet = {\"created_at\":\"Tue Jan 27 12:37:11 +0000 2015\"," +
                                                           "\"id\":560053908144275456,\"id_str\":\"560053908144275456\"," +
                                                           "\"text\":\"%s\",\"source\":\"twitter\"}";


    @Test
    @DisplayName("should correctly identify happy messages")
    void shouldFindHappyMessages() {
        String mood = MoodAnalyser.analyseMood(format(TWITTER_MESSAGE_TEMPLATE, "I am so happy today"));

        assertEquals("HAPPY", mood);
    }

    @Test
    @DisplayName("should correctly identify happy messages that are not lower case")
    void shouldIdentifyThoseThatAreNotLowerCase() {
        String mood = MoodAnalyser.analyseMood(format(TWITTER_MESSAGE_TEMPLATE, "I am so Awesome today"));

        assertEquals("HAPPY", mood);
    }

    @Test
    @DisplayName("should correctly identify sad messages")
    void ShouldIdentifySadMessages() {
        String mood = MoodAnalyser.analyseMood(format(TWITTER_MESSAGE_TEMPLATE, "I am so sad today"));

        assertEquals("SAD", mood);
    }

    @Test
    @DisplayName("should correctly identify mixed messages")
    void shouldIdentifyMixedMessages() {
        String mood = MoodAnalyser.analyseMood(format(TWITTER_MESSAGE_TEMPLATE, "I am so sad today it almost makes me happy"));

        assertEquals("SAD,HAPPY", mood);
    }

    @Test
    @DisplayName("should correctly identify mixed messages with multiple moods")
    void shouldIdentifyMultipleMoods() {
        String mood = MoodAnalyser.analyseMood(format(TWITTER_MESSAGE_TEMPLATE, "Yesterday I was sad sad sad, but today is awesome"));

        assertEquals("SAD,HAPPY", mood);
    }

    @Test
    @DisplayName("should not have any mood for messages that are neither happy or sad")
    void shouldNotHaveMoodsForOtherMessages() {
        String mood = MoodAnalyser.analyseMood(format(TWITTER_MESSAGE_TEMPLATE, "I don't care"));

        assertEquals("", mood);
    }
}
