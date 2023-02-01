package com.anyqn.mastodon.common.models.twitter.tweets;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Value
@Jacksonized
@Builder(toBuilder = true)
public class TweetsGroup {
    @Builder.Default
    List<PublishedTweet> data = List.of();
    GroupMeta meta;
}
