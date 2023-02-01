package com.anyqn.mastodon.pingen.configurations;

import com.anyqn.mastodon.common.Constants;
import com.anyqn.mastodon.common.util.LoggerAdapter;
import lombok.RequiredArgsConstructor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@RequiredArgsConstructor
public class OkHttpClientConfig {
    @Bean("mastodonWebClient")
    public OkHttpClient getMastodonWebClient(@Value("${http.logging.level:BASIC}") String loggingLevel) {
        return new OkHttpClient.Builder()
                .connectTimeout(Constants.HTTP_CLIENT_TIMEOUT, TimeUnit.MILLISECONDS)
                .writeTimeout(Constants.HTTP_CLIENT_TIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(Constants.HTTP_CLIENT_TIMEOUT, TimeUnit.MILLISECONDS)
                .addNetworkInterceptor(new LoggerAdapter(HttpLoggingInterceptor.Level.valueOf(loggingLevel)))
                .build();
    }

    @Bean("twitterWebClient")
    public OkHttpClient getTwitterWebClient(@Value("${http.logging.level:BASIC}") String loggingLevel) {
        return new OkHttpClient.Builder()
                .connectTimeout(Constants.HTTP_CLIENT_TIMEOUT, TimeUnit.MILLISECONDS)
                .writeTimeout(Constants.HTTP_CLIENT_TIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(Constants.HTTP_CLIENT_TIMEOUT, TimeUnit.MILLISECONDS)
                .addNetworkInterceptor(new LoggerAdapter(HttpLoggingInterceptor.Level.valueOf(loggingLevel)))
                .build();
    }
}
