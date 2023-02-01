package com.anyqn.mastodon.mastosync.processors.visitors;

import com.anyqn.mastodon.common.models.values.twitter.TweetId;
import com.anyqn.mastodon.mastosync.models.mastodon.messages.payloads.MentionPayload;
import com.anyqn.mastodon.mastosync.models.mastodon.messages.payloads.PostDeletedPayload;
import com.anyqn.mastodon.mastosync.models.mastodon.messages.payloads.StatusPayload;
import com.anyqn.mastodon.mastosync.models.twitter.FutureTweet;
import org.checkerframework.checker.nullness.qual.Nullable;

public interface IMastodonPayloadVisitor {
    FutureTweet visit(boolean eventFromMainUser, @Nullable TweetId parentTweetId, MentionPayload payload);

    FutureTweet visit(boolean eventFromMainUser, @Nullable TweetId parentTweetId, PostDeletedPayload payload);

    FutureTweet visit(boolean eventFromMainUser, @Nullable TweetId parentTweetId, StatusPayload payload);
}
