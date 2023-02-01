package com.anyqn.mastodon.mastosync.controllers;

import com.anyqn.mastodon.mastosync.fabrics.SimpleContextFabric;
import com.anyqn.mastodon.mastosync.models.twitter.TweetsGroupResponse;
import com.anyqn.mastodon.mastosync.processors.handlers.twitter.PublishedTweetConsumer;
import com.anyqn.mastodon.mastosync.services.ConfigurationStateService;
import com.anyqn.mastodon.mastosync.services.twitter.TwitterTweetsFetchService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import static com.anyqn.mastodon.common.Constants.DEFAULT_POLLING_TIME_MILLIS;

public class TwitterFetchDataController {

    private final TwitterTweetsFetchService twitterTweetsFetchService =
            SimpleContextFabric.IT.getInstance(TwitterTweetsFetchService.class);

    private final ConfigurationStateService configurationStateService =
            SimpleContextFabric.IT.getInstance(ConfigurationStateService.class);

    private final Consumer<TweetsGroupResponse> publishedTweetConsumer =
            SimpleContextFabric.IT.getInstance(PublishedTweetConsumer.class);

    private final ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();


    public ExecutorService fetchMainUserTimeline() {
        scheduledExecutorService.scheduleWithFixedDelay(() -> {
            twitterTweetsFetchService.readNextTweetsGroup(configurationStateService.getTwitterMainUserId().orElseThrow(), publishedTweetConsumer);
        }, 0, DEFAULT_POLLING_TIME_MILLIS, TimeUnit.MILLISECONDS);
        return scheduledExecutorService;
    }
}
