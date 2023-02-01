package com.anyqn.mastodon.mastosync.models.twitter;

import com.anyqn.mastodon.common.models.twitter.tweets.TweetsGroup;
import com.anyqn.mastodon.common.models.values.twitter.TweetId;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;
import org.checkerframework.checker.nullness.qual.Nullable;

@Value
@Jacksonized
@Builder
public class TweetsGroupResponse {
    TweetsGroup tweetsGroup;
    TweetId sinceId;
    @Nullable
    TweetId untilId;
}
