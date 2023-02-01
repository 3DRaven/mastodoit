package com.anyqn.mastodon.common.enums;

import lombok.Getter;

public enum TweetReferenceType {
    RETWEETED(0), REPLIED_TO(1), QUOTED(2);
    @Getter
    private final int adaptedValue;

    TweetReferenceType(int adaptedValue) {
        this.adaptedValue = adaptedValue;
    }
}
