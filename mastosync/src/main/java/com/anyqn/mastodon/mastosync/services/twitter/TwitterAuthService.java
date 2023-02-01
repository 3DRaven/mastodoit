package com.anyqn.mastodon.mastosync.services.twitter;

import com.anyqn.mastodon.common.models.twitter.TwitterCallDescriptor;
import com.anyqn.mastodon.common.models.values.twitter.TwitterPinCode;
import com.anyqn.mastodon.common.models.values.twitter.TwitterUserAccessToken;
import com.anyqn.mastodon.mastosync.fabrics.SimpleContextFabric;
import com.anyqn.mastodon.mastosync.models.twitter.TwitterAccessTokenResponse;
import com.anyqn.mastodon.mastosync.models.twitter.TwitterPinUrlResponse;
import com.anyqn.mastodon.mastosync.repostitories.pingen.PingenAuthRepository;
import com.anyqn.mastodon.mastosync.repostitories.twitter.UnauthorizedAuthRepository;
import com.anyqn.mastodon.mastosync.services.ConfigurationStateService;

public class TwitterAuthService {

    private final PingenAuthRepository pingenAuthRepository =
            SimpleContextFabric.IT.getInstance(PingenAuthRepository.class);
    private final UnauthorizedAuthRepository twitterAuthRepository =
            SimpleContextFabric.IT.getInstance(UnauthorizedAuthRepository.class);
    private final ConfigurationStateService configurationStateService =
            SimpleContextFabric.IT.getInstance(ConfigurationStateService.class);

    public TwitterAccessTokenResponse getTwitterToken(TwitterPinCode code, TwitterUserAccessToken accessToken) {
        return twitterAuthRepository.getTwitterToken(code, accessToken);
    }

    public TwitterPinUrlResponse getTwitterPinUrl() {
        return pingenAuthRepository.getTwitterPinUrl(configurationStateService.getPingenHostForTwitter());
    }

    public String getTwitterAuthHeader(boolean forceGet, TwitterCallDescriptor twitterCallDescriptor) {
        if (forceGet) {
            return getTwitterAuthHeader(twitterCallDescriptor);
        } else {
            return configurationStateService.getTwitterAuthHeader(
                            configurationStateService.getTwitterAccessToken().orElseThrow(),
                            twitterCallDescriptor)
                    .orElseGet(() -> getTwitterAuthHeader(twitterCallDescriptor));
        }
    }

    private String getTwitterAuthHeader(TwitterCallDescriptor twitterCallDescriptor) {
        var twitterAuthHeaderResponse = pingenAuthRepository.getTwitterAuthHeader(
                configurationStateService.getPingenHostForTwitter(),
                twitterCallDescriptor);
        configurationStateService.saveTwitterAuthHeader(
                configurationStateService.getTwitterAccessToken().orElseThrow(),
                twitterCallDescriptor,
                twitterAuthHeaderResponse);
        return twitterAuthHeaderResponse.getHeader();
    }

    public boolean isTwitterTokenSaved() {
        return configurationStateService.getTwitterAccessToken().isPresent();
    }

    public void saveTwitterToken(TwitterAccessTokenResponse twitterAccessTokenResponse) {
        configurationStateService.saveTwitterToken(twitterAccessTokenResponse);
    }
}
