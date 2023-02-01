package com.anyqn.mastodon.common.models.values;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class StringValue extends TypedValue<String> {
    public StringValue(String id) {
        super(id);
    }

    public String encode() {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }
}
