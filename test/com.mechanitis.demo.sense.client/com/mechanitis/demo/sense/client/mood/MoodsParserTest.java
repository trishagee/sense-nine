package com.mechanitis.demo.sense.client.mood;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class MoodsParserTest {
    @Test
    @DisplayName("should turn a single mood into a TweetMood with just that mood")
    void shouldParseSingleMood() {
        TweetMood decodedMood = MoodsParser.parse("SAD");

        assertAll(() -> assertThat(decodedMood.isSad(), is(true)),
                  () -> assertThat(decodedMood.isHappy(), is(false)),
                  () -> assertThat(decodedMood.isConfused(), is(false)));
    }

    @Test
    @DisplayName("should turn a CSV of multiple moods into a TweetMood with those properties")
    void shouldParseMultipleMoods() {
        TweetMood decodedMood = MoodsParser.parse("SAD,HAPPY");

        assertAll(() -> assertThat(decodedMood.isSad(), is(true)),
                  () -> assertThat(decodedMood.isHappy(), is(true)),
                  () -> assertThat(decodedMood.isConfused(), is(true)));
    }
}