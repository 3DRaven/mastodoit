package com.anyqn.mastodon.common.models.values;

import org.apache.commons.lang3.StringUtils;

public class LocalDbPassword extends StringValue {
    public LocalDbPassword(String id) {
        super(id);
        if (StringUtils.isBlank(id)) {
            throw new IllegalArgumentException("Password must be not blank");
        }
    }
}
