package com.mechanitis.demo.sense.user;

import com.mechanitis.demo.sense.service.Service;
import com.mechanitis.demo.sense.twitter.TweetParser;
import io.reactivex.Flowable;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

import java.util.concurrent.Flow;

import static com.mechanitis.demo.sense.flow.PublisherFromFlowAdaptor.toPublisher;
import static com.mechanitis.demo.sense.flow.SubscriberFromFlowAdaptor.toSubscriber;

public class UserService implements Runnable {
    private static final int PORT = 8083;
    private final Service service;

    private UserService() {
        service = new Service("ws://localhost:8081/tweets/", "/users/", PORT);
    }

    @Override
    public void run() {
        Flow.Subscriber<String> subscriber = service.getSubscriber();
        Subscriber<String> s = toSubscriber(subscriber);
        Publisher<String> source = toPublisher(service.getPublisher());
        Flowable.fromPublisher(source)
                .map(TweetParser::getTwitterHandleFromTweet)
                .subscribe(s);
        service.run();
    }

    public static void main(String[] args) {
        new UserService().run();
    }

}
