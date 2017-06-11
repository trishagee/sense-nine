package com.mechanitis.demo.sense.twitter;

import com.mechanitis.demo.sense.service.MessageReceivedEndpoint;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.nio.file.Paths;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.mechanitis.demo.sense.service.ServiceFixture.connectAndWaitForSuccess;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

class CannedTweetsServiceTest {
    private final ExecutorService executor = Executors.newFixedThreadPool(5);

    @Test
    void shouldMessageClientsWithTweetsReceived() throws Exception {
        // start service
        CannedTweetsService service = new CannedTweetsService(Paths.get("tweetdata60-mins.txt"));
        executor.submit(service);

        // run a client that connects to the server and finishes when it receives a message
        CountDownLatch latch = new CountDownLatch(1);
        MessageReceivedEndpoint clientEndpoint = new MessageReceivedEndpoint(latch);

        boolean success = connectAndWaitForSuccess(URI.create("ws://localhost:8081/tweets/"), clientEndpoint, latch);
        assertThat("Client endpoint should have received a message", success, is(true));

        // finally
//        service.stop();
    }

    @Test
    void shouldStop() throws Exception {
        CannedTweetsService service = new CannedTweetsService(Paths.get("tweetdata60-mins.txt"));
        executor.submit(service);

        service.stop();
        assertThat("Should actually reach this and not wait forever", true, is(true));
    }

}