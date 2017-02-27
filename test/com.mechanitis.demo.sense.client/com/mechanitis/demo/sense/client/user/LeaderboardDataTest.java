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
        LeaderboardData leaderboardData = new LeaderboardData(null, 5);

        // when:
        leaderboardData.doIt("Trisha");
        leaderboardData.doIt("Someone else");
        leaderboardData.doIt("Trisha");

        // then:
        assertUserAt(leaderboardData, 2, "Trisha", 0);
        assertUserAt(leaderboardData, 1, "Someone else", 1);
    }

    @Test
    @DisplayName("should add a user to the correct position when they have enough tweets and shuffle others downwards")
    void shouldAddAUserToTheBoardWhenTheyHaveSufficientTweets() {
        // given:
        LeaderboardData leaderboardData = new LeaderboardData(null, 3);

        // when:
        leaderboardData.doIt("1");
        leaderboardData.doIt("2");
        leaderboardData.doIt("3");
        leaderboardData.doIt("4");

        // to begin with, only the first three should be displayed
        assertEquals(3, leaderboardData.getItems().size());
        assertUserAt(leaderboardData, 1, "1", 0);
        assertUserAt(leaderboardData, 1, "2", 1);
        assertUserAt(leaderboardData, 1, "3", 2);

        // when:
        leaderboardData.doIt("4");

        // then:
        assertUserAt(leaderboardData, 2, "4", 0);
        assertUserAt(leaderboardData, 1, "1", 1);
        assertUserAt(leaderboardData, 1, "2", 2);

        // when:
        leaderboardData.doIt("3");

        // then:
        assertUserAt(leaderboardData, 2, "4", 0);
        assertUserAt(leaderboardData, 2, "3", 1);
        assertUserAt(leaderboardData, 1, "1", 2);

        // when:
        leaderboardData.doIt("4");

        // then:
        assertUserAt(leaderboardData, 3, "4", 0);
        assertUserAt(leaderboardData, 2, "3", 1);
        assertUserAt(leaderboardData, 1, "1", 2);

        // when:
        leaderboardData.doIt("2");

        // then:
        assertUserAt(leaderboardData, 3, "4", 0);
        assertUserAt(leaderboardData, 2, "3", 1);
        assertUserAt(leaderboardData, 2, "2", 2);
    }

    @Test
    @DisplayName("should resort the leaderboard if a user rises up")
    void shouldResort() {
        // given:
        LeaderboardData leaderboardData = new LeaderboardData(null, 5);

        // when:
        leaderboardData.doIt("Someone else");
        leaderboardData.doIt("Trisha");
        leaderboardData.doIt("Trisha");

        // then:
        assertUserAt(leaderboardData, 2, "Trisha", 0);
        assertUserAt(leaderboardData, 1, "Someone else", 1);
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