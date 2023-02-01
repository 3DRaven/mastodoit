package com.anyqn.mastodon.common.models.values;

import org.apache.commons.lang3.StringUtils;

public class LocalDbUserName extends StringValue {
    public LocalDbUserName(String id) {
        super(id);
        if (StringUtils.isBlank(id)) {
            throw new IllegalArgumentException("User name must be not blank");
        }
    }
}
