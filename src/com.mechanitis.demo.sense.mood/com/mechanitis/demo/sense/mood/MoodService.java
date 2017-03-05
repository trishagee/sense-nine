package com.mechanitis.demo.sense.mood;

import com.mechanitis.demo.sense.service.Service;

import java.util.Optional;
import java.util.concurrent.Flow.Publisher;
import java.util.concurrent.Flow.Subscriber;

import static com.mechanitis.demo.sense.flow.PublisherFromFlowAdaptor.toPublisher;
import static com.mechanitis.demo.sense.flow.SubscriberFromFlowAdaptor.toSubscriber;
import static io.reactivex.Flowable.fromArray;
import static io.reactivex.Flowable.fromPublisher;

@SuppressWarnings("ConstantConditions")
class MoodService implements Runnable {
    //    private static final Logger LOGGER = Logger.getLogger(MoodService.class.getName());
    private static final int PORT = 8082;
    private final Service service;

    private MoodService() {
        service = new Service("ws://localhost:8081/tweets/", "/moods/", PORT,
                MoodService::filterMessagesForMoods);
    }

    static void filterMessagesForMoods(Publisher<String> stringPublisher, Subscriber<String> stringSubscriber) {
        fromPublisher(toPublisher(stringPublisher))
                .map(MoodService::getTweetMessageFrom)
                .flatMap(s1 -> fromArray(splitMessageIntoWords(s1)), 1)
                .map(String::toLowerCase)
                .map(MoodAnalyser::getMood)
                .filter(Optional::isPresent)
                .map(mood -> mood.get().name())
                .subscribe(toSubscriber(stringSubscriber));
    }

    private static String[] splitMessageIntoWords(String s) {
        // magical regex that splits by punctuation and numbers and leaves unicode characters
        return s.split("\\s*[^\\p{IsAlphabetic}]+\\s*");
    }

    private static String getTweetMessageFrom(String fullTweet) {
        int fieldStartIndex = fullTweet.indexOf("\"text\":\"") + "\"text\":\"".length();
        int fieldEndIndex = fullTweet.indexOf("\"", fieldStartIndex);
        return fullTweet.substring(fieldStartIndex, fieldEndIndex);
    }

    @Override
    public void run() {
        service.run();
    }

    public static void main(String[] args) {
        new MoodService().run();
    }
}
