package com.anyqn.mastodon.mastosync.services.twitter;

import com.anyqn.mastodon.common.exceptions.HttpResponseException;
import com.anyqn.mastodon.common.models.twitter.tweets.PublishedTweet;
import com.anyqn.mastodon.common.models.values.twitter.TweetId;
import com.anyqn.mastodon.mastosync.enums.MessageSource;
import com.anyqn.mastodon.mastosync.fabrics.MastodonPostToTweetTransformerFabric;
import com.anyqn.mastodon.mastosync.fabrics.ObjectMapperFabric;
import com.anyqn.mastodon.mastosync.fabrics.SimpleContextFabric;
import com.anyqn.mastodon.mastosync.models.mastodon.messages.payloads.StatusPayload;
import com.anyqn.mastodon.mastosync.models.twitter.FutureTweet;
import com.anyqn.mastodon.mastosync.models.twitter.TwitterCreationResponse;
import com.anyqn.mastodon.mastosync.models.twitter.TwitterDeletedResponse;
import com.anyqn.mastodon.mastosync.models.twitter.TwitterReplyToDeletedResponse;
import com.anyqn.mastodon.mastosync.repostitories.local.LocalPostsRepository;
import com.anyqn.mastodon.mastosync.repostitories.local.LocalTweetsRepository;
import com.anyqn.mastodon.mastosync.repostitories.twitter.AuthorizedTweetsRepository;
import com.anyqn.mastodon.mastosync.services.ConfigurationStateService;
import com.anyqn.mastodon.mastosync.transformers.to.twitter.MastodonPostToTweetTransformer;
import com.anyqn.mastodon.mastosync.transformers.to.twitter.PostCreatedPayloadToPublishedTweet;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Optional;

@Slf4j
public class TwitterTweetsService {

    private final LocalTweetsRepository localTweetsRepository =
            SimpleContextFabric.IT.getInstance(LocalTweetsRepository.class);

    private final LocalPostsRepository localPostsRepository =
            SimpleContextFabric.IT.getInstance(LocalPostsRepository.class);

    private final MastodonPostToTweetTransformer mastodonPostToTweetTransformer =
            MastodonPostToTweetTransformerFabric.IT.getInstance();
    private final AuthorizedTweetsRepository authorizedTweetsRepository =
            SimpleContextFabric.IT.getInstance(AuthorizedTweetsRepository.class);
    private final ConfigurationStateService configurationStateService =
            SimpleContextFabric.IT.getInstance(ConfigurationStateService.class);

    private final PostCreatedPayloadToPublishedTweet postCreatedPayloadToPublishedTweet =
            SimpleContextFabric.IT.getInstance(PostCreatedPayloadToPublishedTweet.class);
    private final ObjectMapper mapper = ObjectMapperFabric.IT.getInstance();

    @SneakyThrows
    public Optional<TwitterCreationResponse> createTweetFromPost(MessageSource messageSource,
                                                                 StatusPayload statusPayload,
                                                                 boolean isEventFromMainUser,
                                                                 @Nullable TweetId associatedTweetId) {
        if (!localPostsRepository.isPostPublished(statusPayload.getId())) {
            FutureTweet futureTweet = mastodonPostToTweetTransformer.map(isEventFromMainUser, statusPayload, associatedTweetId);
            log.debug("Tweet prepared [{}]", futureTweet);
            try {
                TwitterCreationResponse response = authorizedTweetsRepository.createTweet(configurationStateService.getTwitterAccessToken().orElseThrow(),
                        configurationStateService.getTwitterAccessTokenSecret().orElseThrow(), futureTweet);
                log.info("Created tweet. Response from Twitter [{}]", response);
                PublishedTweet publishedTweet =
                        postCreatedPayloadToPublishedTweet.map(
                                configurationStateService.getTwitterMainUserId().orElseThrow(),
                                Optional.ofNullable(associatedTweetId).flatMap(localTweetsRepository::getConversationId).orElse(null),
                                associatedTweetId,
                                futureTweet.getSourceStatus(),
                                response);
                localTweetsRepository.savePublishedTweet(publishedTweet,
                        futureTweet.getMastodonStatusId(),
                        futureTweet.getMastodonNotificationId(),
                        messageSource);
                log.info("Saved tweet [{}]", publishedTweet);
                return Optional.of(response);
            } catch (HttpResponseException httpResponseException) {
                log.warn("Error was got from Twitter [{}]", httpResponseException.getResponseBody());
                if (httpResponseException.getCode() == 403 && mapper.readValue(httpResponseException.getResponseBody(),
                        TwitterReplyToDeletedResponse.class).getDetail().equals("You attempted to reply to a Tweet that is deleted or not visible to you.")) {
                    if (null != futureTweet.getReply()) {
                        localTweetsRepository.markDeleted(futureTweet.getReply().getInReplyToTweetId());
                    } else {
                        throw new IllegalStateException("Reply to deleted tweet without deleted tweet id");
                    }
                }
                throw httpResponseException;
            }
        } else {
            return Optional.empty();
        }
    }

    public Optional<TwitterDeletedResponse> deleteTweet(TweetId tweetId) {
        if (!localTweetsRepository.isTweetDeleted(tweetId)) {
            TwitterDeletedResponse response = authorizedTweetsRepository.deleteTweet(
                    configurationStateService.getTwitterAccessToken().orElseThrow(),
                    configurationStateService.getTwitterAccessTokenSecret().orElseThrow(),
                    tweetId);
            if (response.getData().isDeleted()) {
                localTweetsRepository.markDeleted(tweetId);
            }
            return Optional.of(response);
        } else {
            return Optional.empty();
        }
    }
}
