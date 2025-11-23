package com.mechanitis.demo.sense.mood;

import com.mechanitis.demo.sense.flow.PublisherFromFlowAdaptor;
import com.mechanitis.demo.sense.flow.SubscriberFromFlowAdaptor;
import com.mechanitis.demo.sense.service.Service;
import reactor.core.publisher.Flux;

import java.util.Optional;
import java.util.concurrent.Flow;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings("ConstantConditions")
class MoodService implements Runnable {
    private static final int PORT = 8082;
    private final Service service;

    private MoodService() {
        service = new Service("ws://localhost:8081/tweets/", "/moods/", PORT,
                              MoodService::filterMessagesForMoods);
    }

    @SuppressWarnings("unused")
    static void filterMessagesForMoods(Flow.Publisher<String> publisher,
                                       Flow.Subscriber<String> subscriber) {
        Flux.from(PublisherFromFlowAdaptor.adapt(publisher))
            .map(MoodService::getTweetMessageFrom)
            .flatMap(s1 -> Flux.fromArray(splitMessageIntoWords(s1)))
            .map(String::toLowerCase)
            .map(MoodAnalyser::getMood)
            .filter(Optional::isPresent)
            .map(mood -> mood.get().name())
            .subscribe(SubscriberFromFlowAdaptor.adapt(subscriber));
    }

    static String mapMessageToMoodsCSV(String message) {
        return Stream.of(message)
                     .map(MoodService::getTweetMessageFrom)
                     .flatMap(s1 -> Stream.of(splitMessageIntoWords(s1)))
                     .map(String::toLowerCase)
                     .map(MoodAnalyser::getMood)
                     .filter(Optional::isPresent)
                     .distinct()
                     .map(mood -> mood.get().name())
                     .collect(Collectors.joining(","));
    }

    private static String[] splitMessageIntoWords(String s) {
        // magical regex that splits by punctuation and numbers and leaves unicode characters
        String[] split = s.split("\\s*[^\\p{IsAlphabetic}]+\\s*");
        int length = split.length;
        // TODO write a test for this. I do not know why, but the whole Flux operation fails (the flatmap?) if this string split returns an array of size zero or 1
        if (length == 0 || length == 1) {
            String[] result = { "1", "2" };
            return result;
        }
        return split;
    }

    private static String getTweetMessageFrom(String fullTweet) {
        // TODO: this also needs a test - it's the case when the tweet data has no message for some reason
        int startIndex = fullTweet.indexOf("\"text\":\"");
        if (startIndex == -1) {
            return "";
        }
        int fieldStartIndex = startIndex + "\"text\":\"".length();
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
