package com.anyqn.mastodon.mastosync.services.twitter;

import com.anyqn.mastodon.common.Constants;
import com.anyqn.mastodon.common.models.values.twitter.TwitterUserId;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.local.LocalBucketBuilder;

import java.time.Duration;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TwitterBucketService {
    private final Map<TwitterUserId, Bucket> bucketMap = new ConcurrentHashMap<>();

    private static final BucketConfiguration CONFIGURATION = BucketConfiguration.builder()
            .addLimit(Bandwidth.simple(Constants.TWITTER_PER_USER_READING_TWEETS_LIMIT_PER_HOUR, Duration.ofHours(1)))
            .build();

    public boolean consumeTweetsReadingLimit(TwitterUserId userId, int numberOfTweets) {
        return bucketMap.computeIfAbsent(userId, twitterUserId -> {
            LocalBucketBuilder builder = Bucket.builder();
            Arrays.stream(CONFIGURATION.getBandwidths()).forEach(builder::addLimit);
            return builder.build();
        }).tryConsume(numberOfTweets);
    }

    public void returnTweetsReadingLimit(TwitterUserId userId, int numberOfTweets) {
        if (numberOfTweets > 0 && bucketMap.containsKey(userId)) {
            bucketMap.get(userId).addTokens(numberOfTweets);
        }
    }
}
