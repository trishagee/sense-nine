package com.mechanitis.demo.sense.mood;

import com.mechanitis.demo.sense.service.Service;

class MoodService implements Runnable {
//    private static final Logger LOGGER = Logger.getLogger(MoodService.class.getName());
    private static final int PORT = 8082;
    private final Service service;

    private MoodService() {
        service = new Service("ws://localhost:8081/tweets/", "/moods/", PORT,
                              s -> MoodAnalyser.analyseMood(getTweetMessageFrom(s)));
    }

    @Override
    public void run() {
        service.run();
    }

    private static String getTweetMessageFrom(String fullTweet) {
        int fieldStartIndex = fullTweet.indexOf("\"text\":\"")+ "\"text\":\"".length();
        int fieldEndIndex = fullTweet.indexOf("\"", fieldStartIndex);
        return fullTweet.substring(fieldStartIndex, fieldEndIndex);
    }

    public static void main(String[] args) {
        new MoodService().run();
    }
}
