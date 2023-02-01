package com.anyqn.mastodon.common.models.twitter.tweets;

import com.anyqn.mastodon.common.enums.ReplyType;
import com.anyqn.mastodon.common.enums.TweetReferenceType;
import com.anyqn.mastodon.common.models.values.twitter.TweetId;
import com.anyqn.mastodon.common.models.values.twitter.TwitterUserId;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Value
@Jacksonized
@Builder
public class PublishedTweet {
    @Builder.Default
    @JsonAlias("edit_history_tweet_ids")
    List<TweetId> editHistoryTweetIds = List.of();

    TweetId id;

    String text;

    @JsonAlias("possibly_sensitive")
    boolean possiblySensitive;

    @JsonAlias("author_id")
    TwitterUserId authorId;

    @JsonAlias("referenced_tweets")
    @Builder.Default
    List<TweetReference> referencedTweets = List.of();

    @JsonAlias("conversation_id")
    @Nullable
    TweetId conversationId;

    @JsonAlias("created_at")
    ZonedDateTime createdAt;

    @JsonAlias("reply_settings")
    ReplyType replySettings;

    @Builder.Default
    boolean isDeleted = false;

    public boolean isDeleted() {
        return isDeleted;
    }

    @JsonAnySetter
    @Builder.Default
    Map<String, Object> additionalProperties = Map.of();

    public Optional<TweetId> getParentId() {
        if (!referencedTweets.isEmpty()) {
            return Optional.of(referencedTweets.get(0).getId());
        } else {
            return Optional.empty();
        }
    }

    public Optional<TweetReferenceType> getReplyType() {
        if (!referencedTweets.isEmpty()) {
            return Optional.of(referencedTweets.get(0).getType());
        } else {
            return Optional.empty();
        }
    }
}