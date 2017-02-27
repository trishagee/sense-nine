package com.mechanitis.demo.sense.client.user;

import javafx.application.Platform;
import javafx.collections.ObservableList;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Flow;
import java.util.stream.Collectors;

import static javafx.collections.FXCollections.observableArrayList;

public class LeaderboardData implements Flow.Subscriber<String> {
    private static final int NUMBER_OF_LEADERS = 17;
    private final Map<String, TwitterUser> allTwitterUsers = new HashMap<>();

    private ObservableList<TwitterUser> items = observableArrayList();

    @Override
    public void onNext(String message) {
        TwitterUser twitterUser = allTwitterUsers.computeIfAbsent(message, TwitterUser::new);
        twitterUser.incrementCount();

        List<TwitterUser> topTweeters = allTwitterUsers.values()
                                                   .stream()
                                                   .sorted(Comparator.comparingInt(TwitterUser::getTweetCount)
                                                                     .reversed())
                                                   .limit(NUMBER_OF_LEADERS)
                                                   .collect(Collectors.toList());
        Platform.runLater(() -> items.setAll(topTweeters));
    }

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        subscription.request(Long.MAX_VALUE);
    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void onComplete() {

    }

    ObservableList<TwitterUser> getItems() {
        return items;
    }
}
