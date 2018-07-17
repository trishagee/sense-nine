package com.mechanitis.demo.sense.mood;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.mechanitis.demo.sense.mood.Mood.HAPPY;
import static com.mechanitis.demo.sense.mood.Mood.SAD;

class MoodAnalyser {
    private static final Map<String, Mood> WORD_TO_MOOD = new HashMap<>();

    static {
        WORD_TO_MOOD.put("happy", HAPPY);
        WORD_TO_MOOD.put("good", HAPPY);
        WORD_TO_MOOD.put("great", HAPPY);
        WORD_TO_MOOD.put("keen", HAPPY);
        WORD_TO_MOOD.put("awesome", HAPPY);
        WORD_TO_MOOD.put("marvelous", HAPPY);
        WORD_TO_MOOD.put("yay", HAPPY);
        WORD_TO_MOOD.put("pleased", HAPPY);
        WORD_TO_MOOD.put("sad", SAD);
        WORD_TO_MOOD.put("mad", SAD);
        WORD_TO_MOOD.put("blargh", SAD);
        WORD_TO_MOOD.put("boo", SAD);
        WORD_TO_MOOD.put("terrible", SAD);
        WORD_TO_MOOD.put("horrible", SAD);
        WORD_TO_MOOD.put("bad", SAD);
        WORD_TO_MOOD.put("awful", SAD);
    }

    private MoodAnalyser() {
    }

    static Optional<Mood> getMood(String key) {
        final Optional<Mood> optional = Optional.ofNullable(WORD_TO_MOOD.get(key));

        //new Optional features in Java 9
        optional.or(() -> getAlternativeOptionalValue());

        optional.ifPresentOrElse(MoodAnalyser::doSomething, MoodAnalyser::doSomethingElse);

        optional.stream();

        return Optional.ofNullable(WORD_TO_MOOD.get(key));
    }

    private static void doSomethingElse() {

    }

    private static void doSomething(Mood mood) {

    }

    private static Optional<? extends Mood> getAlternativeOptionalValue() {
        return Optional.empty();
    }

}
