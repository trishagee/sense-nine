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

    void doIt(String twitterHandle) {
        TwitterUser currentUser = allTwitterUsers.computeIfAbsent(twitterHandle, TwitterUser::new);
        currentUser.incrementCount();

        if (userIsDisplayed(currentUser)) {
            int currentIndex = items.indexOf(currentUser);
            if (userNeedsToMoveUpwards(currentUser, currentIndex)) {
                addUserToLeaderboard(currentUser, currentIndex);
            }
        }

        if (!userIsDisplayed(currentUser) && userCanBeDisplayed(currentUser)) {
            addUserToLeaderboard(currentUser, items.size() - 1);
        }
    }

    private void addUserToLeaderboard(TwitterUser currentUser, int positionToRemove) {
        TwitterUser userCurrentlyInNewUserPosition = items.stream()
                .filter(twitterUser -> twitterUser.getTweetCount() < currentUser.getTweetCount())
                .findFirst().orElseThrow(() -> new RuntimeException("Should be a position in the list for new user"));
        int index = items.indexOf(userCurrentlyInNewUserPosition);
        items.remove(positionToRemove);
        items.add(index, currentUser);

        minCountForDisplay = items.get(items.size() - 1).getTweetCount();
    }

    private boolean userNeedsToMoveUpwards(TwitterUser currentUser, int currentIndex) {
        return currentIndex != 0 && items.get(currentIndex - 1).getTweetCount() < currentUser.getTweetCount();
    }

    private boolean userCanBeDisplayed(TwitterUser twitterUser) {
        return twitterUser.getTweetCount() > minCountForDisplay;
    }

    private boolean userIsDisplayed(TwitterUser username) {
        return items.contains(username);
    }

    ObservableList<TwitterUser> getItems() {
        return items;
    }
}
