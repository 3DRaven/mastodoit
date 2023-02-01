package com.anyqn.mastodon.mastosync.models.mastodon.messages.payloads;

import com.anyqn.mastodon.common.models.values.mastodon.MastodonId;
import com.anyqn.mastodon.common.models.values.twitter.TweetId;
import com.anyqn.mastodon.mastosync.models.twitter.FutureTweet;
import com.anyqn.mastodon.mastosync.processors.visitors.IMastodonPayloadVisitor;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.checkerframework.checker.nullness.qual.Nullable;

public abstract class AbstractPayload {
    public abstract FutureTweet visit(boolean eventFromMainUser, @Nullable TweetId parentTweetId, IMastodonPayloadVisitor visitor);
}
