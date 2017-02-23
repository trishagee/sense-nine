package com.mechanitis.demo.sense.mood;

import com.mechanitis.demo.sense.twitter.TweetParser;
import io.reactivex.Flowable;
import io.reactivex.Maybe;

import java.util.Map;

import static com.mechanitis.demo.sense.mood.Mood.HAPPY;
import static com.mechanitis.demo.sense.mood.Mood.SAD;
import static io.reactivex.Flowable.fromArray;
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

    public static Maybe<String> analyseMood(Flowable<String> fullMessage) {
        return fullMessage.map(TweetParser::getTweetMessageFrom)
                          .flatMap(s -> fromArray(s.split("\\s")))
                          .map(String::toLowerCase)
                          .filter(WORD_TO_MOOD::containsKey)
                          .map(WORD_TO_MOOD::get)
                          .distinct()
                          .map(Enum::name)
                          .reduce((m1, m2) -> m1 + "," + m2);
    }
}
