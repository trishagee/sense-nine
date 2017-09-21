package com.mechanitis.demo.sense.mood;

import java.util.Map;
import java.util.Optional;

import static com.mechanitis.demo.sense.mood.Mood.HAPPY;
import static com.mechanitis.demo.sense.mood.Mood.SAD;

class MoodAnalyser {
    private static final Map<String, Mood> WORD_TO_MOOD = Map.of(
            "happy", HAPPY,
            "good", HAPPY,
            "great", HAPPY,
            "keen", HAPPY,
            "boo", SAD,
            "terrible", SAD,
            "horrible", SAD,
            "bad", SAD,
            "awful", SAD);


    private MoodAnalyser() {
    }

    static Optional<Mood> getMood(String key) {
        return Optional.ofNullable(WORD_TO_MOOD.get(key));
    }

}
