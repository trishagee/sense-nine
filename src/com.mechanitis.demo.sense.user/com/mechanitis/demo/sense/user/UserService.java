package com.mechanitis.demo.sense.user;

import com.mechanitis.demo.sense.service.Service;

import static com.mechanitis.demo.sense.flow.PublisherFromFlowAdaptor.toPublisher;
import static com.mechanitis.demo.sense.flow.SubscriberFromFlowAdaptor.toSubscriber;
import static io.reactivex.Flowable.fromPublisher;

public class UserService implements Runnable {
    private static final int PORT = 8083;
    private final Service service;

    private UserService() {
        service = new Service("ws://localhost:8081/tweets/", "/users/", PORT,
                (stringPublisher, stringSubscriber) ->
                        fromPublisher(toPublisher(stringPublisher))
                                .map(UserService::getTwitterHandleFromTweet)
                                .subscribe(toSubscriber(stringSubscriber)));
    }

    @Override
    public void run() {
        service.run();
    }

    private static String getTwitterHandleFromTweet(String fullTweet) {
        int fieldStartIndex = fullTweet.indexOf("\"screen_name\":\"")+ "\"screen_name\":\"".length();
        int fieldEndIndex = fullTweet.indexOf("\"", fieldStartIndex);
        return fullTweet.substring(fieldStartIndex, fieldEndIndex);
    }

    public static void main(String[] args) {
        new UserService().run();
    }
}
