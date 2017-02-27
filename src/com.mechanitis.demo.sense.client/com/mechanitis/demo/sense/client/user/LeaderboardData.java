package com.mechanitis.demo.sense.client.user;

import io.reactivex.Flowable;
import javafx.collections.ObservableList;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.HashMap;
import java.util.Map;
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

    private AtomicLong minCountForDisplay = new AtomicLong();

    public LeaderboardData(Flow.Publisher<String> publisher, int numberToDisplay) {
        this.publisher = publisher;
//        items.sorted(Comparator.comparingInt(TwitterUser::getTweetCount));
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
                .map(twitterHandle -> allTwitterUsers.computeIfAbsent(twitterHandle, TwitterUser::new))
                .doOnNext(TwitterUser::incrementCount);

        twitterUsers
                .filter(twitterUser -> !userIsDisplayed(twitterUser))
                .subscribe(newUserSubscriber);
    }

    private void addUserToLeaderboard(TwitterUser newTwitterUser) {
        TwitterUser tempForMoving = null;
        int positionForNewTwitterUser = -1;

        for (int i = 0; i < items.size(); i++) {
            TwitterUser twitterUser = items.get(i);
            if (twitterUser.getTweetCount() < newTwitterUser.getTweetCount()) {
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
        minCountForDisplay.set(items.get(items.size() - 1).getTweetCount());
    }

    private boolean userIsDisplayed(TwitterUser twitterUser) {
        boolean contains = items.contains(twitterUser);
        LOGGER.fine("LeaderboardData.userIsDisplayed: "+contains);
        return contains;
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
            addUserToLeaderboard(twitterUser);
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
