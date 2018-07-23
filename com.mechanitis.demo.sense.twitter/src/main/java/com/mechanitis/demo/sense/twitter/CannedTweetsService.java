package com.mechanitis.demo.sense.twitter;

import com.mechanitis.demo.sense.service.BroadcastingServerEndpoint;
import io.reactivex.Flowable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicBoolean;
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
    private AtomicBoolean running = new AtomicBoolean(true);

    CannedTweetsService(Path filePath) {
        this.filePath = filePath;
    }

    @Override
    public void run() {
        LOGGER.fine(() -> format("Starting CannedTweetService reading %s", filePath.toAbsolutePath()));
        Flowable<Long> tick = Flowable.interval(100, MILLISECONDS);

        try {
            Flowable.fromIterable(Files.readAllLines(filePath))
                    .filter(s -> !s.equals("OK"))
                    .zipWith(tick, (s, aLong) -> s)
                    .takeWhile(s -> running.get())
                    .forEach(tweetsEndpoint::onNext);
        } catch (IOException e) {
            e.printStackTrace();
        }
        LOGGER.info("Finished");
    }

    public static void main(String[] args) {
        new CannedTweetsService(get("tweetdata60-mins.txt")).run();
    }

    void stop() {
        running.set(false);
        tweetsEndpoint.close();
    }
}
