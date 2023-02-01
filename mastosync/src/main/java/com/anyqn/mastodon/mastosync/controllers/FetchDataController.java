package com.anyqn.mastodon.mastosync.controllers;

import com.anyqn.mastodon.common.models.values.mastodon.MastodonAccountId;
import com.anyqn.mastodon.mastosync.fabrics.SimpleContextFabric;
import com.anyqn.mastodon.mastosync.services.ConfigurationStateService;
import com.anyqn.mastodon.mastosync.services.mastodon.MastodonUserService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class FetchDataController {
    private final ConfigurationStateService configurationStateService =
            SimpleContextFabric.IT.getInstance(ConfigurationStateService.class);
    private final MastodonUserService mastodonUserService =
            SimpleContextFabric.IT.getInstance(MastodonUserService.class);
    private final TwitterFetchDataController twitterFetchDataController =
            SimpleContextFabric.IT.getInstance(TwitterFetchDataController.class);
    private final MastodonFetchDataController mastodonFetchDataController =
            SimpleContextFabric.IT.getInstance(MastodonFetchDataController.class);
    private final ScheduledExecutorService fetchScheduledExecutorService =
            Executors.newSingleThreadScheduledExecutor();

    public ExecutorService sync() {
        MastodonAccountId mastodonMainAccountId = mastodonUserService.getMastodonMainUserId();
        MastodonAccountId mastodonReplyAccountId = mastodonUserService.getMastodonReplyUserId();

        fetchScheduledExecutorService.scheduleWithFixedDelay(() -> {
                    //It's easier to sync messages one by one, we don't have the task of being fast with Twitter
                    mastodonFetchDataController.fetchStatusesAndNotifications(mastodonMainAccountId);
                    mastodonFetchDataController.fetchStatusesAndNotifications(mastodonReplyAccountId);
                    twitterFetchDataController.fetchMainUserTimeline();
                }, 0,
                configurationStateService.getFetcherDelayMillis(), TimeUnit.MICROSECONDS);
        return fetchScheduledExecutorService;
    }
}
