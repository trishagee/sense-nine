package com.mechanitis.demo.sense.client.user;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.util.concurrent.atomic.AtomicInteger;

public class TwitterUser {
    private final SimpleStringProperty twitterHandle = new SimpleStringProperty();
    private final SimpleIntegerProperty tweetCount = new SimpleIntegerProperty(0);
    private AtomicInteger count;

    public TwitterUser(String twitterHandle) {
        this(twitterHandle, 0);
    }

    public TwitterUser(String twitterHandle, int numberOfTweets) {
        this.twitterHandle.set(twitterHandle);
        count = new AtomicInteger(numberOfTweets);
        tweetCount.set(numberOfTweets);
    }

    public String getTwitterHandle() {
        return twitterHandle.get();
    }

    public SimpleStringProperty twitterHandleProperty() {
        return twitterHandle;
    }

    public int getTweetCount() {
        return tweetCount.get();
    }

    public SimpleIntegerProperty tweetCountProperty() {
        return tweetCount;
    }

    public int incrementCount() {
        int newValue = count.incrementAndGet();
        tweetCount.set(newValue);
        return newValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TwitterUser that = (TwitterUser) o;

        return twitterHandle.get().equals(that.twitterHandle.get());
    }

    @Override
    public int hashCode() {
        return twitterHandle.hashCode();
    }
}
