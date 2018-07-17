package com.mechanitis.demo.sense.twitter;

//import org.eclipse.jetty.util.component.LifeCycle;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

        import java.nio.file.Paths;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

class CannedTweetsServiceTest {
    private final ExecutorService executor = Executors.newFixedThreadPool(5);

//    @Test
//    void shouldMessageClientsWithTweetsReceived() throws Exception {
//        // start service
//        CannedTweetsService service = new CannedTweetsService(Paths.get("tweetdata60-mins.txt"));
//        executor.submit(service);
//
//        // run a client that connects to the server and finishes when it receives a message
//        CountDownLatch latch = new CountDownLatch(1);
//        MessageReceivedEndpoint clientEndpoint = new MessageReceivedEndpoint(latch);
//
//        boolean success = isSuccess(latch, clientEndpoint);
//        assertThat("Client endpoint should have received a message", success, is(true));
//
//        // finally
//        service.stop();
//    }
//
//    private boolean isSuccess(CountDownLatch latch, MessageReceivedEndpoint clientEndpoint) throws Exception {
//        boolean success;
//        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
//        try {
//            Session session = container.connectToServer(clientEndpoint, URI.create("ws://localhost:8081/tweets/"));
//            success = latch.await(10, TimeUnit.SECONDS);
//            session.close();
//        } finally {
//            if (container instanceof LifeCycle) {
//                ((LifeCycle) container).stop();
//            }
//        }
//        return success;
//    }

    @Test
    void shouldStop() throws InterruptedException {
        // given
        CannedTweetsService service = new CannedTweetsService(Paths.get("tweetdata60-mins.txt"));
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(service);

        // when
        service.stop();
        executorService.shutdown(); // can shut down now the service is stopped. In theory.
        boolean terminated = executorService.awaitTermination(5, TimeUnit.SECONDS);

        // then
        assertThat(terminated, is(true));
    }

}