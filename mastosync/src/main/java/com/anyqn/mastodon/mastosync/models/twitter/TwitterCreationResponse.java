package com.anyqn.mastodon.mastosync.models.twitter;

import com.anyqn.mastodon.common.models.values.twitter.TweetId;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Jacksonized
@Builder
public class TwitterCreationResponse {

    CreationResult data;

    @Value
    @Jacksonized
    @Builder
    public static class CreationResult {
        TweetId id;
        String text;
    }
}
