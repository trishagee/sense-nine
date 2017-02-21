package com.mechanitis.demo.sense.twitter;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.logging.Logger;

import static java.lang.ClassLoader.getSystemResource;
import static java.nio.file.Paths.get;
import static java.util.logging.Logger.getLogger;

/**
 * Reads tweets from a file and sends them to the Twitter Service endpoint.
 */
public class CannedTweetsService implements Runnable {
    private static final Logger LOGGER = getLogger(CannedTweetsService.class.getName());

    private final Path filePath;

    CannedTweetsService(Path filePath) {
        this.filePath = filePath;
    }

    public static void main(String[] args) throws URISyntaxException {
        new CannedTweetsService(get(getSystemResource("./tweetdata60-mins.txt").toURI())).run();
    }

    public void run() {

        // TODO: get a stream of lines in the file
        // TODO: filter out "OK" noise
        // TODO: send each line to be broadcast via websockets

    }

}
