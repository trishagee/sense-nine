package com.mechanitis.demo.sense.twitter;

public interface TweetParser {

    static String getTweetMessageFrom(String fullTweet) {
        //very crude
        String textFieldName = "\"text\":\"";
        return getValueForField(fullTweet, textFieldName);
    }

    static String getTwitterHandleFromTweet(String fullTweet) {
        String twitterHandleFieldName = "\"screen_name\":\"";
        return getValueForField(fullTweet, twitterHandleFieldName);
    }

    private static String getValueForField(String fullTweet, String twitterHandleFieldName) {
        int indexOfTwitterHandleField = fullTweet.indexOf(twitterHandleFieldName)+ twitterHandleFieldName.length();
        int indexOfEndOfTwitterHandle = fullTweet.indexOf("\"", indexOfTwitterHandleField);
        return fullTweet.substring(indexOfTwitterHandleField, indexOfEndOfTwitterHandle);
    }
}
