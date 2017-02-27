package com.mechanitis.demo.sense.client.user;

import javafx.application.Platform;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Flow;
import java.util.stream.IntStream;

import static javafx.collections.FXCollections.observableArrayList;

public class LeaderboardData {
    private final Map<String, TwitterUser> allTwitterUsers = new HashMap<>();
    private final Flow.Publisher<String> publisher;
    private int numberToDisplay;
    private final ObservableList<TwitterUser> items = observableArrayList();

    private int minCountForDisplay = 0;

    public LeaderboardData(Flow.Publisher<String> publisher, int numberToDisplay) {
        this.publisher = publisher;
        IntStream.range(0, numberToDisplay)
                .forEach(value -> items.add(new TwitterUser("", 0)));
        this.numberToDisplay = numberToDisplay;
    }

    private void react(String twitterHandle) {
//        Flowable.fromPublisher(toPublisher(publisher))
//                .map(s -> allTwitterUsers.computeIfAbsent(twitterHandle, TwitterUser::new))
//                .
    }

    public void doIt(String twitterHandle) {
        TwitterUser currentUser = allTwitterUsers.computeIfAbsent(twitterHandle, TwitterUser::new);
        int numberOfTweets = currentUser.incrementCount();

        if (!userIsDisplayed(currentUser) && numberOfTweets > minCountForDisplay) {
            List<TwitterUser> toDisplay = new ArrayList<>();

            // Add everyone that's above the current user
            items.stream()
                    .takeWhile(twitterUser -> twitterUser.getTweetCount() >= numberOfTweets)
                    .forEach(toDisplay::add);
            // Add current user
            toDisplay.add(currentUser);
            // Add everyone below the current user
            items.stream()
                    .dropWhile(twitterUser -> twitterUser.getTweetCount() >= numberOfTweets)
                    .forEach(toDisplay::add);

            minCountForDisplay = items.get(items.size() - 1).getTweetCount();

            // replace with runLater
            items.setAll(toDisplay.subList(0, numberToDisplay));
        }
    }

    private boolean userIsDisplayed(TwitterUser username) {
        return items.contains(username);
    }

    ObservableList<TwitterUser> getItems() {
        return items;
    }
}
