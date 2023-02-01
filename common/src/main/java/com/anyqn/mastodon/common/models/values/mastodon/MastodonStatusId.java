package com.anyqn.mastodon.common.models.values.mastodon;

public class MastodonStatusId extends MastodonId {
    public MastodonStatusId(String id) {
        super(id);
    }

    public MastodonStatusId(long id) {
        super(String.valueOf(id));
    }
}
