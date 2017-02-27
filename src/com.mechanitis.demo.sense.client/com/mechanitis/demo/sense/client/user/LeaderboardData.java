package com.mechanitis.demo.sense.client.user;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.Flow;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;
import java.util.stream.IntStream;

import static com.mechanitis.demo.sense.flow.PublisherFromFlowAdaptor.toPublisher;
import static java.util.logging.Logger.getLogger;
import static javafx.collections.FXCollections.observableArrayList;

public class LeaderboardData implements Flow.Subscriber<TwitterUser> {
    private static final Logger LOGGER = getLogger(LeaderboardData.class.getName());

    private final Map<String, TwitterUser> allTwitterUsers = new HashMap<>();
    private final Flow.Publisher<String> publisher;
    private final ObservableList<TwitterUser> items = observableArrayList();
    private boolean onDisplay = false;

    private AtomicLong minCountForDisplay = new AtomicLong();

    public LeaderboardData(Flow.Publisher<String> publisher, int numberToDisplay) {
        this.publisher = publisher;
        IntStream.range(0, numberToDisplay)
                .forEach(value -> items.add(new TwitterUser("", 0)));
    }

    public void stream() {
        NewUserSubscriber newUserSubscriber = new NewUserSubscriber();
        Flowable<TwitterUser> twitterUsers = Flowable.fromPublisher(toPublisher(publisher))
                .onErrorResumeNext(new Publisher<String>() {
                    @Override
                    public void subscribe(Subscriber<? super String> s) {
                        System.out.println("s = [" + s + "]");
                    }
                })
                .doOnNext(s -> LOGGER.fine(s))
                .map(twitterHandle -> allTwitterUsers.computeIfAbsent(twitterHandle, TwitterUser::new))
//                .doOnNext(TwitterUser::incrementCount)
                ;

//        twitterUsers.filter(twitterUser -> !userIsDisplayed(twitterUser) && userCanBeDisplayed(twitterUser))
//                .subscribe(newUserSubscriber);

        twitterUsers
                .filter(twitterUser -> !userIsDisplayed(twitterUser))
                .subscribe(newUserSubscriber);

//        twitterUsers
//                .subscribe(newUserSubscriber);

    }

//    void react(TwitterUser twitterUser) {
//        if (userIsDisplayed(twitterUser)) {
//            int currentIndex = items.indexOf(twitterUser);
//            if (userNeedsToMoveUpwards(twitterUser, currentIndex)) {
//                addUserToLeaderboard(twitterUser, currentIndex);
//            }
//        }
//
//        if (!userIsDisplayed(twitterUser) && userCanBeDisplayed(twitterUser)) {
//            addUserToLeaderboard(twitterUser, items.size() - 1);
////            minCountForDisplay = items.get(items.size() - 1).getTweetCount();
//        }
//    }

//    void doIt(String twitterHandle) {
//        TwitterUser currentUser = allTwitterUsers.computeIfAbsent(twitterHandle, TwitterUser::new);
//        currentUser.incrementCount();
//
//        if (userIsDisplayed(currentUser)) {
//            int currentIndex = items.indexOf(currentUser);
//            if (userNeedsToMoveUpwards(currentUser, currentIndex)) {
//                addUserToLeaderboard(currentUser, currentIndex);
//            }
//        }
//
//        if (!userIsDisplayed(currentUser) && userCanBeDisplayed(currentUser)) {
//            addUserToLeaderboard(currentUser, items.size() - 1);
//        }
//    }

    private void addUserToLeaderboard(TwitterUser currentUser, int positionToRemove) {
//        TwitterUser userCurrentlyInNewUserPosition = items.stream()
//                .filter(twitterUser -> twitterUser.getTweetCount() < currentUser.getTweetCount())
//                .findFirst().orElseThrow(() -> new RuntimeException("Should be a position in the list for new user"));
//        int index = items.indexOf(userCurrentlyInNewUserPosition);
//
//                              items.remove(positionToRemove);
//                              items.add(index, currentUser);
            items.add(currentUser);

        LOGGER.fine(currentUser.getTwitterHandle());

//        minCountForDisplay = items.get(items.size() - 1).getTweetCount();
    }

    private boolean userNeedsToMoveUpwards(TwitterUser currentUser, int currentIndex) {
        return currentIndex != 0 && items.get(currentIndex - 1).getTweetCount() < currentUser.getTweetCount();
    }

    private boolean userCanBeDisplayed(TwitterUser twitterUser) {
        return twitterUser.getTweetCount() > minCountForDisplay.get();
    }

    private boolean userIsDisplayed(TwitterUser twitterUser) {
        boolean contains = items.contains(twitterUser);
//        boolean contains = onDisplay;
        LOGGER.fine("LeaderboardData.userIsDisplayed: "+contains);
        onDisplay = !onDisplay;
        return contains;
//        return items.stream().anyMatch(t -> t.getTwitterHandle().equals(twitterUser.getTwitterHandle()));
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
//        react(item);
    }

    @Override
    public void onError(Throwable throwable) {
        LOGGER.severe(throwable.toString());
        throwable.printStackTrace();
    }

    @Override
    public void onComplete() {

    }

    private class NewUserSubscriber implements Subscriber<TwitterUser> {
        @Override
        public void onSubscribe(Subscription s) {
            s.request(Long.MAX_VALUE);
        }

        @Override
        public void onNext(TwitterUser twitterUser) {
            LOGGER.fine("twitterUser = [" + twitterUser + "]");
//            if (!userIsDisplayed(twitterUser) && userCanBeDisplayed(twitterUser)) {

                addUserToLeaderboard(twitterUser, items.size() - 1);
//            }
//            minCountForDisplay.setPlain(items.get(items.size() - 1).getTweetCount());
        }

        @Override
        public void onError(Throwable t) {
            t.printStackTrace();
        }

        @Override
        public void onComplete() {
            LOGGER.fine("DONE!!!");
        }
    }

}
