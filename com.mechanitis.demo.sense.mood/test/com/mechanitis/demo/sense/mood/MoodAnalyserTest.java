package com.mechanitis.demo.sense.mood;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.*;
import java.util.stream.Stream;

import static com.mechanitis.demo.sense.mood.MoodService.filterMessagesForMoods;
import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MoodAnalyserTest {
    private static final String TWITTER_MESSAGE_TEMPLATE = "tweet = {\"created_at\":\"Tue Jan 27 12:37:11 +0000 2015\"," +
            "\"id\":560053908144275456,\"id_str\":\"560053908144275456\"," +
            "\"text\":\"%s\",\"source\":\"twitter\"}";

    static Stream<Collection> createCollection() {
        return Stream.of(Arrays.asList());
    }

    static Stream<String[]> createArray() {
        return null;
    }


    @DisplayName("Should correctly identify happy messages regardless of case")
    @ParameterizedTest
    @ValueSource(strings = {"I am so happy today", "I am so Awesome today"})
    void shouldFindHappyMessages(String message) {
        // when:
        String moodyMessage = filterMessagesForMoods(format(TWITTER_MESSAGE_TEMPLATE, message));

        // then:
        assertEquals("HAPPY", moodyMessage);
    }

    @Test
    @DisplayName("should correctly identify sad messages")
    void ShouldIdentifySadMessages() {
        // when:
        String moodyMessage = filterMessagesForMoods(format(TWITTER_MESSAGE_TEMPLATE,
                "I am so sad today"));

        // then:
        assertEquals("SAD", moodyMessage);
    }

    @Test
    @DisplayName("should correctly identify mixed messages")
    void shouldIdentifyMixedMessages() {
//        when:
        String moodyMessage = filterMessagesForMoods(format(TWITTER_MESSAGE_TEMPLATE,
                "I am so sad today it almost makes me happy"));

//        then:
        assertEquals("SAD,HAPPY", moodyMessage);
    }

    @Test
    @DisplayName("should correctly identify mixed messages with multiple moods")
    void shouldIdentifyMultipleMoods() {
//        when:
        String moodyMessage = filterMessagesForMoods(format(TWITTER_MESSAGE_TEMPLATE,
                "Yesterday I was sad sad sad, but today is awesome"));

//        then:
        assertEquals("SAD,HAPPY", moodyMessage);
    }

    @Test
    @DisplayName("should not have any mood for messages that are neither happy or sad")
    void shouldNotHaveMoodsForOtherMessages() {
//        when:
        String moodyMessage = filterMessagesForMoods(format(TWITTER_MESSAGE_TEMPLATE,
                "I don't care"));

//        then:
        assertEquals("", moodyMessage);
    }

    @ParameterizedTest
    @MethodSource(names = "createCollection")
    void shouldReportMethodsThatDoNotMakeSenseOnEmptyCollections(List<String> collection) {
        if (!(collection.size() > 0)) {
            return;
        }

        if (collection.size() == 5) {
            Object value = collection.get(6);
        }
    }

    @ParameterizedTest
    @MethodSource(names = "createCollection")
    void shouldReportMethodsThatDoNotMakeSenseOnEmptyLists(List<String> collection) {
        if (collection.isEmpty()) {
            String s = collection.get(0);
        }
    }

    @ParameterizedTest
    @MethodSource(names = "createCollection")
    void shouldReportMethodsThatDoNotMakeSenseOnEmptyMaps(Map<Integer, String> map) {
        if (map.isEmpty()) {
            boolean isEmpty = map.isEmpty();
            Object key = map.containsKey(0);
            Object value = map.containsValue("Str");
        }
    }

    @ParameterizedTest
    @MethodSource(names = "createCollection")
    void shouldReportMethodsThatDoNotMakeSenseOnEmptySets(Set<String> set) {
        if (set.isEmpty()) {
            boolean isEmpty = set.isEmpty();
            Object value = set.contains("String");
        }
    }

    @Test
    void shouldGiveNullabilityWarning() {
        ArrayList<@Nullable String> list = new ArrayList<>();
        doSomethingWithAListOfNonNullElements(list);
    }

    private void doSomethingWithAListOfNonNullElements(List<@NotNull String> list) {
        //do something
        for (String s : list) {
            //blah
            //blah
        }
    }

    @ParameterizedTest
    @MethodSource(names = "createArray")
    void reportIfIndexOutOfRange(String[] strings) {
        if (strings.length <= 5) {
            System.out.println(strings[6]);
        }
    }




}
