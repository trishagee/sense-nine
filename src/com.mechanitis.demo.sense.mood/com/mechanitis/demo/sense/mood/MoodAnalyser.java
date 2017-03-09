package com.mechanitis.demo.sense.mood;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.mechanitis.demo.sense.mood.Mood.HAPPY;
import static com.mechanitis.demo.sense.mood.Mood.SAD;

class MoodAnalyser {
    private static final Map<String, Mood> WORD_TO_MOOD = Map.of(
            "happy", HAPPY,
            "good", HAPPY);

    private MoodAnalyser() {
    }

    static Optional<Mood> getMood(String key) {
        return Optional.ofNullable(WORD_TO_MOOD.get(key));
    }

}
