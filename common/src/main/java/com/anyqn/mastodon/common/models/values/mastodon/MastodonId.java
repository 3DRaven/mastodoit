package com.anyqn.mastodon.common.models.values.mastodon;

import com.anyqn.mastodon.common.models.values.StringValue;

public abstract class MastodonId extends StringValue {
    public MastodonId(String id) {
        super(id);
    }
}
