package com.mechanitis.demo.sense.mood;

import com.mechanitis.demo.sense.service.test.StubService;

import java.util.Random;
import java.util.Set;

class MoodTestData {
    private static final Set<String> POSSIBLE_MOODS
            = Set.of("HAPPY", "SAD");

    public static void main(String[] args) {
        Random random = new Random();
        new StubService("/moods/", 8082,
                () -> getRandomMood(random)).run();
    }

    private static String getRandomMood(Random random) {
        int requiredIndex = random.nextInt(POSSIBLE_MOODS.size());
        return POSSIBLE_MOODS.toArray()[requiredIndex].toString();
    }
}
