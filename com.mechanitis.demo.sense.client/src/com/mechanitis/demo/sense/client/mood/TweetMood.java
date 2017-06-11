package com.mechanitis.demo.sense.client.mood;

import java.util.Set;

import static com.mechanitis.demo.sense.client.mood.Mood.HAPPY;
import static com.mechanitis.demo.sense.client.mood.Mood.SAD;

class TweetMood {
    private final Set<Mood> moods;

    TweetMood(Set<Mood> moods) {
        this.moods = moods;
    }

    boolean isHappy() {
        return moods.contains(HAPPY);
    }

    boolean isSad() {
        return moods.contains(SAD);
    }

    boolean isConfused() {
        return isHappy() && isSad();
    }
}
