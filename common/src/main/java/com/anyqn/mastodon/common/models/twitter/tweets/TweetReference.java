package com.anyqn.mastodon.common.models.twitter.tweets;

import com.anyqn.mastodon.common.enums.TweetReferenceType;
import com.anyqn.mastodon.common.models.values.twitter.TweetId;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Jacksonized
@Builder
public class TweetReference {
    TweetReferenceType type;
    TweetId id;
}
