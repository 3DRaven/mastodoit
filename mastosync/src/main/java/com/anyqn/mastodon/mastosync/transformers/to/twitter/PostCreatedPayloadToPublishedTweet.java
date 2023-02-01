package com.anyqn.mastodon.mastosync.transformers.to.twitter;

import com.anyqn.mastodon.common.enums.ReplyType;
import com.anyqn.mastodon.common.enums.TweetReferenceType;
import com.anyqn.mastodon.common.models.twitter.tweets.PublishedTweet;
import com.anyqn.mastodon.common.models.twitter.tweets.TweetReference;
import com.anyqn.mastodon.common.models.values.twitter.TweetId;
import com.anyqn.mastodon.common.models.values.twitter.TwitterUserId;
import com.anyqn.mastodon.mastosync.models.mastodon.messages.payloads.StatusPayload;
import com.anyqn.mastodon.mastosync.models.twitter.TwitterCreationResponse;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
public class PostCreatedPayloadToPublishedTweet {

    public PublishedTweet map(TwitterUserId twitterUserId,
                              @Nullable TweetId conversationId,
                              @Nullable TweetId associatedTweetId,
                              StatusPayload statusPayload,
                              TwitterCreationResponse response) {
        return PublishedTweet.builder()
                .editHistoryTweetIds(List.of(response.getData().getId()))
                .id(response.getData().getId())
                .referencedTweets(Optional.ofNullable(associatedTweetId)
                        .map(it -> List.of(TweetReference.builder()
                                .id(it)
                                .type(TweetReferenceType.REPLIED_TO)
                                .build())).orElse(List.of()))
                .createdAt(ZonedDateTime.now())
                .authorId(twitterUserId)
                .text(response.getData().getText())
                .conversationId(conversationId)
                .replySettings(ReplyType.EVERYONE)
                .possiblySensitive(statusPayload.isSensitive())
//                .inReplyToUserId()
                .build();
    }

}
