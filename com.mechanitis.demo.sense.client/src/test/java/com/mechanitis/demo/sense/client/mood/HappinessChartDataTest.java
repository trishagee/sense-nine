package com.mechanitis.demo.sense.client.mood;

class HappinessChartDataTest {
//    @Test
//    @DisplayName("should have ten bars with value zero before data received")
//    void shouldInit() {
//        // when:
//        HappinessChartData happinessChartData = new HappinessChartData();
//
//        // then:
//        assertEquals(10, happinessChartData.getDataSeries()
//                                           .getData()
//                                           .size());
//        happinessChartData.getDataSeries()
//                          .getData()
//                          .forEach(data -> assertEquals(0.0, data.getYValue()
//                                                                 .doubleValue()));
//    }

//    @Test
//    @DisplayName("should increment the bar that matches the current minute if the message is happy")
//    void shouldIncrement() {
//        // this functionality would be better (and more testable) if the time element was removed from the chart
//        // data - inject "now" into the constructor, and have the TweetMood know when it was created.
//        // given:
//        HappinessChartData happinessChartData = new HappinessChartData();
//
//        // when:
//        happinessChartData.onNext("HAPPY");
//
//        // then:
//        assertEquals(1, happinessChartData.getDataSeries()
//                                          .getData()
//                                          .get(0)
//                                          .getYValue()
//                                          .intValue());
//    }
//
//    @Test
//    @DisplayName("should not increment the bar that matches the current minute if the message is not happy")
//    void shouldNotIncrement() {
//        // this functionality would be better (and more testable) if the time element was removed from the chart
//        // data - inject "now" into the constructor, and have the TweetMood know when it was created.
//        // given:
//        HappinessChartData happinessChartData = new HappinessChartData();
//
//        // when:
//        happinessChartData.onNext("SAD");
//
//        // then:
//        assertEquals(0, happinessChartData.getDataSeries()
//                                          .getData()
//                                          .get(0)
//                                          .getYValue()
//                                          .intValue());
//    }
}