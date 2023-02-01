package com.anyqn.mastodon.pingen.services.twitter;


import com.anyqn.mastodon.common.Constants;
import com.anyqn.mastodon.common.models.twitter.tweets.TweetsGroup;
import com.anyqn.mastodon.common.models.values.twitter.*;
import com.anyqn.mastodon.pingen.repository.twitter.AuthorizedTwitterReplyRepository;
import com.anyqn.mastodon.pingen.services.BucketService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class TwitterFetchTweetsService {
    private final AuthorizedTwitterReplyRepository authorizedTwitterReplyRepository;
    private final TwitterUserAuthService twitterUserAuthService;
    private final TwitterConsumerKey twitterConsumerKey;
    private final TwitterConsumerSecret twitterConsumerSecret;
    private final BucketService bucketService;

    public TwitterFetchTweetsService(BucketService bucketService,
                                     AuthorizedTwitterReplyRepository authorizedTwitterReplyRepository,
                                     TwitterUserAuthService twitterUserAuthService,
                                     @Value("${client.key}") String consumerKey,
                                     @Value("${client.secret}") String consumerSecret) {
        this.authorizedTwitterReplyRepository = authorizedTwitterReplyRepository;
        this.twitterUserAuthService = twitterUserAuthService;
        this.twitterConsumerKey = new TwitterConsumerKey(consumerKey);
        this.twitterConsumerSecret = new TwitterConsumerSecret(consumerSecret);
        this.bucketService = bucketService;
    }

    public TweetsGroup getTweets(TwitterUserId twitterUserId,
                                 @Nullable TweetId sinceId,
                                 @Nullable TweetId untilId,
                                 @Nullable TwitterPaginationToken twitterPaginationToken,
                                 TwitterUserAccessToken accessToken,
                                 TwitterUserAccessTokenSecret accessTokenSecret) {
        if (bucketService.consumeTweetsReadingLimit(twitterUserId, Constants.TWITTER_MAX_CONSUME_TWEETS_BY_REQUEST)) {
            try {
                TweetsGroup tweetsGroup = authorizedTwitterReplyRepository.getLastTweets(
                        twitterConsumerKey,
                        twitterConsumerSecret,
                        accessToken,
                        accessTokenSecret,
                        twitterUserId,
                        sinceId,
                        untilId,
                        twitterPaginationToken);

                bucketService.returnTweetsReadingLimit(twitterUserId,
                        Constants.TWITTER_MAX_CONSUME_TWEETS_BY_REQUEST - tweetsGroup.getMeta().getResultCount());
                return tweetsGroup;
            } catch (Exception e) {
                bucketService.returnTweetsReadingLimit(twitterUserId, Constants.TWITTER_MAX_CONSUME_TWEETS_BY_REQUEST);
                throw e;
            }
        } else {
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS);
        }
    }
}
