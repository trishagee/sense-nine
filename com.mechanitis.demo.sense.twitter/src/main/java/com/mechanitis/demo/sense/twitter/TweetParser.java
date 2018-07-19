package com.mechanitis.demo.sense.twitter;

public interface TweetParser {

    static String getTweetMessageFrom(String fullTweet) {
        return getFieldValue(fullTweet, "\"text\":\"");
    }

    static String getTwitterHandleFromTweet(String fullTweet) {
        return getFieldValue(fullTweet, "\"screen_name\":\"");
    }

    private static String getFieldValue(String fullTweet, String fieldName) {
        int indexOfTwitterHandleField = fullTweet.indexOf(fieldName)+ fieldName.length();
        int indexOfEndOfTwitterHandle = fullTweet.indexOf("\"", indexOfTwitterHandleField);
        return fullTweet.substring(indexOfTwitterHandleField, indexOfEndOfTwitterHandle);
    }

}
