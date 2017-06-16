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

    private static String getValueForField(String fullTweet, String textFieldName) {
        int indexOfTextField = fullTweet.indexOf(textFieldName) + textFieldName.length();
        int indexOfEndOfText = fullTweet.indexOf("\"", indexOfTextField);
        return fullTweet.substring(indexOfTextField, indexOfEndOfText);
    }
}
