package com.mechanitis.demo.sense.mood;

import com.mechanitis.demo.sense.service.Service;
import io.reactivex.Flowable;

import java.util.concurrent.Flow;

import static com.mechanitis.demo.sense.flow.PublisherFromFlowAdaptor.toPublisher;
import static com.mechanitis.demo.sense.flow.SubscriberFromFlowAdaptor.toSubscriber;
import static io.reactivex.Flowable.fromPublisher;

class MoodService implements Runnable {
//    private static final Logger LOGGER = Logger.getLogger(MoodService.class.getName());
    private static final int PORT = 8082;
    private final Service service;

    private MoodService() {
        service = new Service("ws://localhost:8081/tweets/", "/moods/", PORT,
                              MoodService::filterMessagesForMoods);
    }

    static void filterMessagesForMoods(Flow.Publisher<String> publisher, Flow.Subscriber<String> subscriber) {
        Flowable<String> words = fromPublisher(toPublisher(publisher))
                .map(MoodService::getTweetMessageFrom)
                .map(MoodService::splitMessageIntoWords)
                .flatMap(Flowable::fromArray, false, 1);
        MoodAnalyser.moodsForWords(words)
                    .subscribe(toSubscriber(subscriber));
    }

    @Override
    public void run() {
        service.run();
    }

    private static String[] splitMessageIntoWords(String s) {
        // magical regex that splits by punctuation and numbers and leaves unicode characters
        return s.split("\\s*[^\\p{IsAlphabetic}]+\\s*");
    }

    private static String getTweetMessageFrom(String fullTweet) {
        int fieldStartIndex = fullTweet.indexOf("\"text\":\"")+ "\"text\":\"".length();
        int fieldEndIndex = fullTweet.indexOf("\"", fieldStartIndex);
        return fullTweet.substring(fieldStartIndex, fieldEndIndex);
    }

    public static void main(String[] args) {
        new MoodService().run();
    }
}
