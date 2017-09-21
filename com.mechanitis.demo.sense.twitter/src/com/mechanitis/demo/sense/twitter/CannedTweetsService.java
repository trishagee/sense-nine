package com.mechanitis.demo.sense.twitter;

import com.mechanitis.demo.sense.service.BroadcastingServerEndpoint;
import io.reactivex.Flowable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Logger;

import static java.lang.String.format;
import static java.nio.file.Paths.get;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.logging.Logger.getLogger;

/**
 * Reads tweets from a file and sends them to the Twitter Service endpoint.
 */
public class CannedTweetsService implements Runnable {
    private static final Logger LOGGER = getLogger(CannedTweetsService.class.getName());

    private final BroadcastingServerEndpoint tweetsEndpoint
            = new BroadcastingServerEndpoint("/tweets/", 8081);
    private final Path filePath;

    CannedTweetsService(Path filePath) {
        this.filePath = filePath;
    }

    @Override
    public void run() {
        LOGGER.fine(() -> format("Starting CannedTweetService reading %s", filePath.toAbsolutePath()));
        final Flowable<Long> tick = Flowable.interval(100, MILLISECONDS);

        try {
            final Flowable<String> filteredPublisher = Flowable.fromIterable(Files.readAllLines(filePath))
                                                               .filter(s -> !s.equals("OK"));
            filteredPublisher.zipWith(tick, (s, aLong) -> s)
                             .forEach(tweetsEndpoint::onNext);



        } catch (IOException e) {
            //TODO: we'll use Java 9 features to make this a bit better
            LOGGER.severe(e::getMessage);
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new CannedTweetsService(get("tweetdata60-mins.txt")).run();
    }

    void stop() {
        tweetsEndpoint.close();
    }
}
