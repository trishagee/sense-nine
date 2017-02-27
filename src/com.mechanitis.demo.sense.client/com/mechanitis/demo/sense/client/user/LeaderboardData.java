package com.mechanitis.demo.sense.client.user;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Flow;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.IntStream;

import static javafx.collections.FXCollections.observableArrayList;

public class LeaderboardData {
    private static final int NUMBER_OF_LEADERS = 17;
    private final Map<String, Integer> allTwitterUsers = new HashMap<>();
    private final Flow.Publisher<String> publisher;
    private final ObservableList<TwitterUser> items = observableArrayList();

    private int minCountForDisplay = 0;

    public LeaderboardData(Flow.Publisher<String> publisher) {
        this.publisher = publisher;
        IntStream.range(0, NUMBER_OF_LEADERS)
                .forEach(value -> items.add(new TwitterUser("", 0)));
    }

    public void doIt(String twitterHandle) {
//        Integer count = allTwitterUsers.putIfAbsent(twitterHandle, 1);
//        allTwitterUsers.putIfAbsent(twitterHandle, 0);
        Integer count = allTwitterUsers.compute(twitterHandle, (s, integer) -> integer == null ? 1 : integer + 1);
        int numberOfTweets = count;

        if (!userIsDisplayed(twitterHandle) && count > minCountForDisplay) {
            TwitterUser newTwitterUser = new TwitterUser(twitterHandle, numberOfTweets);
            TwitterUser tempForMoving = null;
            int positionForNewTwitterUser = -1;

            for (int i = 0; i < items.size(); i++) {
                TwitterUser twitterUser = items.get(i);
                if (twitterUser.getTweetCount() < numberOfTweets) {
                    positionForNewTwitterUser = i;
                    items.set(i, newTwitterUser);
                    tempForMoving = twitterUser;
                    break;
                }
            }
            if (tempForMoving != null) {
                for (int i = positionForNewTwitterUser + 1; i < items.size(); i++) {
                    TwitterUser twitterUser = items.get(i);
                    items.set(i, tempForMoving);
                    tempForMoving = twitterUser;
                }
            }
            minCountForDisplay = items.get(items.size() - 1).getTweetCount();
        }
    }

    private boolean userIsDisplayed(String username) {
        return items.contains(new TwitterUser(username));
    }


    ObservableList<TwitterUser> getItems() {
        return items;
    }
}
