package com.anyqn.mastodon.mastosync.controllers;

import com.anyqn.mastodon.common.enums.Scope;
import com.anyqn.mastodon.common.models.values.mastodon.MastodonAuthorizationCode;
import com.anyqn.mastodon.common.models.values.twitter.TwitterPinCode;
import com.anyqn.mastodon.mastosync.fabrics.SimpleContextFabric;
import com.anyqn.mastodon.mastosync.models.mastodon.MastodonPinUrlResponse;
import com.anyqn.mastodon.common.models.mastodon.MastodonTokenResponse;
import com.anyqn.mastodon.mastosync.models.twitter.TwitterAccessTokenResponse;
import com.anyqn.mastodon.mastosync.models.twitter.TwitterPinUrlResponse;
import com.anyqn.mastodon.mastosync.services.UserInputService;
import com.anyqn.mastodon.mastosync.services.mastodon.MastodonAuthService;
import com.anyqn.mastodon.mastosync.services.twitter.TwitterAuthService;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

@Slf4j
public class UserRegistrationController {

    private final UserInputService userInputService =
            SimpleContextFabric.IT.getInstance(UserInputService.class);
    private final MastodonAuthService mastodonAuthService =
            SimpleContextFabric.IT.getInstance(MastodonAuthService.class);
    private final TwitterAuthService twitterAuthService =
            SimpleContextFabric.IT.getInstance(TwitterAuthService.class);

    public void fetchTokens() {
        //TODO: утащить бизнесс логику в сервис сам twitterAuthService, а общение с юзером оставить тут
        if (!mastodonAuthService.isMastodonMainUserTokenSaved()) {
            log.info("Mastodon authorization url request for main user");
            MastodonPinUrlResponse mastodonPinUrl = mastodonAuthService.getMastodonPinUrl(Set.of(Scope.READ));
            log.info("Please open this url in browser and copy authorization code from Mastodon server:\n{}",
                    mastodonPinUrl.getAuthorizationUrl());
            log.info("Paste Mastodon server authorization code to console or write here:");
            MastodonAuthorizationCode mastodonCode = userInputService.getMastodonCode();
            MastodonTokenResponse mastodonTokenResponse =
                    mastodonAuthService.getMastodonToken(mastodonCode, Set.of(Scope.READ));
            mastodonAuthService.saveMastodonMainUserToken(mastodonTokenResponse);
        }
        if (!mastodonAuthService.isMastodonReplyUserTokenSaved()) {
            log.info("Mastodon authorization url request for reply user");
            MastodonPinUrlResponse mastodonPinUrl = mastodonAuthService.getMastodonPinUrl(Set.of(Scope.READ, Scope.WRITE));
            log.info("Please open this url in browser and copy authorization code from Mastodon server:\n{}",
                    mastodonPinUrl.getAuthorizationUrl());
            log.info("Paste Mastodon server authorization code to console or write here:");
            MastodonAuthorizationCode mastodonCode = userInputService.getMastodonCode();
            MastodonTokenResponse mastodonTokenResponse =
                    mastodonAuthService.getMastodonToken(mastodonCode, Set.of(Scope.READ, Scope.WRITE));
            mastodonAuthService.saveMastodonReplyUserToken(mastodonTokenResponse);
        }
        if (!twitterAuthService.isTwitterTokenSaved()) {
            log.info("Twitter authorization url request");
            TwitterPinUrlResponse twitterPinUrlResponse =
                    twitterAuthService.getTwitterPinUrl();
            log.info("Please open this url in browser and copy authorization PIN from Twitter server:\n{}",
                    twitterPinUrlResponse.getAuthorizationUrl());
            log.info("Paste Twitter server authorization PIN to console or write here: ");
            TwitterPinCode twitterPin = userInputService.getTwitterPin();

            TwitterAccessTokenResponse twitterAccessTokenResponse = twitterAuthService.getTwitterToken(twitterPin,
                    twitterPinUrlResponse.getOauthToken());
            twitterAuthService.saveTwitterToken(twitterAccessTokenResponse);
        }
        log.info("Access tokens collected");
    }
}
