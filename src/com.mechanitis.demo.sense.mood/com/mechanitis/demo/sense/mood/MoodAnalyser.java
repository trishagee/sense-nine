package com.mechanitis.demo.sense.mood;

import io.reactivex.Observable;

import java.util.Map;
import java.util.StringJoiner;

import static com.mechanitis.demo.sense.mood.Mood.HAPPY;
import static com.mechanitis.demo.sense.mood.Mood.SAD;
import static com.mechanitis.demo.sense.twitter.TweetParser.getTweetMessageFrom;
import static java.util.Map.entry;
import static java.util.Map.ofEntries;

public class MoodAnalyser {
    private static final Map<String, Mood> WORD_TO_MOOD =
            ofEntries(entry("happy", HAPPY),
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

    public static String analyseMood(String fullMessage) {
        String[] words = getTweetMessageFrom(fullMessage).split("\\s");
        StringJoiner csvCreator = new StringJoiner(",");

        Observable.fromArray(words)
                  .map(String::toLowerCase)
                  .filter(WORD_TO_MOOD::containsKey)
                  .map(WORD_TO_MOOD::get)
                  .distinct()
                  .subscribe(mood -> csvCreator.add(mood.name()));

        return csvCreator.toString();
    }
}
