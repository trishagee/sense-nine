package com.mechanitis.demo.sense.mood;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MoodAnalyserTest {
    private static final String TWITTER_MESSAGE_TEMPLATE = "tweet = {\"created_at\":\"Tue Jan 27 12:37:11 +0000 2015\"," +
            "\"id\":560053908144275456,\"id_str\":\"560053908144275456\"," +
            "\"text\":\"%s\",\"source\":\"twitter\"}";


    @DisplayName("Should correctly identify happy messages regardless of case")
    @ParameterizedTest
    @ValueSource(strings = {"I am so happy today",
                            "I am so Awesome today"})
    void shouldFindHappyMessages(String message) {
        // when:
        String moodyMessage = MoodService.mapMessageToMoodsCSV(
                format(TWITTER_MESSAGE_TEMPLATE, message));

        // then:
        assertEquals("HAPPY", moodyMessage);
    }

    @Test
    @DisplayName("should correctly identify sad messages")
    void ShouldIdentifySadMessages() {
//        when:
        String moodyMessage = MoodService.mapMessageToMoodsCSV(format(TWITTER_MESSAGE_TEMPLATE,
                "I am so sad today"));

//        then:
        assertEquals("SAD", moodyMessage);
    }

    @DisplayName("should correctly identify mixed messages")
    @ParameterizedTest
    @ValueSource(strings = {"I am so sad today it almost makes me happy",
                            "Yesterday I was sad sad sad, but today is awesome"})
    void shouldIdentifyMixedMessages(String message) {
//        when:
        String moodyMessage = MoodService.mapMessageToMoodsCSV(format(TWITTER_MESSAGE_TEMPLATE,
                message));

//        then:
        assertEquals("SAD,HAPPY", moodyMessage);
    }

    @Test
    @DisplayName("should not have any mood for messages that are neither happy or sad")
    void shouldNotHaveMoodsForOtherMessages() {
//        when:
        String moodyMessage = MoodService.mapMessageToMoodsCSV(format(TWITTER_MESSAGE_TEMPLATE,
                "I don't care"));

//        then:
        assertEquals("", moodyMessage);
    }

}
