package com.anyqn.mastodon.mastosync.services.twitter;

import com.anyqn.mastodon.common.models.values.PingenHost;
import com.anyqn.mastodon.common.models.values.twitter.TwitterUserId;
import com.anyqn.mastodon.mastosync.fabrics.SimpleContextFabric;
import com.anyqn.mastodon.mastosync.models.twitter.TweetsGroupResponse;
import com.anyqn.mastodon.mastosync.repostitories.local.LocalTweetsRepository;
import com.anyqn.mastodon.mastosync.repostitories.pingen.UnauthorizedPingenTwitsRepository;
import com.anyqn.mastodon.mastosync.services.ConfigurationStateService;

import java.util.function.Consumer;

import static com.anyqn.mastodon.common.Constants.TWITTER_MAX_CONSUME_TWEETS_BY_REQUEST;

public class TwitterTweetsFetchService {

    private final UnauthorizedPingenTwitsRepository authorizedTwitterReplyRepository =
            SimpleContextFabric.IT.getInstance(UnauthorizedPingenTwitsRepository.class);
    private final ConfigurationStateService configurationStateService =
            SimpleContextFabric.IT.getInstance(ConfigurationStateService.class);
    private final LocalTweetsRepository localTweetsRepository =
            SimpleContextFabric.IT.getInstance(LocalTweetsRepository.class);

    private final TwitterBucketService bucketService = SimpleContextFabric.IT.getInstance(TwitterBucketService.class);


    private final PingenHost pingenHost = configurationStateService.getPingenHostForTwitter();

    public void readNextTweetsGroup(TwitterUserId userId, Consumer<TweetsGroupResponse> tweetsGroupConsumer) {
            if (bucketService.consumeTweetsReadingLimit(userId, TWITTER_MAX_CONSUME_TWEETS_BY_REQUEST)) {
                try {
                    localTweetsRepository.getLastFetchedTweetId()
                            .map(lastFetchedTweetId ->
                                    localTweetsRepository.getNextCreatedFromPostTweetIdSince(lastFetchedTweetId)
                                            .map(nextCreatedTweetId ->
                                                    authorizedTwitterReplyRepository.getTweetsRange(
                                                            configurationStateService.getTwitterAccessToken().orElseThrow(),
                                                            configurationStateService.getTwitterAccessTokenSecret().orElseThrow(),
                                                            userId,
                                                            pingenHost,
                                                            lastFetchedTweetId,
                                                            nextCreatedTweetId))
                                            .orElseGet(() -> authorizedTwitterReplyRepository.getTweetsRange(
                                                    configurationStateService.getTwitterAccessToken().orElseThrow(),
                                                    configurationStateService.getTwitterAccessTokenSecret().orElseThrow(),
                                                    userId,
                                                    pingenHost,
                                                    lastFetchedTweetId,
                                                    null)))
                            .or(() ->
                                    localTweetsRepository.getFirstCreatedTweetId()
                                            .map(firstCreatedTweetId ->
                                                    localTweetsRepository.getNextCreatedFromPostTweetIdSince(firstCreatedTweetId)
                                                            .map(nextCreatedTweetId ->
                                                                    authorizedTwitterReplyRepository.getTweetsRange(
                                                                            configurationStateService.getTwitterAccessToken().orElseThrow(),
                                                                            configurationStateService.getTwitterAccessTokenSecret().orElseThrow(),
                                                                            userId,
                                                                            pingenHost,
                                                                            firstCreatedTweetId,
                                                                            nextCreatedTweetId)
                                                            ).orElseGet(() ->
                                                                    authorizedTwitterReplyRepository.getTweetsRange(
                                                                            configurationStateService.getTwitterAccessToken().orElseThrow(),
                                                                            configurationStateService.getTwitterAccessTokenSecret().orElseThrow(),
                                                                            userId,
                                                                            pingenHost,
                                                                            firstCreatedTweetId,
                                                                            null)
                                                            )
                                            )
                            ).ifPresent(tweetsGroupResponse -> {
                                tweetsGroupConsumer.accept(tweetsGroupResponse);
                                bucketService.returnTweetsReadingLimit(
                                        userId,
                                        TWITTER_MAX_CONSUME_TWEETS_BY_REQUEST - tweetsGroupResponse
                                                .getTweetsGroup()
                                                .getMeta()
                                                .getResultCount()
                                );
                            });
                } finally {
                    bucketService.returnTweetsReadingLimit(userId, TWITTER_MAX_CONSUME_TWEETS_BY_REQUEST);
                }
            }
    }
}
