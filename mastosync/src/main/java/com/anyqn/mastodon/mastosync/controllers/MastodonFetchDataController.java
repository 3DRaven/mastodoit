package com.anyqn.mastodon.mastosync.controllers;

import com.anyqn.mastodon.common.models.values.mastodon.MastodonAccountId;
import com.anyqn.mastodon.mastosync.fabrics.MessagesDelegatedHandlerFabric;
import com.anyqn.mastodon.mastosync.fabrics.SimpleContextFabric;
import com.anyqn.mastodon.mastosync.processors.handlers.mastodon.MessagesDelegatedHandler;
import com.anyqn.mastodon.mastosync.services.mastodon.MastodonAccountNotificationsFetchService;
import com.anyqn.mastodon.mastosync.services.mastodon.MastodonAccountStatusesFetchService;

public class MastodonFetchDataController {

    private final MastodonAccountNotificationsFetchService mastodonAccountNotificationsFetchService =
            SimpleContextFabric.IT.getInstance(MastodonAccountNotificationsFetchService.class);
    private final MastodonAccountStatusesFetchService mastodonAccountStatusesFetchService =
            SimpleContextFabric.IT.getInstance(MastodonAccountStatusesFetchService.class);
    private final MessagesDelegatedHandler messagesDelegatedHandler =
            MessagesDelegatedHandlerFabric.IT.getInstance();

    public void fetchStatusesAndNotifications(MastodonAccountId mastodonAccountId) {
        //TODO: резать на твиты большие посты с мастодона
        mastodonAccountStatusesFetchService.readAllStatusesFromLastSync(mastodonAccountId, messagesDelegatedHandler);
        mastodonAccountNotificationsFetchService.readAllNotificationsFromLastSync(mastodonAccountId, messagesDelegatedHandler);
    }
}
