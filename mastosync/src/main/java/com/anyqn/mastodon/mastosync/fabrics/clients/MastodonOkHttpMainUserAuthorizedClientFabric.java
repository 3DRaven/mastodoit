package com.anyqn.mastodon.mastosync.fabrics.clients;

import com.anyqn.mastodon.common.util.LoggerAdapter;
import com.anyqn.mastodon.mastosync.fabrics.SimpleContextFabric;
import com.anyqn.mastodon.mastosync.services.ConfigurationStateService;
import com.anyqn.mastodon.mastosync.utils.client.AuthorizationInterceptor;
import lombok.Getter;
import okhttp3.OkHttpClient;

import java.util.concurrent.TimeUnit;

import static com.anyqn.mastodon.common.Constants.MASTODON_OAUTH_TOKEN_PREFIX;


public enum MastodonOkHttpMainUserAuthorizedClientFabric {
    IT;
    private final ConfigurationStateService configurationStateService =
            SimpleContextFabric.IT.getInstance(ConfigurationStateService.class);

    @Getter
    private final OkHttpClient mainUserHttpClient = new OkHttpClient.Builder()
            .connectTimeout(configurationStateService.getHttpTimeout(), TimeUnit.MILLISECONDS)
            .writeTimeout(configurationStateService.getHttpTimeout(), TimeUnit.MILLISECONDS)
            .readTimeout(configurationStateService.getHttpTimeout(), TimeUnit.MILLISECONDS)
            .addInterceptor(new AuthorizationInterceptor(configurationStateService,
                    (force) ->
                            MASTODON_OAUTH_TOKEN_PREFIX + configurationStateService.getMastodonMainUserToken().orElseThrow()
            ))
            .addNetworkInterceptor(new LoggerAdapter(configurationStateService.geHttpLoggingLevel()))
            .build();

    @Getter
    private final OkHttpClient replyUserHttpClient = new OkHttpClient.Builder()
            .connectTimeout(configurationStateService.getHttpTimeout(), TimeUnit.MILLISECONDS)
            .writeTimeout(configurationStateService.getHttpTimeout(), TimeUnit.MILLISECONDS)
            .readTimeout(configurationStateService.getHttpTimeout(), TimeUnit.MILLISECONDS)
            .addInterceptor(new AuthorizationInterceptor(configurationStateService,
                    (force) ->
                            MASTODON_OAUTH_TOKEN_PREFIX + configurationStateService.getMastodonReplyUserToken().orElseThrow()
            ))
            .addNetworkInterceptor(new LoggerAdapter(configurationStateService.geHttpLoggingLevel()))
            .build();
}