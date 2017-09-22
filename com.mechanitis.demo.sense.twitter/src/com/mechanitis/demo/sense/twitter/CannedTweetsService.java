package com.mechanitis.demo.sense.twitter;

import com.mechanitis.demo.sense.service.BroadcastingServerEndpoint;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;
import java.util.stream.Stream;

import static java.lang.String.format;
import static java.nio.file.Paths.get;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.logging.Level.WARNING;
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

        final Stream<String> lines = getStreamOfTweets();
        // JEP 213: can use effectively final values in try-with-resources
        try (lines) {
            lines.filter(s -> !s.equals("OK"))
                 .takeWhile(s -> running.get())
                 .peek(s -> this.addArtificialDelay())
                 .forEach(tweetsEndpoint::onNext);
        }
    }

    private void addArtificialDelay() {
        try {
            //reading the file is FAST, add a delay so the UI ticks in a visible way
            MILLISECONDS.sleep(100);
        } catch (InterruptedException e) {
            LOGGER.log(WARNING, e.getMessage(), e);
        }
    }

    private Stream<String> getStreamOfTweets() {
        try {
            return Files.lines(filePath);
        } catch (IOException e) {
            LOGGER.warning(() -> format("There was a problem reading %s: %s",
                    filePath.toAbsolutePath(), e.getMessage()));
            return Stream.empty();
        }
    }

    public static void main(String[] args) {
        new CannedTweetsService(get("tweetdata60-mins.txt")).run();
    }

    void stop() {
        tweetsEndpoint.close();
        running.set(false);
    }
}
