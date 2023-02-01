package com.anyqn.mastodon.common.models.values;

import io.netty.handler.codec.http.HttpScheme;
import lombok.Getter;
import lombok.ToString;
import okhttp3.HttpUrl;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Objects;

@ToString
public abstract class UrlValue {
    @Getter
    private final HttpUrl url;

    protected UrlValue(String url) {
        @Nullable HttpUrl http = HttpUrl.parse(url);
        this.url = Objects.requireNonNullElseGet(http, () -> new HttpUrl.Builder().host(url).scheme(HttpScheme.HTTPS.toString()).build());
    }
}
