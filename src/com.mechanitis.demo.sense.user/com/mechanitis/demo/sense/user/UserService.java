package com.mechanitis.demo.sense.user;

import com.mechanitis.demo.sense.service.Service;
import com.mechanitis.demo.sense.twitter.TweetParser;
import io.reactivex.Flowable;

import java.util.concurrent.Flow;

import static com.mechanitis.demo.sense.flow.PublisherFromFlowAdaptor.toPublisher;
import static com.mechanitis.demo.sense.flow.SubscriberFromFlowAdaptor.toSubscriber;

public class UserService implements Runnable {
    private static final int PORT = 8083;
    private final Service service;

    private UserService() {
        service = new Service("ws://localhost:8081/tweets/", "/users/", PORT,
                              UserService::mapTweetsToTwitterUser);
    }

    static void mapTweetsToTwitterUser(Flow.Publisher<String> publisher, Flow.Subscriber<String> subscriber) {
        Flowable.fromPublisher(toPublisher(publisher))
                .map(TweetParser::getTwitterHandleFromTweet)
                .subscribe(toSubscriber(subscriber));
    }

    @Override
    public void run() {
        service.run();
    }

    public static void main(String[] args) {
        new UserService().run();
    }

}
