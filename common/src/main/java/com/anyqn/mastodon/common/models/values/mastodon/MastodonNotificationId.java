package com.anyqn.mastodon.common.models.values.mastodon;

public class MastodonNotificationId extends MastodonId {
    public MastodonNotificationId(String id) {
        super(id);
    }

    public MastodonNotificationId(long id) {
        super(String.valueOf(id));
    }

}
