package com.anyqn.mastodon.common.enums;

import lombok.Getter;

public enum Scope {
    READ("READ"), WRITE("WRITE");

    @Getter
    private final String description;

    Scope(String description) {
        this.description = description;
    }
}
