package com.anyqn.mastodon.mastosync.repostitories;

import com.anyqn.mastodon.common.models.values.PingenHost;
import com.anyqn.mastodon.common.models.values.mastodon.MastodonAccessToken;
import com.anyqn.mastodon.common.models.values.mastodon.MastodonAccountId;
import com.anyqn.mastodon.common.models.values.mastodon.MastodonHost;
import com.anyqn.mastodon.common.models.values.twitter.TwitterUserAccessToken;
import com.anyqn.mastodon.common.models.values.twitter.TwitterUserAccessTokenSecret;
import com.anyqn.mastodon.common.models.values.twitter.TwitterUserId;
import com.anyqn.mastodon.common.util.Fluent;
import com.anyqn.mastodon.mastosync.enums.Profile;
import com.anyqn.mastodon.common.models.mastodon.MastodonTokenResponse;
import com.anyqn.mastodon.mastosync.models.mastodon.account.MastodonAccount;
import com.anyqn.mastodon.mastosync.models.twitter.TwitterAccessTokenResponse;
import okhttp3.logging.HttpLoggingInterceptor;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;

import java.util.Optional;

public class ConfigurationFileRepository {
    private final Configuration config;

    public ConfigurationFileRepository() {
        Parameters params = new Parameters();
        FileBasedConfigurationBuilder<FileBasedConfiguration> builder =
                new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
                        .configure(params.properties()
                                .setFileName("application.properties"));
        builder.setAutoSave(true);
        config = Fluent.returnAndRethrow(builder::getConfiguration);
    }

    public int getInputRetries() {
        return config.getInt("input.retries");
    }

    public MastodonHost getMastodonHost() {
        return Optional
                .of(config.getString("mastodon.host"))
                .map(MastodonHost::new)
                .orElseThrow(() -> new IllegalArgumentException("Invalid mastodon host name"));
    }

    public Optional<MastodonAccountId> getMastodonMainUserAccountId() {
        return Optional.ofNullable(config.getString("mastodon.main.user.account.id")).map(MastodonAccountId::new);
    }

    public Optional<MastodonAccountId> getMastodonReplyUserAccountId() {
        return Optional.ofNullable(config.getString("mastodon.reply.user.account.id")).map(MastodonAccountId::new);
    }

    public PingenHost getPingenMastodonHost() {
        return Optional
                .of(config.getString("pingen.mastodon.host"))
                .map(PingenHost::new)
                .orElseThrow(() -> new IllegalArgumentException("Invalid pingen host for Mastodon name"));
    }

    public PingenHost getPingenHostForTwitter() {
        return Optional
                .of(config.getString("pingen.twitter.host"))
                .map(PingenHost::new)
                .orElseThrow(() -> new IllegalArgumentException("Invalid pingen host for Twitter name"));
    }

    public HttpLoggingInterceptor.Level geHttpLoggingLevel() {
        return HttpLoggingInterceptor.Level.valueOf(config.getString("http.logging.level"));
    }

    public int geRetryAttempts() {
        return config.getInt("http.retry.attempts");
    }


    public Profile getProfile() {
        return Profile.valueOf(config.getString("profile"));
    }

    public void saveMastodonMainUserToken(MastodonTokenResponse mastodonNodeUrl) {
        config.setProperty("mastodon.main.user.access.token", mastodonNodeUrl.getAccessToken());
    }

    public void saveMastodonMainUserAccountId(MastodonAccount mastodonAccount) {
        config.setProperty("mastodon.main.user.account.id", mastodonAccount.getId());
    }

    public Optional<MastodonAccessToken> getMastodonMainUserToken() {
        return Optional.ofNullable(config.getString("mastodon.main.user.access.token")).map(MastodonAccessToken::new);
    }

    public void saveMastodonReplyUserToken(MastodonTokenResponse mastodonNodeUrl) {
        config.setProperty("mastodon.reply.user.access.token", mastodonNodeUrl.getAccessToken());
    }

    public void saveMastodonReplyUserAccountId(MastodonAccount mastodonAccount) {
        config.setProperty("mastodon.reply.user.account.id", mastodonAccount.getId());
    }

    public Optional<MastodonAccessToken> getMastodonReplyUserToken() {
        return Optional.ofNullable(config.getString("mastodon.reply.user.access.token")).map(MastodonAccessToken::new);
    }


    public void saveTwitterToken(TwitterAccessTokenResponse twitterAccessTokenResponse) {
        config.setProperty("twitter.access.token", twitterAccessTokenResponse.getOauthToken());
        config.setProperty("twitter.access.token.secret", twitterAccessTokenResponse.getOauthTokenSecret());
        config.setProperty("twitter.user.id", twitterAccessTokenResponse.getUserId());
        config.setProperty("twitter.name", twitterAccessTokenResponse.getScreenName());
    }

    public Optional<TwitterUserAccessToken> getTwitterAccessToken() {
        return Optional.ofNullable(config.getString("twitter.access.token")).map(TwitterUserAccessToken::new);
    }

    public Optional<TwitterUserAccessTokenSecret> getTwitterAccessTokenSecret() {
        return Optional.ofNullable(config.getString("twitter.access.token.secret")).map(TwitterUserAccessTokenSecret::new);
    }

    public long getHttpTimeout() {
        return config.getLong("http.timout.millis");
    }

    public Optional<TwitterUserId> getTwitterMainUserId() {
        return Optional.ofNullable(config.getString("twitter.user.id")).map(TwitterUserId::new);
    }

    public int getFetcherDelayMillis() {
        return config.getInt("fetcher.delay.millis");
    }

    public long getFetcherShutdownTimoutMillis() {
        return config.getInt("fetcher.shutdown.timeout.millis");
    }
}
