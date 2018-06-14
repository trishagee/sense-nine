package com.mechanitis.demo.sense.mood;

import com.mechanitis.demo.sense.service.StubService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;

class MoodTestData {
    private static final Set<String> POSSIBLE_MOODS
            = Set.of("HAPPY", "SAD");

    public static void main(String[] args) {
        Set<String> moods = Set.of("HAPPY", "SAD");
//        List<String> newCopyOfCollection = Collections.unmodifiableList(new ArrayList<>(moods));
//        List<String> newCopyOfCollection = Arrays.asList("1", "1");
        Collections.copy(new ArrayList<>(3), List.of("HAPPY", "SAD"));


//        Set<String> newCopyOfSet = new HashSet<>(moods);
        Set<String> setCopyOfCollection = Set.copyOf(moods);


        List<String> newCopyOfCollection = List.copyOf(moods);




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
