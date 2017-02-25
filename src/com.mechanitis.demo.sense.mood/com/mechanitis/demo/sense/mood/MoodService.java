package com.mechanitis.demo.sense.mood;

import com.mechanitis.demo.sense.service.Service;
import com.mechanitis.demo.sense.twitter.TweetParser;
import io.reactivex.Flowable;

import javax.websocket.DeploymentException;
import java.io.IOException;
import java.util.logging.Logger;

import static com.mechanitis.demo.sense.flow.PublisherFromFlowAdaptor.toPublisher;
import static com.mechanitis.demo.sense.flow.SubscriberFromFlowAdaptor.toSubscriber;
import static io.reactivex.Flowable.fromPublisher;

public class MoodService implements Runnable {
    private static final Logger LOGGER = Logger.getLogger(MoodService.class.getName());
    private static final int PORT = 8082;
    private final Service service;

    private MoodService() {
        service = new Service("ws://localhost:8081/tweets/", "/moods/", PORT);
    }

    @Override
    public void run() {

        Flowable<String> words = fromPublisher(toPublisher(service.getPublisher()))
                .map(TweetParser::getTweetMessageFrom)
                .map(this::splitMessageIntoWords)
                .flatMap(Flowable::fromArray, false, 1);

        MoodAnalyser.coreAnalysis(words)
                    .subscribe(toSubscriber(service.getSubscriber()));
        service.run();
    }

    private String[] splitMessageIntoWords(String s) {
        // magical regex that splits by punctuation and numbers and leaves unicode characters
        return s.split("\\s*[^\\p{IsAlphabetic}]+\\s*");
    }

    public static void main(String[] args) throws IOException, DeploymentException {
        new MoodService().run();
    }
}
