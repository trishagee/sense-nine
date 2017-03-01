package com.mechanitis.demo.sense.user;

import com.mechanitis.demo.sense.service.StubService;

import java.util.List;
import java.util.Random;

class MoodTestData {
    private static final List<String> POSSIBLE_MOODS = List.of("HAPPY", "SAD");

    public static void main(String[] args) {
        Random random = new Random();
        new StubService("/moods/", 8082,
                        () -> POSSIBLE_MOODS.get(random.nextInt(2))).run();
    }
}
