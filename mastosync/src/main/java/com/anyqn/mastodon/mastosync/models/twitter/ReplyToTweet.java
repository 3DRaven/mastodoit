package com.anyqn.mastodon.mastosync.models.twitter;

import com.anyqn.mastodon.common.models.values.twitter.TweetId;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Jacksonized
@Builder
public class ReplyToTweet {
    @JsonProperty("in_reply_to_tweet_id")
    TweetId inReplyToTweetId;
}
