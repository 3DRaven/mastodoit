package com.anyqn.mastodon.mastosync.models.mastodon.messages.payloads;

import com.anyqn.mastodon.common.models.values.mastodon.MastodonId;
import com.anyqn.mastodon.common.models.values.mastodon.MastodonNotificationId;
import com.anyqn.mastodon.common.models.values.twitter.TweetId;
import com.anyqn.mastodon.mastosync.models.twitter.FutureTweet;
import com.anyqn.mastodon.mastosync.processors.visitors.IMastodonPayloadVisitor;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Value;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.time.ZonedDateTime;

@Value
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Jacksonized
@Builder
public class MentionPayload extends AbstractPayload {
    @Getter
    @EqualsAndHashCode.Include
    MastodonNotificationId id;

    String type;
    @JsonProperty("created_at")
    ZonedDateTime createdAt;
    Account account;
    StatusPayload status;

    @Override
    public FutureTweet visit(boolean eventFromMainUser, @Nullable TweetId parentTweetId, IMastodonPayloadVisitor visitor) {
        return visitor.visit(eventFromMainUser, parentTweetId, this);
    }
}