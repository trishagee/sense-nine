package com.mechanitis.demo.sense.client.user;

import javafx.collections.ObservableList;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Flow;
import java.util.concurrent.atomic.AtomicInteger;
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
            AtomicInteger index = new AtomicInteger(-1);

            //noinspection ResultOfMethodCallIgnored - simply using findFirst to stop the stream
            items.stream()
                    .filter(twitterUser -> {
                        index.incrementAndGet();
                        return twitterUser.getTweetCount() < numberOfTweets;
                    })
                    .findFirst();
            items.remove(items.size() - 1);
            items.add(index.get(), currentUser);

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
