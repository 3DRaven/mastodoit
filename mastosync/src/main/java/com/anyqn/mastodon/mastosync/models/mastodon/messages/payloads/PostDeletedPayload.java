package com.anyqn.mastodon.mastosync.models.mastodon.messages.payloads;

import com.anyqn.mastodon.common.models.values.mastodon.MastodonId;
import com.anyqn.mastodon.common.models.values.mastodon.MastodonStatusId;
import com.anyqn.mastodon.common.models.values.twitter.TweetId;
import com.anyqn.mastodon.mastosync.models.twitter.FutureTweet;
import com.anyqn.mastodon.mastosync.processors.visitors.IMastodonPayloadVisitor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Value;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;
import org.checkerframework.checker.nullness.qual.Nullable;

@Value
@EqualsAndHashCode(onlyExplicitlyIncluded = true,callSuper = true)
@Jacksonized
@Builder
public class PostDeletedPayload extends AbstractPayload {
    @Getter
    @EqualsAndHashCode.Include
    MastodonStatusId id;

    @Override
    public FutureTweet visit(boolean eventFromMainUser, @Nullable TweetId parentTweetId, IMastodonPayloadVisitor visitor) {
        return visitor.visit(eventFromMainUser,parentTweetId,this);
    }
}
