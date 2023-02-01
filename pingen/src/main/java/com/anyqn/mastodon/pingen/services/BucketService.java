package com.anyqn.mastodon.pingen.services;

import com.anyqn.mastodon.common.Constants;
import com.anyqn.mastodon.common.models.values.twitter.TwitterUserId;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import io.github.bucket4j.grid.jcache.JCacheProxyManager;
import org.springframework.stereotype.Service;

import javax.cache.CacheManager;
import java.time.Duration;

@Service
public class BucketService {
    private static final BucketConfiguration configuration = BucketConfiguration.builder()
            .addLimit(Bandwidth.simple(Constants.TWITTER_PER_USER_READING_TWEETS_LIMIT_PER_HOUR, Duration.ofHours(1)))
            .build();

    private final ProxyManager<String> buckets;

    //Autoconfiguration will provide this bean
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public BucketService(CacheManager cacheManager) {
        this.buckets = new JCacheProxyManager<>(cacheManager.getCache("tweets_reading_limits",
                String.class,
                byte[].class));
    }

    public boolean consumeTweetsReadingLimit(TwitterUserId userId, int numberOfTweets) {
        return buckets.builder().build(userId.toString(), configuration).tryConsume(numberOfTweets);
    }

    public void returnTweetsReadingLimit(TwitterUserId userId, int numberOfTweets) {
        if (numberOfTweets > 0) {
            buckets.builder().build(userId.toString(), configuration).addTokens(numberOfTweets);
        }
    }
}
