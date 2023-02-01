package com.anyqn.mastodon.common.util;

import java.util.stream.Collectors;

public class ParametersBuilder extends PercentEncodedStringBuilder {

    @Override
    public String build() {
        return values.entrySet().stream().map(it -> it.getKey() + "=" + it.getValue()).collect(Collectors.joining(
                "&"));
    }
}
