package com.anyqn.mastodon.common.util;

import com.anyqn.mastodon.common.models.twitter.EncodedValue;

import java.util.Map;
import java.util.TreeMap;

public abstract class PercentEncodedStringBuilder {
    protected final Map<EncodedValue, EncodedValue> values = new TreeMap<>();

    public abstract String build();

    public PercentEncodedStringBuilder appendKeyValue( String key,String value) {
        values.put(new EncodedValue(key), new EncodedValue((value)));
        return this;
    }
}
