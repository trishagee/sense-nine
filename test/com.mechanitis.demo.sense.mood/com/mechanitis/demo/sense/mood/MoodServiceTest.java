package com.mechanitis.demo.sense.mood;

@SuppressWarnings("unchecked")
class MoodServiceTest {
    private static final String TWITTER_MESSAGE_TEMPLATE = "tweet = {\"created_at\":\"Tue Jan 27 12:37:11 +0000 2015\"," +
                                                           "\"id\":560053908144275456,\"id_str\":\"560053908144275456\"," +
                                                           "\"text\":\"%s\",\"source\":\"twitter\"}";
//    private final Flow.Subscriber<String> subscriber = mock(Flow.Subscriber.class);
//    private final StubPub publisher = new StubPub();
//
//    @BeforeEach
//    void setup() {
//        setMockSubscriberToRequestAll();
//    }
//
//    @Test
//    @DisplayName("should correctly identify happy messages")
//    void shouldFindHappyMessages() {
//        MoodService.filterMessagesForMoods(publisher, subscriber);
//        publisher.publishSingleItem(format(TWITTER_MESSAGE_TEMPLATE, "I am so happy today"));
//
//        verify(subscriber).onNext("HAPPY");
//    }
//
//    @Test
//    @DisplayName("should correctly identify happy messages that are not lower case")
//    void shouldIdentifyThoseThatAreNotLowerCase() {
//        MoodService.filterMessagesForMoods(publisher, subscriber);
//        publisher.publishSingleItem(format(TWITTER_MESSAGE_TEMPLATE, "I am so Awesome today"));
//
//        verify(subscriber).onNext("HAPPY");
//    }
//
//    @Test
//    @DisplayName("should correctly identify sad messages")
//    void ShouldIdentifySadMessages() {
//        MoodService.filterMessagesForMoods(publisher, subscriber);
//        publisher.publishSingleItem(format(TWITTER_MESSAGE_TEMPLATE, "I am so sad today"));
//
//        verify(subscriber).onNext("SAD");
//    }
//
//    @Test
//    @DisplayName("should correctly identify mixed messages")
//    void shouldIdentifyMixedMessages() {
//        MoodService.filterMessagesForMoods(publisher, subscriber);
//        publisher.publishSingleItem(format(TWITTER_MESSAGE_TEMPLATE, "I am so sad today it almost makes me happy"));
//        verify(subscriber).onNext("SAD");
//        verify(subscriber).onNext("HAPPY");
//    }
//
//    @Test
//    @DisplayName("should correctly identify mixed messages with multiple moods")
//    void shouldIdentifyMultipleMoods() {
//        MoodService.filterMessagesForMoods(publisher, subscriber);
//        publisher.publishSingleItem(format(TWITTER_MESSAGE_TEMPLATE, "Yesterday I was sad sad sad, but today is awesome"));
//        verify(subscriber, times(3)).onNext("SAD");
//        verify(subscriber).onNext("HAPPY");
//    }
//
//    @Test
//    @DisplayName("should not have any mood for messages that are neither happy or sad")
//    void shouldNotHaveMoodsForOtherMessages() {
//        MoodService.filterMessagesForMoods(publisher, subscriber);
//        publisher.publishSingleItem(format(TWITTER_MESSAGE_TEMPLATE, "I don't care"));
//
//        verify(subscriber, never()).onNext(any());
//    }
//
//    private void setMockSubscriberToRequestAll() {
//        doAnswer(invocation -> {
//            invocation.<Subscription>getArgument(0).request(Long.MAX_VALUE);
//            return null;
//        }).when(subscriber).onSubscribe(any());
//    }
//
//    private class StubPub implements Flow.Publisher<String> {
//        private Flow.Subscriber<? super String> subscriber;
//
//        @Override
//        public void subscribe(Flow.Subscriber<? super String> subscriber) {
//            this.subscriber = subscriber;
//            subscriber.onSubscribe(mock(Flow.Subscription.class));
//        }
//
//        private void publishSingleItem(String input) {
//            subscriber.onNext(input);
//        }
//    }

}
