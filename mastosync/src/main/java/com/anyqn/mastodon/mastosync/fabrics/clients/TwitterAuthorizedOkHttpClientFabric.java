package com.anyqn.mastodon.mastosync.fabrics.clients;

import com.anyqn.mastodon.common.models.twitter.TwitterCallDescriptor;
import com.anyqn.mastodon.common.util.LoggerAdapter;
import com.anyqn.mastodon.mastosync.fabrics.SimpleContextFabric;
import com.anyqn.mastodon.mastosync.services.ConfigurationStateService;
import com.anyqn.mastodon.mastosync.services.twitter.TwitterAuthService;
import com.anyqn.mastodon.mastosync.utils.client.AuthorizationInterceptor;
import okhttp3.OkHttpClient;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public enum TwitterAuthorizedOkHttpClientFabric {
    IT;
    private final ConfigurationStateService configurationStateService =
            SimpleContextFabric.IT.getInstance(ConfigurationStateService.class);

    private final ConcurrentHashMap<TwitterCallDescriptor, OkHttpClient> instances = new ConcurrentHashMap<>();

    public OkHttpClient getInstance(TwitterCallDescriptor twitterCallDescriptor) {
        if (!instances.containsKey(twitterCallDescriptor)) {
            synchronized (TwitterAuthorizedOkHttpClientFabric.class) {
                if (!instances.containsKey(twitterCallDescriptor)) {
                    instances.put(twitterCallDescriptor, new OkHttpClient.Builder()
                            .connectTimeout(configurationStateService.getHttpTimeout(), TimeUnit.MILLISECONDS)
                            .writeTimeout(configurationStateService.getHttpTimeout(), TimeUnit.MILLISECONDS)
                            .readTimeout(configurationStateService.getHttpTimeout(), TimeUnit.MILLISECONDS)
                            .addInterceptor(new AuthorizationInterceptor(configurationStateService, (force) ->
                                    //TODO: Pingen return header with prefix, need to remove this prefix from Pingen
                                    // and add here
                                    //HACK: We get it lazy in runtime for prevent cyclic references in code initialization
                                    SimpleContextFabric.IT.getInstance(TwitterAuthService.class)
                                            .getTwitterAuthHeader(force, twitterCallDescriptor)
                            ))
                            .addNetworkInterceptor(new LoggerAdapter(configurationStateService.geHttpLoggingLevel()))
                            .build());
                }
            }
        }
        return instances.get(twitterCallDescriptor);
    }
}