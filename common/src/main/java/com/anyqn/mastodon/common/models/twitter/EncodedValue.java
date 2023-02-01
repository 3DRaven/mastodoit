package com.anyqn.mastodon.common.models.twitter;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class EncodedValue implements Comparable<EncodedValue> {
    private final String rawValue;
    private final String encodedValue;

    public EncodedValue(String rawValue) {
        this.rawValue = rawValue;
        this.encodedValue = URLEncoder.encode(rawValue, StandardCharsets.UTF_8);
    }

    @Override
    public String toString() {
        return encodedValue;
    }

    @Override
    public int compareTo(EncodedValue other) {
        return this.encodedValue.compareTo(other.encodedValue);
    }
}
