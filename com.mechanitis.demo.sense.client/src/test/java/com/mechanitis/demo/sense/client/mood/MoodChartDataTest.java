package com.mechanitis.demo.sense.client.mood;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MoodChartDataTest {
    @Test
    @DisplayName("should increment happy slice")
    void shouldIncrementHappy() {
        // given:
        MoodChartData moodChartData = new MoodChartData();

        // when:
        moodChartData.onNext("HAPPY");

        // then:
        assertEquals(1, moodChartData.getHappyPortion().getPieValue());
    }

    @Test
    @DisplayName("should increment happy slice for every time a happy emotion is found")
    void shouldIncrementHappyEveryTime() {
        // given:
        MoodChartData moodChartData = new MoodChartData();

        // when:
        moodChartData.onNext("HAPPY");
        moodChartData.onNext("HAPPY");
        moodChartData.onNext("HAPPY");

        // then:
        assertEquals(3, moodChartData.getHappyPortion().getPieValue());
    }

    @Test
    @DisplayName("should increment sad slice")
    void shouldIncrementSad() {
        // given:
        MoodChartData moodChartData = new MoodChartData();

        // when:
        moodChartData.onNext("SAD");

        // then:
        assertEquals(1, moodChartData.getSadPortion().getPieValue());
    }

}