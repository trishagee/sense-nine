package com.mechanitis.demo.sense.twitter;

import com.mechanitis.demo.sense.service.BroadcastingServerEndpoint;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.logging.Logger;
import java.util.stream.Stream;

import static java.lang.String.format;
import static java.nio.file.Files.lines;
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

    CannedTweetsService(Path filePath) {
        this.filePath = filePath;
    }

    @Override
    public void run() {
        LOGGER.fine(() -> format("Starting CannedTweetService reading %s", filePath.toAbsolutePath()));

        try (Stream<String> lines = lines(filePath)) {
            lines.filter(s -> !s.equals("OK"))
                 .peek(s -> this.addArtificialDelay())
                 .forEach(tweetsEndpoint::onNext);

        } catch (IOException e) {
            //TODO: do some error handling here!!!
            LOGGER.severe(e::getMessage);
            e.printStackTrace();
        }
    }

    private void addArtificialDelay() {
        try {
            //reading the file is FAST, add an artificial delay
            MILLISECONDS.sleep(100);
        } catch (InterruptedException e) {
            LOGGER.log(WARNING, e.getMessage(), e);
        }
    }

    public static void main(String[] args) throws URISyntaxException {
        new CannedTweetsService(get("tweetdata60-mins.txt")).run();
    }

    void stop() {
        tweetsEndpoint.close();
    }
}
