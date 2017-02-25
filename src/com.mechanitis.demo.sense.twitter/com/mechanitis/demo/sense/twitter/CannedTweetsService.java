package com.mechanitis.demo.sense.twitter;

import com.mechanitis.demo.sense.service.BroadcastingServerEndpoint;
import io.reactivex.Flowable;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.logging.Logger;

import static com.mechanitis.demo.sense.flow.SubscriberFromFlowAdaptor.toSubscriber;
import static java.lang.String.format;
import static java.nio.file.Files.readAllLines;
import static java.nio.file.Paths.get;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.logging.Logger.getLogger;

/**
 * Reads tweets from a file and sends them to the Twitter Service endpoint.
 */
public class CannedTweetsService implements Runnable {
    private static final Logger LOGGER = getLogger(CannedTweetsService.class.getName());

    private final BroadcastingServerEndpoint<String> tweetsEndpoint = new BroadcastingServerEndpoint<>("/tweets/", 8081);
    private final Path filePath;

    CannedTweetsService(Path filePath) {
        this.filePath = filePath;
    }

    @Override
    public void run() {
        LOGGER.fine(() -> format("Starting CannedTweetService reading %s", filePath.toAbsolutePath()));
        Flowable<Long> tick = Flowable.interval(1000, MILLISECONDS);

        try {
            Flowable.fromIterable(readAllLines(filePath))
                    .filter(s -> !s.equals("OK"))
                    .zipWith(tick, (s, aLong) -> s)
                    .subscribe(toSubscriber(tweetsEndpoint));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws URISyntaxException {
        new CannedTweetsService(get("tweetdata60-mins.txt")).run();
    }

    void stop() throws Exception {
        tweetsEndpoint.close();
    }
}
