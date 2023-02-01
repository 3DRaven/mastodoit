package com.anyqn.mastodon.mastosync.fabrics.clients;

import com.anyqn.mastodon.common.util.LoggerAdapter;
import com.anyqn.mastodon.mastosync.fabrics.ISingletonFabric;
import com.anyqn.mastodon.mastosync.fabrics.SimpleContextFabric;
import com.anyqn.mastodon.mastosync.services.ConfigurationStateService;
import okhttp3.OkHttpClient;

import java.util.concurrent.TimeUnit;

public enum UnauthorizedOkHttpClientFabric implements ISingletonFabric<OkHttpClient> {
    IT;
    private final ConfigurationStateService configurationStateService =
            SimpleContextFabric.IT.getInstance(ConfigurationStateService.class);

    private final OkHttpClient instance;

    UnauthorizedOkHttpClientFabric() {
        this.instance = new OkHttpClient.Builder()
                .connectTimeout(configurationStateService.getHttpTimeout(), TimeUnit.MILLISECONDS)
                .writeTimeout(configurationStateService.getHttpTimeout(), TimeUnit.MILLISECONDS)
                .readTimeout(configurationStateService.getHttpTimeout(), TimeUnit.MILLISECONDS)
                .addNetworkInterceptor(new LoggerAdapter(configurationStateService.geHttpLoggingLevel()))
                .build();
    }

    @Override
    public OkHttpClient getInstance() {
        return instance;
    }
}