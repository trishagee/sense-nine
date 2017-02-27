package com.mechanitis.demo.sense.client.user;

import io.reactivex.Flowable;
import javafx.collections.ObservableList;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Flow;
import java.util.stream.IntStream;
import static com.mechanitis.demo.sense.flow.PublisherFromFlowAdaptor.toPublisher;

import static com.mechanitis.demo.sense.flow.SubscriberFromFlowAdaptor.toSubscriber;
import static javafx.collections.FXCollections.observableArrayList;

public class LeaderboardData implements Flow.Subscriber<TwitterUser> {
    private final Map<String, TwitterUser> allTwitterUsers = new HashMap<>();
    private final Flow.Publisher<String> publisher;
    private final ObservableList<TwitterUser> items = observableArrayList();

    private int minCountForDisplay = 0;

    public LeaderboardData(Flow.Publisher<String> publisher, int numberToDisplay) {
        this.publisher = publisher;
        IntStream.range(0, numberToDisplay)
                .forEach(value -> items.add(new TwitterUser("", 0)));
    }

    public void stream() {
        Flowable.fromPublisher(toPublisher(publisher))
                .map(twitterHandle -> allTwitterUsers.computeIfAbsent(twitterHandle, TwitterUser::new))
                .doOnNext(TwitterUser::incrementCount)
                .subscribe(toSubscriber(this));

    }

    void react(TwitterUser currentUser) {
        if (userIsDisplayed(currentUser)) {
            int currentIndex = items.indexOf(currentUser);
            if (userNeedsToMoveUpwards(currentUser, currentIndex)) {
                addUserToLeaderboard(currentUser, currentIndex);
            }
        }

        double numberOfTweets = currentUser.getTweetCount();
        if (!userIsDisplayed(currentUser) && numberOfTweets > minCountForDisplay) {
            System.out.println("currentUser = " + currentUser.getTwitterHandle());
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

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        subscription.request(Long.MAX_VALUE);
    }

    @Override
    public void onNext(TwitterUser item) {
        react(item);
    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void onComplete() {

    }
}
