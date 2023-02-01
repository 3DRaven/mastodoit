package com.anyqn.mastodon.common.enums;

import lombok.Getter;

public enum ReplyType {
    EVERYONE(0), MENTIONEDUSERS(1), FOLLOWING(2);

    @Getter
    private final int adaptedValue;

    ReplyType(int adaptedValue) {
        this.adaptedValue = adaptedValue;
    }
}
