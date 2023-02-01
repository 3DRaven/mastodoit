package com.anyqn.mastodon.common.util;

import lombok.experimental.Delegate;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Interceptor;
import okhttp3.logging.HttpLoggingInterceptor;

@Slf4j
public class LoggerAdapter implements Interceptor {
    @Delegate
    private final HttpLoggingInterceptor loggingInterceptor;

    public LoggerAdapter(HttpLoggingInterceptor.Level level) {
        loggingInterceptor = new HttpLoggingInterceptor(log::debug);
        loggingInterceptor.setLevel(level);
        if (level != HttpLoggingInterceptor.Level.BODY) {
            loggingInterceptor.redactHeader("Authorization");
            loggingInterceptor.redactHeader("Cookie");
        }
    }
}