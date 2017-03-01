package com.mechanitis.demo.sense.twitter;

public interface TweetParser {

    static String getTweetMessageFrom(String fullTweet) {
        return getFieldValue(fullTweet, "\"text\":\"");
    }

    static String getTwitterHandleFromTweet(String fullTweet) {
        return getFieldValue(fullTweet, "\"screen_name\":\"");
    }

    static String getFieldValue(String fullTweet, String fieldName) {
        int fieldStartIndex = fullTweet.indexOf(fieldName)+ fieldName.length();
        int fieldEndIndex = fullTweet.indexOf("\"", fieldStartIndex);
        return fullTweet.substring(fieldStartIndex, fieldEndIndex);
    }

}
