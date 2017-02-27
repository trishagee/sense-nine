package com.mechanitis.demo.sense.client.user;

import javafx.collections.ObservableList;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Flow;
import java.util.stream.IntStream;

import static javafx.collections.FXCollections.observableArrayList;

public class LeaderboardData {
    private final Map<String, TwitterUser> allTwitterUsers = new HashMap<>();
    private final Flow.Publisher<String> publisher;
    private final ObservableList<TwitterUser> items = observableArrayList();

    private int minCountForDisplay = 0;

    public LeaderboardData(Flow.Publisher<String> publisher, int numberToDisplay) {
        this.publisher = publisher;
        IntStream.range(0, numberToDisplay)
                .forEach(value -> items.add(new TwitterUser("", 0)));
    }

    public void doIt(String twitterHandle) {
        TwitterUser currentUser = allTwitterUsers.computeIfAbsent(twitterHandle, TwitterUser::new);
        int numberOfTweets = currentUser.incrementCount();

        if (!userIsDisplayed(currentUser) && numberOfTweets > minCountForDisplay) {
            TwitterUser tempForMoving = null;
            int positionForNewTwitterUser = -1;

            for (int i = 0; i < items.size(); i++) {
                TwitterUser twitterUser = items.get(i);
                if (twitterUser.getTweetCount() < numberOfTweets) {
                    positionForNewTwitterUser = i;
                    items.set(i, currentUser);
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

    private boolean userIsDisplayed(TwitterUser username) {
        return items.contains(username);
    }

    ObservableList<TwitterUser> getItems() {
        return items;
    }
}
