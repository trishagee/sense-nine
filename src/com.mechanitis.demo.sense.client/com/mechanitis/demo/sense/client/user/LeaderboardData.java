package com.mechanitis.demo.sense.client.user;

import com.mechanitis.demo.sense.service.MessageListener;
import javafx.collections.ObservableList;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Flow;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import static javafx.collections.FXCollections.observableArrayList;

public class LeaderboardData implements Flow.Subscriber<String> {
    private static final int NUMBER_OF_LEADERS = 14;
    private final Map<String, TwitterUser> allTwitterUsers = new HashMap<>();
    private final ObservableList<TwitterUser> items = observableArrayList();

    private int minCountForDisplay = 0;

    public LeaderboardData() {
        this(NUMBER_OF_LEADERS);
    }

    LeaderboardData(int numberToDisplay) {
        IntStream.range(0, numberToDisplay)
                .forEach(value -> items.add(new TwitterUser("")));
    }

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        subscription.request(Long.MAX_VALUE);
    }

    @Override
    public void onNext(String twitterHandle) {
        TwitterUser currentUser = allTwitterUsers.computeIfAbsent(twitterHandle, TwitterUser::new);
        int numberOfTweets = currentUser.incrementCount();

        int currentIndex = items.indexOf(currentUser);
        if (currentIndex >= 0) { //user is displayed
            if (userNeedsToMoveUpwards(currentUser, currentIndex)) {
                putUserIntoNewPosition(currentUser, currentIndex);
            }
        } else if (userCanBeDisplayed(numberOfTweets)) {
            putUserIntoNewPosition(currentUser, items.size() - 1);
        }
    }

    @Override
    public void onError(Throwable throwable) {
        //TODO error handling
    }

    @Override
    public void onComplete() {

    }

    private int findPositionForUser(TwitterUser currentUser) {
        AtomicInteger positionForNewUser = new AtomicInteger(0);
        items.stream()
                .filter(user ->
                        user.getTweetCount() >= currentUser.getTweetCount())
                .forEach(user -> positionForNewUser.incrementAndGet());
        return positionForNewUser.get();
    }

    private void putUserIntoNewPosition(TwitterUser currentUser, int itemToRemove) {
        items.remove(itemToRemove);

        int positionForNewUser = findPositionForUser(currentUser);
        items.add(positionForNewUser, currentUser);

        minCountForDisplay = items.get(items.size() - 1)
                .getTweetCount();
    }

    private boolean userNeedsToMoveUpwards(TwitterUser currentUser, int currentIndex) {
        return currentIndex != 0 &&
                items.get(currentIndex - 1)
                        .getTweetCount() < currentUser.getTweetCount();
    }

    private boolean userCanBeDisplayed(int numberOfTweets) {
        return numberOfTweets > minCountForDisplay;
    }

    ObservableList<TwitterUser> getItems() {
        return items;
    }

}
