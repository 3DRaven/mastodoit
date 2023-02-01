package com.anyqn.mastodon.common.util;

import java.util.stream.Collectors;

public class HeaderBuilder extends PercentEncodedStringBuilder {

    @Override
    public String build() {
        return "OAuth " + values.entrySet().stream().map(it -> it.getKey() + "=\"" + it.getValue() + "\"").collect(Collectors.joining(
                ", "));
    }
}
