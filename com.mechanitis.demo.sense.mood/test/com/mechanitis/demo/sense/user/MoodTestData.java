package com.mechanitis.demo.sense.user;

import com.mechanitis.demo.sense.service.StubService;

import java.util.Random;
import java.util.Set;

import static java.util.Set.of;

class MoodTestData {
    private static final Set<String> POSSIBLE_MOODS
            = of("HAPPY", "SAD");

    public static void main(String[] args) {
        Random random = new Random();
        new StubService("/moods/", 8082,
                () -> getRandomMood(random.nextInt(2))).run();
    }

    private static String getRandomMood(int requiredIndex) {
        int i = 0;
        for (String handle : POSSIBLE_MOODS) {
            if (i == requiredIndex) {
                return handle;
            }
            i = i + 1;
        }
        return null;
    }

}
