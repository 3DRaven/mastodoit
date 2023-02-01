package com.anyqn.mastodon.common.models.twitter.tweets;

import com.anyqn.mastodon.common.models.values.twitter.TweetId;
import com.anyqn.mastodon.common.models.values.twitter.TwitterPaginationToken;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Setter;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;
import org.checkerframework.checker.nullness.qual.Nullable;

@Value
@Jacksonized
@Builder(toBuilder = true)
public class GroupMeta {
    @JsonAlias("result_count")
    int resultCount;
    @JsonAlias("newest_id")
    @Nullable
    TweetId newestId;
    @JsonAlias("oldest_id")
    @Nullable
    TweetId oldestId;
    @JsonAlias("next_token")
    @Nullable
    TwitterPaginationToken nextToken;
}
