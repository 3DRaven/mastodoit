package com.anyqn.mastodon.mastosync.services;

import com.anyqn.mastodon.common.models.twitter.TwitterCallDescriptor;
import com.anyqn.mastodon.common.models.values.PingenHost;
import com.anyqn.mastodon.common.models.values.mastodon.*;
import com.anyqn.mastodon.common.models.values.twitter.TwitterUserAccessToken;
import com.anyqn.mastodon.common.models.values.twitter.TwitterUserAccessTokenSecret;
import com.anyqn.mastodon.common.models.values.twitter.TwitterUserId;
import com.anyqn.mastodon.mastosync.enums.Profile;
import com.anyqn.mastodon.mastosync.fabrics.ObjectMapperFabric;
import com.anyqn.mastodon.mastosync.fabrics.SimpleContextFabric;
import com.anyqn.mastodon.mastosync.models.twitter.TwitterAccessTokenResponse;
import com.anyqn.mastodon.mastosync.models.twitter.TwitterAuthHeaderResponse;
import com.anyqn.mastodon.mastosync.repostitories.ConfigurationFileRepository;
import com.anyqn.mastodon.mastosync.repostitories.local.TwitterHeadersRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import okhttp3.logging.HttpLoggingInterceptor;

import java.util.Optional;

public class ConfigurationStateService {
    private final ConfigurationFileRepository configurationFileRepository =
            SimpleContextFabric.IT.getInstance(ConfigurationFileRepository.class);
    private final TwitterHeadersRepository twitterHeadersRepository =
            SimpleContextFabric.IT.getInstance(TwitterHeadersRepository.class);
    private final ObjectMapper mapper = ObjectMapperFabric.IT.getInstance();

    public boolean isTwitterConfigured() {
        return configurationFileRepository.getTwitterAccessTokenSecret().isPresent();
    }

    public HttpLoggingInterceptor.Level geHttpLoggingLevel() {
        return configurationFileRepository.geHttpLoggingLevel();
    }

    public boolean isRelease() {
        return configurationFileRepository.getProfile() == Profile.RELEASE;
    }

    public int getRetryAttempts() {
        return configurationFileRepository.geRetryAttempts();
    }

    public long getHttpTimeout() {
        return configurationFileRepository.getHttpTimeout();
    }

    public Optional<MastodonAccessToken> getMastodonMainUserToken() {
        return configurationFileRepository.getMastodonMainUserToken();
    }

    public Optional<MastodonAccountId> getMastodonMainUserAccountId() {
        return configurationFileRepository.getMastodonMainUserAccountId();
    }
    public Optional<MastodonAccountId> getMastodonReplyUserAccountId() {
        return configurationFileRepository.getMastodonReplyUserAccountId();
    }

    public Optional<MastodonAccessToken> getMastodonReplyUserToken() {
        return configurationFileRepository.getMastodonReplyUserToken();
    }

    public PingenHost getPingenHostForTwitter() {
        return configurationFileRepository.getPingenHostForTwitter();
    }

    public MastodonHost getMastodonHost() {
        return configurationFileRepository.getMastodonHost();
    }

    @SneakyThrows
    public void saveTwitterAuthHeader(TwitterUserAccessToken twitterUserAccessToken,
                                      TwitterCallDescriptor twitterCallDescriptor,
                                      TwitterAuthHeaderResponse twitterAuthHeaderResponse) {
        twitterHeadersRepository.saveTwitterAuthHeader(
                twitterUserAccessToken,
                twitterCallDescriptor.getCallMethod(),
                twitterCallDescriptor.getCalledUrl().toString(),
                mapper.writeValueAsString(twitterCallDescriptor.getCallParameters()),
                twitterAuthHeaderResponse.getHeader());

    }

    @SneakyThrows
    public Optional<String> getTwitterAuthHeader(TwitterUserAccessToken twitterUserAccessToken,
                                                 TwitterCallDescriptor twitterCallDescriptor) {
        return twitterHeadersRepository.getTwitterAuthHeader(
                twitterUserAccessToken,
                twitterCallDescriptor.getCallMethod(),
                twitterCallDescriptor.getCalledUrl().toString(),
                mapper.writeValueAsString(twitterCallDescriptor.getCallParameters()));
    }

    public Optional<TwitterUserAccessToken> getTwitterAccessToken() {
        return configurationFileRepository.getTwitterAccessToken();
    }

    public Optional<TwitterUserAccessTokenSecret> getTwitterAccessTokenSecret() {
        return configurationFileRepository.getTwitterAccessTokenSecret();
    }

    public Optional<TwitterUserId> getTwitterMainUserId() {
        return configurationFileRepository.getTwitterMainUserId();
    }

    public void saveTwitterToken(TwitterAccessTokenResponse twitterAccessTokenResponse) {
        configurationFileRepository.saveTwitterToken(twitterAccessTokenResponse);
    }

    public int getFetcherDelayMillis(){
        return configurationFileRepository.getFetcherDelayMillis();
    }

    public long getFetcherShutdownTimoutMillis() {
        return configurationFileRepository.getFetcherShutdownTimoutMillis();
    }
}
