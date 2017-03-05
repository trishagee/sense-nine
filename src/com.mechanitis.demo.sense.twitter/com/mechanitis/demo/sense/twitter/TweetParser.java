package com.mechanitis.demo.sense.twitter;

public interface TweetParser {

    static String getTweetMessageFrom(String fullTweet) {
        //very crude
        return getValueForField(fullTweet, "\"text\":\"");
    }

    static String getTwitterHandleFromTweet(String fullTweet) {
        return getValueForField(fullTweet, "\"screen_name\":\"");
    }

    static String getValueForField(String fullTweet, String fieldName) {
        int indexOfField = fullTweet.indexOf(fieldName)+ fieldName.length();
        int indexOfEndOfField = fullTweet.indexOf("\"", indexOfField);
        return fullTweet.substring(indexOfField, indexOfEndOfField);
    }
}
