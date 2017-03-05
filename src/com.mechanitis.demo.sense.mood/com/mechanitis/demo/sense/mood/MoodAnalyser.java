package com.mechanitis.demo.sense.mood;

import java.util.Map;
import java.util.Optional;

import static com.mechanitis.demo.sense.mood.Mood.HAPPY;
import static com.mechanitis.demo.sense.mood.Mood.SAD;
import static java.util.Map.entry;
import static java.util.Map.ofEntries;

class MoodAnalyser {
    private static final Map<String, Mood> WORD_TO_MOOD = ofEntries(
            entry("happy", HAPPY),
            entry("good", HAPPY),
            entry("great", HAPPY),
            entry("keen", HAPPY),
            entry("awesome", HAPPY),
            entry("marvelous", HAPPY),
            entry("yay", HAPPY),
            entry("pleased", HAPPY),
            entry("sad", SAD),
            entry("mad", SAD),
            entry("blargh", SAD),
            entry("boo", SAD),
            entry("terrible", SAD),
            entry("horrible", SAD),
            entry("bad", SAD),
            entry("awful", SAD));

    private MoodAnalyser() {
    }

    static Optional<Mood> getMood(String key) {
        return Optional.ofNullable(WORD_TO_MOOD.get(key));
    }

}
