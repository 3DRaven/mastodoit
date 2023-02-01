package com.anyqn.mastodon.mastosync.services.mastodon;

import com.anyqn.mastodon.common.Constants;
import com.anyqn.mastodon.common.models.values.mastodon.MastodonAccountId;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.local.LocalBucketBuilder;

import java.time.Duration;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MastodonBucketService {
    private final Map<MastodonAccountId, Bucket> notificationsBucket = new ConcurrentHashMap<>();
    private final Map<MastodonAccountId, Bucket> statusesBucket = new ConcurrentHashMap<>();

    private static final BucketConfiguration CONFIGURATION = BucketConfiguration.builder()
            .addLimit(Bandwidth.simple(Constants.MASTODON_PER_USER_READING_POSTS_LIMIT_PER_HOUR, Duration.ofHours(1)))
            .build();

    public boolean consumeNotificationReadingLimit(MastodonAccountId userId, int numberOfTweets) {
        return notificationsBucket.computeIfAbsent(userId, mastodonUserId -> {
            LocalBucketBuilder builder = Bucket.builder();
            Arrays.stream(CONFIGURATION.getBandwidths()).forEach(builder::addLimit);
            return builder.build();
        }).tryConsume(numberOfTweets);
    }

    public void returnNotificationReadingLimit(MastodonAccountId userId, int numberOfTweets) {
        if (numberOfTweets > 0 && notificationsBucket.containsKey(userId)) {
            notificationsBucket.get(userId).addTokens(numberOfTweets);
        }
    }

    public boolean consumeStatusesReadingLimit(MastodonAccountId userId, int numberOfTweets) {
        return statusesBucket.computeIfAbsent(userId, mastodonUserId -> {
            LocalBucketBuilder builder = Bucket.builder();
            Arrays.stream(CONFIGURATION.getBandwidths()).forEach(builder::addLimit);
            return builder.build();
        }).tryConsume(numberOfTweets);
    }

    public void returnStatusesReadingLimit(MastodonAccountId userId, int numberOfTweets) {
        if (numberOfTweets > 0 && statusesBucket.containsKey(userId)) {
            statusesBucket.get(userId).addTokens(numberOfTweets);
        }
    }

}
