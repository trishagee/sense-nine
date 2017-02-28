package com.mechanitis.demo.sense.client.user;

import javafx.collections.ObservableList;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Flow;
import java.util.function.Predicate;
import java.util.logging.Logger;
import java.util.stream.IntStream;

import static java.util.logging.Logger.getLogger;
import static javafx.collections.FXCollections.observableArrayList;

public class LeaderboardData implements Flow.Subscriber<String>{
    private static final Logger LOGGER = getLogger(LeaderboardData.class.getName());
    private final Map<String, TwitterUser> allTwitterUsers = new HashMap<>();
    private final ObservableList<TwitterUser> items = observableArrayList();

    private int minCountForDisplay = 0;

    public LeaderboardData(int numberToDisplay) {
        IntStream.range(0, numberToDisplay)
                 .forEach(value -> items.add(new TwitterUser("")));
    }

    @Override
    public void onNext(String twitterHandle) {
        TwitterUser currentUser = allTwitterUsers.computeIfAbsent(twitterHandle, TwitterUser::new);
        int numberOfTweets = currentUser.incrementCount();

        if (userIsDisplayed(currentUser)) {
            int currentIndex = items.indexOf(currentUser);
            if (userNeedsToMoveUpwards(currentUser, currentIndex)) {
                putUserIntoNewPosition(currentUser, twitterUser -> twitterUser.equals(currentUser));
            }
        } else if (userCanBeDisplayed(numberOfTweets)) {
            putUserIntoNewPosition(currentUser, twitterUser -> false);
        }
    }

    private void putUserIntoNewPosition(TwitterUser currentUser, Predicate<TwitterUser> stopCondition) {
        int positionForNewUser = findPositionForUser(currentUser);
        TwitterUser tempForMoving;
        if (positionForNewUser > -1) {
            tempForMoving = items.get(positionForNewUser);
            items.set(positionForNewUser, currentUser);

            for (int i = positionForNewUser + 1; i < items.size(); i++) {
                TwitterUser twitterUser = items.get(i);
                items.set(i, tempForMoving);
                tempForMoving = twitterUser;
                if (stopCondition.test(twitterUser)) {
                    break;
                }
            }
            minCountForDisplay = items.get(items.size() - 1).getTweetCount();
        }
    }

    private int findPositionForUser(TwitterUser currentUser) {
        int positionForNewUser = -1;
        for (int i = 0; i < items.size(); i++) {
            TwitterUser twitterUser = items.get(i);
            if (twitterUser.getTweetCount() < currentUser.getTweetCount()) {
                positionForNewUser = i;
                break;
            }
        }
        return positionForNewUser;
    }

    private boolean userNeedsToMoveUpwards(TwitterUser currentUser, int currentIndex) {
        return currentIndex != 0 && items.get(currentIndex - 1).getTweetCount() < currentUser.getTweetCount();
    }

    private boolean userCanBeDisplayed(int numberOfTweets) {
        return numberOfTweets > minCountForDisplay;
    }

    private boolean userIsDisplayed(TwitterUser username) {
        return items.contains(username);
    }

    ObservableList<TwitterUser> getItems() {
        return items;
    }

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        subscription.request(Long.MAX_VALUE);
    }

    @Override
    public void onError(Throwable throwable) {
        LOGGER.severe(throwable.getMessage());
        throwable.printStackTrace();
    }

    @Override
    public void onComplete() {
        LOGGER.info("Done!!");
    }
}
