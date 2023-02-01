package com.anyqn.mastodon.mastosync.controllers;

import com.anyqn.mastodon.common.models.values.mastodon.MastodonHost;
import okhttp3.WebSocketListener;

public abstract class AbstractSubscriptionEventListener extends WebSocketListener {
    public abstract void onConnectionError(Throwable throwable);

    public abstract void onConnectionStarting(MastodonHost mastodonHost);
}
