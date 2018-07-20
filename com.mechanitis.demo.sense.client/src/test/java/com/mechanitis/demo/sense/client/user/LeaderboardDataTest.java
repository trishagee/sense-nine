package com.mechanitis.demo.sense.client.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class LeaderboardDataTest {

    @Test
    @DisplayName("should count number of tweets by the same person")
    void shouldIncrementCount() {
        // given:
        LeaderboardData leaderboardData = new LeaderboardData(5);

        // when:
        leaderboardData.onNext("Trisha");
        leaderboardData.onNext("Someone else");
        leaderboardData.onNext("Trisha");

        // then:
        assertUserAt(leaderboardData, 2, "Trisha", 0);
        assertUserAt(leaderboardData, 1, "Someone else", 1);
    }

    @Test
    @DisplayName("should add a user to the correct position when they have enough tweets and shuffle others downwards")
    void shouldAddAUserToTheBoardWhenTheyHaveSufficientTweets() {
        // given:
        LeaderboardData leaderboardData = new LeaderboardData(3);

        // when:
        leaderboardData.onNext("1");
        leaderboardData.onNext("2");
        leaderboardData.onNext("3");
        leaderboardData.onNext("4");

        // to begin with, only the first three should be displayed
        assertEquals(3, leaderboardData.getItems().size());
        assertUserAt(leaderboardData, 1, "1", 0);
        assertUserAt(leaderboardData, 1, "2", 1);
        assertUserAt(leaderboardData, 1, "3", 2);

        // when:
        leaderboardData.onNext("4");

        // then:
        assertUserAt(leaderboardData, 2, "4", 0);
        assertUserAt(leaderboardData, 1, "1", 1);
        assertUserAt(leaderboardData, 1, "2", 2);

        // when:
        leaderboardData.onNext("3");

        // then:
        assertUserAt(leaderboardData, 2, "4", 0);
        assertUserAt(leaderboardData, 2, "3", 1);
        assertUserAt(leaderboardData, 1, "1", 2);

        // when:
        leaderboardData.onNext("4");

        // then:
        assertUserAt(leaderboardData, 3, "4", 0);
        assertUserAt(leaderboardData, 2, "3", 1);
        assertUserAt(leaderboardData, 1, "1", 2);

        // when:
        leaderboardData.onNext("2");

        // then:
        assertUserAt(leaderboardData, 3, "4", 0);
        assertUserAt(leaderboardData, 2, "3", 1);
        assertUserAt(leaderboardData, 2, "2", 2);
    }

    @Test
    @DisplayName("should resort the leaderboard if a user rises up")
    void shouldResort() {
        // given:
        LeaderboardData leaderboardData = new LeaderboardData(5);

        // when:
        leaderboardData.onNext("1");
        leaderboardData.onNext("2");
        leaderboardData.onNext("2");
        leaderboardData.onNext("1");
        leaderboardData.onNext("2");
        leaderboardData.onNext("3");
        leaderboardData.onNext("4");
        leaderboardData.onNext("5");

        // then:
        assertEquals(5, leaderboardData.getItems().size());
        assertUserAt(leaderboardData, 3, "2", 0);
        assertUserAt(leaderboardData, 2, "1", 1);
        assertUserAt(leaderboardData, 1, "3", 2);
        assertUserAt(leaderboardData, 1, "4", 3);
        assertUserAt(leaderboardData, 1, "5", 4);
    }

    private void assertUserAt(LeaderboardData leaderboardData, int expectedCount, String expectedUsername, int leaderboardPosition) {
        assertAll(() -> assertEquals(expectedCount, get(leaderboardData, leaderboardPosition).getTweetCount(),
                () -> "Count incorrect at " + leaderboardPosition),
                () -> assertEquals(expectedUsername, get(leaderboardData, leaderboardPosition).getTwitterHandle(),
                        () -> "Username incorrect at " + leaderboardPosition));
    }

    private TwitterUser get(LeaderboardData leaderboardData, int leaderboardPosition) {
        return leaderboardData.getItems().get(leaderboardPosition);
    }
}