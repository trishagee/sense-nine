package com.mechanitis.demo.sense.client.user;

import javafx.application.Application;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.function.Predicate;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.jupiter.api.Assertions.assertEquals;

class LeaderboardDataTest {

    @BeforeAll
    static void setupSpec() throws InterruptedException {
//        Executors.newSingleThreadExecutor().execute({ Application.launch(StubApplication.class) });
//        SECONDS.sleep(1);
    }

    @Test
    @DisplayName("should count number of tweets by the same person")
    void shouldIncrementCount() {
        // given:
        LeaderboardData leaderboardData = new LeaderboardData(null);

        // when:
        leaderboardData.doIt("Trisha");
        leaderboardData.doIt("Someone else");
        leaderboardData.doIt("Trisha");

        // then:
//        waitFor(2, { expectedValue -> leaderboardData.getItems().size() == expectedValue });

        assertEquals(2, leaderboardData.getItems().get(0).getTweetCount());
        assertEquals("Trisha", leaderboardData.getItems().get(0).getTwitterHandle());

        assertEquals(1, leaderboardData.getItems().get(1).getTweetCount());
        assertEquals("Someone else", leaderboardData.getItems().get(1).getTwitterHandle());
    }

//    private static void waitFor(Integer expectedValue, Predicate<Integer> condition) {
//        try {
//            CountDownLatch latch = new CountDownLatch(1);
//            Runnable poller = {
//            try {
//                if (condition.test(expectedValue)) {
//                    latch.countDown();
//                }
//            } catch (Exception ignored) {
//                latch.countDown();
//            }
//            };
//            Executors.newScheduledThreadPool(1).scheduleWithFixedDelay(poller, 0, 10, MILLISECONDS);
//            boolean succeeded = latch.await(5, SECONDS);
//            assert succeeded
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//    }


    public static class StubApplication extends Application {
        @Override
        public void start(Stage primaryStage) throws Exception {
        }
    }
}